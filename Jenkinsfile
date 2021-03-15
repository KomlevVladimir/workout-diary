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
                sh "mv /tests ."
                sh "cd tests"
                sh "pwd"
                sh "ls -la"
                sh "cd gradle"
                sh "pwd"
                sh "ls -la"
                sleep(100)
            }
        }
    }

    post {
            always {
                cleanWs()
            }
        }
}
