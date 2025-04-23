pipeline {
  agent none
  stages {
    stage('Checkout') {
      agent {
        label 'android-node'
      }
      steps {
        checkout scm
      }
    }

    stage('Build & Analysis') {
      agent {
        label 'android-node'
      }
      steps {
        script {
          env.ANDROID_HOME = "${tool 'AndroidSDK'}"
          env.PATH = "${env.ANDROID_HOME}/cmdline-tools/latest/bin:${env.ANDROID_HOME}/platform-tools:${env.PATH}"

          // Baixando dependências e realizando o build
          sh './gradlew clean build'

          // Configurando o SonarQube para análise de código Kotlin
          def sqScannerAndroidHome = tool 'SonarQubeScanner'
          withSonarQubeEnv('SonarQube') {
            sh """
            ${sqScannerAndroidHome}/bin/sonar-scanner \
            -Dsonar.projectKey=XDMobileAPI \
            -Dsonar.sources=src/main/java \
            -Dsonar.java.binaries=build \
            -Dsonar.kotlin.detekt.reportPaths=build/reports/detekt/detekt.xml
            """
          }
        }

      }
    }

    stage('Quality Gate') {
      agent {
        label 'android-node'
      }
      steps {
        timeout(time: 15, unit: 'MINUTES') {
          script {
            waitForQualityGate abortPipeline: true
          }

        }

      }
    }

  }
  options {
    skipDefaultCheckout(true)
  }
}