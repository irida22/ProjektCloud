pipeline {
    agent any

    tools {
        jdk 'JDK-17'
    }

    environment {
        PROJECT_DIR = 'student-management'
        JAR_FILE    = 'web/target/web-1.0.0-SNAPSHOT.jar'
    }

    stages {
        stage('1. Checkout (Pull from Git)') {
            steps {
                checkout scm
                echo "Repository: ${env.GIT_URL ?: 'GitHub'}"
                echo "Branch: ${env.GIT_BRANCH ?: 'main'}"
            }
        }

        stage('2. Build (mvn clean install)') {
            steps {
                dir("${PROJECT_DIR}") {
                    script {
                        if (isUnix()) {
                            sh 'chmod +x mvnw && ./mvnw clean install'
                        } else {
                            bat 'mvnw.cmd clean install'
                        }
                    }
                }
            }
        }

        stage('3. Test Results (JUnit)') {
            steps {
                dir("${PROJECT_DIR}") {
                    junit allowEmptyResults: false, testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('4. Archive Artifact (.jar)') {
            steps {
                dir("${PROJECT_DIR}") {
                    archiveArtifacts artifacts: 'web/target/*.jar', fingerprint: true, onlyIfSuccessful: true
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline SUCCESS: build, tests, and JAR artifact completed.'
        }
        failure {
            echo 'Pipeline FAILED. Check console output above.'
        }
        always {
            dir("${PROJECT_DIR}") {
                script {
                    if (isUnix()) {
                        sh 'ls -la web/target/*.jar 2>/dev/null || true'
                    } else {
                        bat 'dir web\\target\\*.jar 2>nul || echo No JAR found'
                    }
                }
            }
        }
    }
}
