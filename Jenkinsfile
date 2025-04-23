pipeline {
  agent any
  stages {
    stage('Checkout') {
      steps {
        sh '''            agent { label \'android-node\' }
            steps {
                checkout scm
            }'''
      }
    }

    stage('Build & Analysis') {
      steps {
        sh '''            agent { label \'android-node\' }
            steps {
                script {
                    // Configurando o ambiente para o Android SDK
                    env.ANDROID_HOME = "${tool \'AndroidSDK\'}"
                    env.PATH = "${env.ANDROID_HOME}/cmdline-tools/latest/bin:${env.ANDROID_HOME}/platform-tools:${env.PATH}"

                    // Baixando dependências e realizando o build
                    sh \'./gradlew clean build\'

                    // Configurando o SonarQube para análise de código Kotlin
                    def sqScannerAndroidHome = tool \'SonarQubeScanner\'
                    withSonarQubeEnv(\'SonarQube\') {
                        sh """
                        ${sqScannerAndroidHome}/bin/sonar-scanner \\
                        -Dsonar.projectKey=XDMobileAPI \\
                        -Dsonar.sources=src/main/java \\
                        -Dsonar.java.binaries=build \\
                        -Dsonar.kotlin.detekt.reportPaths=build/reports/detekt/detekt.xml
                        """
                    }
                }
            }'''
      }
    }

    stage('Quality Gate') {
      steps {
        sh '''            agent { label \'android-node\' }
            steps {
                timeout(time: 15, unit: \'MINUTES\') {
                    script {
                        // Verificando o Quality Gate do SonarQube
                        waitForQualityGate abortPipeline: true
                    }
                }
            }'''
      }
    }

  }
}