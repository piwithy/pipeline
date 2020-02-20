pipeline{
    agent any
    stages{
        stage('Builds'){
            parallel{
                stage('Client'){
                    steps{
                        build job:'Pi_Client_SCM'
                    }
                }

                stage('Server'){
                    steps{
                        build job:'Pi_Server_SCM'
                    }
                }
            }
        }

        stage('Retrieving Artifacts'){
            steps{
                copyArtifacts fingerprintArtifacts:true, projectName:'Pi_Client_SCM', filter:'target/*.jar'
                copyArtifacts fingerprintArtifacts:true, projectName:'Pi_Server_SCM', filter:'target/*.jar'
            }
        }

        stage('Executions'){
            parallel{
                stage('Client'){
                    steps{
                        sleep(time:5, unit:"SECONDS")
                        sh 'java -jar target/PiClient-*.jar'
                    }
                }

                stage('Server'){
                    steps{
                        sh 'java -jar target/PiServer-*.jar'
                    }
                }
            }
        }

        stage('Arch: Archiving Artifacts'){
            steps{
                archiveArtifacts artifacts:'**/target/Pi*-*.jar', fingerprint:true

            }
        }
    }
}
