pipeline {
    agent any

    stages {
        stage('Integration tests') {
            agent {
                docker {
                    image 'komlevvladimir/workout-diary-backend-integration-tests'
                    args '-u 0:0 --network host'
                    alwaysPull true
                    registryUrl 'https://docker.io/'
                }
            }
            steps
                sh "mv /tests ."
                dir("tests") {
                    script {
                        try {
                            sh "./gradlew clean test -i --no-daemon"
                        } finally {
                            stash name: 'allure-results', includes: 'build/allure-results/*'
                        }
                    }
                }
            }
        }
    }
    post {
        always {
            unstash 'allure-results'
            allure results: [[path: 'build/allure-results']]
            cleanWs()
        }
    }
}
