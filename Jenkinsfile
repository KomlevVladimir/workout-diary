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
                sh "mv -f /tests ."
                sh "cd tests"
                sh "ls -la"
                sh "cat docker-compose.yml"
                sh "docker ps -a"
//                 sh "./gradlew clean test -i --no-daemon"
            }
        }
    }

    post {
            always {
                cleanWs()
            }
        }
}
