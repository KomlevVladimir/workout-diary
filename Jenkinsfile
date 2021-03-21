pipeline {
    agent {
        docker {
        image 'komlevvladimir/workout-diary-backend-integration-tests'
        args '-u 0:0 --network host'
        alwaysPull true
        registryUrl 'https://docker.io/'
        }
    }

    environment {
            ALLURE_PATH="/home/jenkins/tools/ru.yandex.qatools.allure.jenkins.tools.AllureCommandlineInstallation/allure/"
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
                             sh 'mkdir -p $ALLURE_PATH'
                             sh 'cp -r /usr/bin/ $ALLURE_PATH'
                             sh 'chmod -R 777 build/allure-results'
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
