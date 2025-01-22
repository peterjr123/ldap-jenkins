pipeline {
    agent any
    stages {
        stage('Permission') {
            steps {
                sh 'chmod +x ./gradlew'
                sh 'chmod +x ./jenkins/scripts/*.sh'
            }
        }
        stage('Test') {
            steps {
                sh './gradlew check'
            }
            post {
                success {
                    junit 'build/test-results/**/*.xml'
                }
            }
        }
        stage('build') {
            steps {
                sh './gradlew build'
            }
        }
        stage('SonarQube analysis') {
            steps {
                withSonarQubeEnv(installationName: 'sonarqube_server') {
                    sh './gradlew sonar'
                }
            }

        }
        stage('SonarQube Quality Gate') {
            steps {
                timeout(time: 30, unit: 'SECONDS') {
                    waitForQualityGate abortPipeline: true
                }
            }
            post {
                success {
                    mail to: 'jsp020206@gmail.com',
                         subject: "sonarqube quality gate passed",
                         body: "sonarqube project URL: http://localhost:9000/dashboard?id=ldap-project-key"
                }
            }
        }
        stage('Acceptance Test') {
            steps {
                sh './jenkins/scripts/deploy-for-acceptance-test.sh'

                script {
                    def approvalLink = "${env.BUILD_URL}input"
                    def emailBody = """
                        <p>파이프라인 승인이 필요합니다.</p>
                        <p><a href="${approvalLink}">여기를 클릭하여 승인 또는 거부하세요</a></p>
                        """
                    mail to: "jsp020206@gmail.com",
                         subject: "Jenkins 파이프라인 승인 요청: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                         body: emailBody,
                         mimeType: "text/html"
                }
                input message: 'Approve? (Click "Proceed" to approve)'

                sh './jenkins/scripts/kill.sh'
            }
        }
        stage('remove previous container') {
            steps {
                sh 'docker compose down'
            }
        }
        stage('deploy to container') {
            steps {
                sh 'docker build -t ldap-sdk-deploy .'
                sh 'docker compose up -d'
            }
        }
    }
    post {
        success {
            echo 'This will run only if successful'

            mail to: 'jsp020206@gmail.com',
                subject: "pipeline success ${currentBuild.fullDisplayName}",
                body: "build url: ${env.BUILD_URL}"
        }
        failure {
            echo 'This will run only if failed'
        }
    }
}