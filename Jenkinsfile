#!/usr/bin/env groovy

node('nimble-jenkins-slave') {

    stage('Clone and Update') {
        git(url: 'https://github.com/nimble-platform/catalog-search-service.git', branch: env.BRANCH_NAME)
    }

    stage('Build Java') {
        sh 'mvn clean package -DskipTests'
    }

    if (env.BRANCH_NAME == 'staging') {
        stage('Build Docker') {
            sh 'mvn docker:build -P docker -DdockerImageTag=staging'
        }

        stage('Push Docker') {
            sh 'docker push nimbleplatform/catalog-search-service:staging'
        }

        stage('Deploy') {
            sh 'ssh staging "cd /srv/nimble-staging/ && ./run-staging.sh restart-single search-service"'
        }
    } else {
        stage('Build Docker') {
            sh 'mvn docker:build -P docker'
        }

        stage('Deploy') {
            sh 'ssh nimble "cd /data/deployment_setup/prod/ && sudo ./run-prod.sh restart-single search-service"'
        }
    }

    // push and apply only master branch
//    if (env.BRANCH_NAME == 'master') {
//        stage('Push Docker') {
//            withDockerRegistry([credentialsId: 'NimbleDocker']) {
//                sh 'docker push nimbleplatform/catalog-search-service'
//            }
//        }
//
//        stage('Apply to Cluster') {
//            sh 'kubectl apply -f kubernetes/deploy-prod.yml -n prod --validate=false'
//        }
//    }
}
