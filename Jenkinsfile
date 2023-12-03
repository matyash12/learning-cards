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

        stage('Copy .env file to workspace') {
            steps {
                withCredentials([file(credentialsId: 'LEARNING_CARDS_ENV_FILE', variable: 'env_file')]) {
                    script {
                        def envContent = readFile(env_file)
                        writeFile file: '.env', text: envContent
                    }
                }
            }
        }


        stage('Run ./build.sh') {
            steps {
                script {
                    sh "./build.sh"
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
