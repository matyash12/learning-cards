pipeline {
    agent any

    environment {
        // Define environment variables if needed
        NODEJS_HOME = tool 'NodeJS' // Assuming you have a NodeJS tool configured in Jenkins
        PATH = "$NODEJS_HOME/bin:${env.PATH}"
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
                    // Change to the 'frontend' directory before running npm install
                    script {
                        // You might need to customize the npm registry or use a mirror
                        // Example: sh 'npm config set registry https://registry.npmjs.org/'
                        sh 'npm install'
                    }
                }
            }
        }

        stage('Build') {
            steps {
                dir('frontend') {
                    // Change to the 'frontend' directory before running the build
                    script {
                        // Customize the build command if needed (e.g., npm run build)
                        sh 'npm run build'
                    }
                }
            }
        }

        stage('Publish Artifacts') {
            steps {
                // Assuming your build artifacts are in the 'dist' directory
                archiveArtifacts 'frontend/dist/**'
            }
        }
    }

    post {
        success {
            // Add post-build actions or notifications on success if needed
            echo 'Build successful!'
        }

        failure {
            // Add post-build actions or notifications on failure if needed
            echo 'Build failed!'
        }
    }
}
