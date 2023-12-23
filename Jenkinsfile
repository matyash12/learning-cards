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

        stage('Copy application.properties file to workspace (java)') {
            steps {
                withCredentials([file(credentialsId: 'LEARNING_CARDS_APPLICATION_PROPERTIES', variable: 'application_properties')]) {
                    script {
                        def appContent = readFile(application_properties)
                        writeFile file: 'backend/src/main/resources/application.properties', text: appContent
                    }
                }
            }
        }
        stage('Copy manifest.json file to workspace') {
            steps {
                withCredentials([file(credentialsId: 'LEARNING_CARDS_MANIFESTJSON', variable: 'manifestjson')]) {
                    script {
                        def manifestContent = readFile(manifestjson)
                        writeFile file: 'frontend/public/manifest.json', text: manifestContent
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
