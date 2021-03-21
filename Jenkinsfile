pipeline {


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
                            sleep(300)
                           allure includeProperties: false, jdk: '', results: [[path: 'build/allure-results']]
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
