pipeline{
    agent any
    stages{
        stage('Builds'){
            parallel{
                stage('Client'){
                    steps{
                        build job:'Client'
                    }
                }

                stage('Server'){
                    steps{
                        build job:'Server'
                    }
                }
            }
        }

        stage('Retrieving Artifacts'){
            steps{
                copyArtifacts fingerprintArtifacts:true, projectName:'Client', filter:'target/*.jar'
                copyArtifacts fingerprintArtifacts:true, projectName:'Server', filter:'target/*.jar'
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
