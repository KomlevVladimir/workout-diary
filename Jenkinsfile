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
                sh "cd tests && ./gradlew clean test -i --no-daemon"
            }
        }
    }
//
//     post {
//             always {
//                 cleanWs()
//             }
//         }
}
