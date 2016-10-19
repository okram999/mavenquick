
node('Linux'){
    try{
        def mvnHome = tool 'Maven333'
        env.PATH = "${mvnHome}/bin:${env.PATH}"

    stage name: 'Git Clone', concurrency: 1
        checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'aa1c8452-6c57-40d4-814e-99ae1b74d1a9', url: 'git@github.com:okram999/mavenquick.git']]])

    stage name: 'Build & Test', concurrency: 1
        mvn verify sonar:sonar
    }

    catch(err){
      stage 'Send Email Notification'
        sh 'echo "There was an error"'
    }

}
