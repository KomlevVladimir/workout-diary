pipeline {
    agent any
    stages {
        stage('Test') {
            agent {
                docker {
                image 'komlevvladimir/workout-diary-backend-integration-tests'
                args '-u 0:0 --network host'
                alwaysPull true
                registryUrl 'https://docker.io/'
                }
            }
            steps {
                sh "java -version"
                sh "mv /tests ."
                dir("tests") {
                    script {
                        try {
                            sh "./gradlew clean test -i --no-daemon"
                        } finally {
                           allure results: [[path: 'build/allure-results']]
                        }
                    }
                }
            }
        }
    }
    post {
        always {
            cleanWs()
        }
    }
}
