pipeline {
    agent {
        docker {
        image 'komlevvladimir/workout-diary-backend-integration-tests'
        args '-u 0:0 --network host'
        alwaysPull true
        registryUrl 'https://docker.io/'
        }
    }
    stages {
        stage('Test') {
            steps {
                sh "java -version"
                sh "mv /tests ."
                dir("tests") {
                    script {
                        try {
                            sh "./gradlew clean test -i --no-daemon"
                        } finally {
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
