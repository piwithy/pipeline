pipeline{
    agent any

    stages{
        stage('Prep: Retrieving Sources'){
            steps{
                git branch:'master', credentialsId:'e0cb256b-a73f-4d4c-9ea4-06eaef20da82', url:'https://piwithy@bitbucket.org/pi_client/server.git'
            }
        }

        stage('Build: Building Pi Server'){
            sh 'mvn clean package'
        }

        stage('Arch: Archiving Artifacts'){
            archiveArtifacts artifacts:'**/target/PiServer-*.jar', fingerprint:true
        }
    }

    post{
        always{
            junit  testResults:'**/target/surefire-reports/TEST-*.xml', allowEmptyResults:true
        }

        cleanup{
            cleanWs()
        }
    }
}
