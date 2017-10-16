#!/usr/bin/env groovy

node('nimble-jenkins-slave') {

    stage('Clone and Update') {
        git(url: 'https://github.com/nimble-platform/catalog-search-service.git', branch: env.BRANCH_NAME)
    }

    stage('Build Java') {
        sh 'mvn clean package -DskipTests'
    }

    stage('Build Docker') {
        sh 'mvn docker:build -P docker'
    }

    // push and apply only master branch
    if (env.BRANCH_NAME == 'master') {
        stage('Push Docker') {
            withDockerRegistry([credentialsId: 'NimbleDocker']) {
                sh 'docker push nimbleplatform/catalog-search-service'
            }
        }

        stage('Apply to Cluster') {
            sh 'kubectl apply -f kubernetes/deploy-prod.yml -n prod --validate=false'
        }
    }
}
