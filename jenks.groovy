node('Linux'){
    try{
        def mvnHome = tool 'Maven333'
        env.PATH = "${mvnHome}/bin:${env.PATH}"

    stage name: 'Git Clone', concurrency: 1
        checkout([$class: 'GitSCM',
        branches: [[name: '*/master']],
        doGenerateSubmoduleConfigurations: false,
        extensions: [], submoduleCfg: [],
        userRemoteConfigs: [[credentialsId: 'aa1c8452-6c57-40d4-814e-99ae1b74d1a9', url: 'git@github.com:okram999/mavenquick.git']]])

    stage name: 'Test & Publish', concurrency: 1
     try{
        sh 'mvn test -B'
      }
      catch(err){
        sh 'echo "Test have a FAILURE"'
        throw err
        currentBuild.result = 'FAILURE'
      } finally {
        junit '**\\target\\surefire-reports\\*.xml'
      }


    stage name: 'Code_Scan', concurrency: 1
        sh 'mvn verify cobertura:cobertura sonar:sonar sonar-runner -Dsonar.login='ci-user' -Dsonar.password='ci-user''


    stage name: 'Deploy To Lab', concurrency: 1


    }

    catch(err){
      stage 'Send Email Notification'
        sh 'echo "There was an error"'
        throw err
        currentBuild.result = 'FAILURE'
    }

}
