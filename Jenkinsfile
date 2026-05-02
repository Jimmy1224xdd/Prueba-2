pipeline {
    agent any

    tools {
        maven 'Maven 3.9'
    }

    environment {
        DOCKER_IMAGE = 'tu-usuario-dockerhub/uniservicios'
        DOCKER_TAG   = "${BUILD_NUMBER}"
    }

    stages {

        // ─── 1. Obtener código ──────────────────────────────────────
        stage('Checkout') {
            steps {
                checkout scm
                echo "✅ Checkout completado — Rama: ${env.GIT_BRANCH}"
            }
        }

        // ─── 2. Verificar entorno CI (NUEVO — usa JenkinsDetectorMain) ─
        stage('Verify CI Environment') {
            steps {
                // Compila solo la clase utilitaria para ejecutarla antes
                // del build completo — demuestra que Jenkins la detecta.
                sh '''
                    javac -d target/classes src/main/java/util/JenkinsDetectorMain.java || true
                    java -cp target/classes util.JenkinsDetectorMain || true
                '''
                echo "Build #${BUILD_NUMBER} en job: ${JOB_NAME}"
            }
        }

        // ─── 3. Compilar ────────────────────────────────────────────
        stage('Build') {
            steps {
                sh 'mvn clean compile'
                echo "✅ Compilación exitosa — Build #${BUILD_NUMBER}"
            }
        }

        // ─── 4. Pruebas TDD ─────────────────────────────────────────
        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        // ─── 5. Empaquetar WAR ──────────────────────────────────────
        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
                archiveArtifacts artifacts: 'target/*.war', fingerprint: true
                echo "✅ WAR generado y archivado"
            }
        }

        // ─── 6. Build + Push Docker ─────────────────────────────────
        stage('Docker Build & Push') {
            steps {
                script {
                    sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                    sh "docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest"
                    // Descomentar cuando se configure credencial en Jenkins:
                    // withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', ...)]) {
                    //     sh "docker push ${DOCKER_IMAGE}:${DOCKER_TAG}"
                    //     sh "docker push ${DOCKER_IMAGE}:latest"
                    // }
                    echo "✅ Imagen Docker construida: ${DOCKER_IMAGE}:${DOCKER_TAG}"
                }
            }
        }

        // ─── 7. Deploy (opcional en este sprint) ────────────────────
        stage('Deploy') {
            steps {
                script {
                    echo 'Desplegando contenedor actualizado...'
                    sh 'docker-compose up -d --build app || true'
                }
            }
        }
    }

    post {
        success {
            echo "🎉 Pipeline #${BUILD_NUMBER} completado con éxito — ${JOB_NAME}"
        }
        failure {
            echo "❌ Pipeline #${BUILD_NUMBER} FALLÓ — Revisar logs en ${BUILD_URL}"
        }
        always {
            cleanWs()
        }
    }
}
