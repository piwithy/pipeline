pipeline{
    agent any

    stages{
        stage('Prep: Retrieving Sources'){
            steps{
                git branch:'master', credentialsId:'e0cb256b-a73f-4d4c-9ea4-06eaef20da82', url:'https://piwithy@bitbucket.org/pi_client/client.git'
            }
        }

        stage('Build: Building Pi Client'){
            steps{
                sh 'mvn clean package'
            }
        }

        stage('Arch: Archiving Artifacts'){
            steps{
                archiveArtifacts artifacts:'**/target/PiClient-*.jar', fingerprint:true
            }
        }
    }

    post{
        always{
            junit  testResults:'**/target/surefire-reports/TEST-*.xml', allowEmptyResults:true
            archiveArtifacts artifacts:'**/target/surefire-reports/TEST-*.xml', fingerprint:true, allowEmptyArchive:true
        }

        cleanup{
            cleanWs()
        }
    }
}
