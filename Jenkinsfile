pipeline {
    agent any

    tools {
        maven '3.9'
    }

    environment {
        DOCKER_IMAGE = 'jimmynow/uniservicios'
        DOCKER_TAG   = "${BUILD_NUMBER}"
    }

    stages {

        // ─── 1. Checkout con fix de saltos de linea ─────────────────
        stage('Checkout') {
            steps {
                checkout scm
                sh 'find . -name "*.sh" | xargs dos2unix 2>/dev/null || true'
                sh 'git config core.autocrlf false || true'
                echo "Checkout completado"
            }
        }

        // ─── 2. Build Maven: clean compile test package ──────────────
        stage('Build') {
            steps {
                sh 'mvn clean compile test package -DskipTests'
                echo "Compilacion y empaquetado exitosos — Build #${BUILD_NUMBER}"
            }
        }

        // ─── 3. Tests TDD ────────────────────────────────────────────
        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
                }
            }
        }

        // ─── 4. Verificar entorno CI (JenkinsDetectorMain) ───────────
        stage('Verify CI Environment') {
            steps {
                sh 'java -cp target/classes util.JenkinsDetectorMain'
                echo "Build #${BUILD_NUMBER} en job: ${JOB_NAME}"
            }
        }

        // ─── 5. Archivar WAR ─────────────────────────────────────────
        stage('Package') {
            steps {
                archiveArtifacts artifacts: 'target/*.war', fingerprint: true
                echo "WAR generado y archivado"
            }
        }

        // ─── 6. Docker Build ─────────────────────────────────────────
        stage('Docker Build') {
            steps {
                script {
                    sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                    sh "docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest"
                    echo "Imagen Docker construida: ${DOCKER_IMAGE}:${DOCKER_TAG}"
                }
            }
        }
    }

    post {
        success {
            echo "Pipeline #${BUILD_NUMBER} completado con exito — ${JOB_NAME}"
        }
        failure {
            echo "Pipeline #${BUILD_NUMBER} FALLO — Revisar logs"
        }
        always {
            cleanWs()
        }
    }
}
