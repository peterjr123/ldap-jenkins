pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                sh 'chmod +x ./gradlew'
                sh './gradlew check'
            }
        }
        stage('build') {
            steps {
                sh './gradlew build'
            }
        }
        stage('Deploy for production') {
            when {
                branch 'production'
            }
            steps {
                sh './jenkins/scripts/deploy-for-production.sh'
                input message: 'Finished using the web site? (Click "Proceed" to continue)'
                sh './jenkins/scripts/kill.sh'
            }
        }
    }
    post {
        always {
            echo 'This will always run'
        }
        success {
            echo 'This will run only if successful'
            junit 'build/test-results/**/*.xml'

            mail to: 'jsp020206@gmail.com',
                subject: 'pipeline success ${currentBuild.fullDisplayName}',
                body: 'build url: ${env.BUILD_URL}'
        }
        failure {
            echo 'This will run only if failed'
        }
    }
}