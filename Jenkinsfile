pipeline {
    agent any

    environment {
        NODEJS_HOME = tool 'NodeJS'
        PATH = "$NODEJS_HOME/bin:${env.PATH}"
        DOCKER_IMAGE = 'testjenkinsvue'
        LOCAL_DOCKER_SERVER = 'unix:///var/run/docker.sock' // Replace with your local Docker server address
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Install Dependencies') {
            steps {
                dir('frontend') {
                    script {
                        sh 'npm install'
                    }
                }
            }
        }

        stage('Build') {
            steps {
                dir('frontend') {
                    script {
                        sh 'npm run build'
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build Docker image with the Dockerfile in the 'frontend' directory
                    sh "docker build -t $DOCKER_IMAGE frontend"
                }
            }
        }

        stage('Deploy to Local Docker Server') {
            steps {
                script {
                    // Tag the Docker image for the local server
                    sh "docker tag $DOCKER_IMAGE $LOCAL_DOCKER_SERVER/$DOCKER_IMAGE"

                    // Push the Docker image to the local server
                    sh "docker push $LOCAL_DOCKER_SERVER/$DOCKER_IMAGE"
                }
            }
        }
    }

    post {
        success {
            echo 'Build and local deployment successful!'
        }

        failure {
            echo 'Build or local deployment failed!'
        }
    }
}
