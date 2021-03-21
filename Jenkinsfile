final String gitRepo = 'github.com/KomlevVladimir/workout-diary-backend.git'
final String githubCredentialsId = 'github credentials'
final String registryName = 'docker.io/komlevvladimir'
final String imageName = 'workout-diary-backend'
String version

def getDateTime = {
    DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(ZoneDateTime.now(ZoneOffset.UTC))
}

pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                dir('workout-diary-backend') {
                    git url: "https://$gitRepo", branch: 'jenkinsfile', credentialsId: githubCredentialsId

                    def commitHash = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
                    version = "${getDateTime()}-$commitHash"

                    sh 'chmod +x gradlew && ./gradlew clean build --no-daemon'

                    withCredentials([usernamePassword(credentialsId: githubCredentialsId,
                        passwordVariable: 'GITHUB_PASSWORD', usernameVariable: 'GITHUB_USERNAME')]) {
                            sh("docker login $registryName -u '$GITHUB_USERNAME' -p '$GITHUB_PASSWORD'")
                        }

                    sh "docker rmi -f \$(docker images '*/$imageName:latest' -q) || true"

                    docker.withRegistry("https://$registryName") {
                        def image = docker.build(imageName)
                        image.push(version)
                        image.push('latest')
                    }
                }
            }
        }

        stage('Integration tests') {
            agent {
                docker {
                image 'komlevvladimir/workout-diary-backend-integration-tests'
                args '-u 0:0 --network host'
                alwaysPull true
                registryUrl 'https://docker.io/'
                }
            }
            steps {
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
