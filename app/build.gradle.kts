import com.beetlestance.buildsrc.Libs

plugins {
    id 'com.android.application'
    id 'kotlin-android'
    id 'kotlin-kapt'
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
}

ext {
    ci = System.getenv("CI") == "true"
}

android {
    compileSdkVersion 29

    defaultConfig {
        applicationId "com.beetlestance.aphid"
        minSdkVersion 21
        targetSdkVersion 29
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_12
        targetCompatibility JavaVersion.VERSION_12
    }

    dexOptions {
        // Don't pre-dex on CI
        preDexLibraries !ci
    }

    lintOptions {
        // Disable lintVital. Not needed since lint is run on CI
        checkReleaseBuilds false
        // Allow lint to check dependencies
        checkDependencies true
        // Ignore any tests
        ignoreTestSources true

        // Lint doesn't seem to handle Kotlin int types + string format very well
        disable 'StringFormatMatches'
    }

    buildFeatures {
        // We need to keep this enabled because submodules use it
        dataBinding true

        viewBinding true
    }
}

dependencies {

    // Local projects
    implementation project(':base')
    implementation project(':data')
    implementation project(':base-android')
    implementation project(':data-android')
    implementation project(':domain')
    implementation project(':spoonacular-kotlin')

    // Lint checks
    lintChecks project(':mdc-theme-lint')

    // Testing
    testImplementation Libs.Test.junit
    androidTestImplementation Libs.AndroidX.Test.junit
    androidTestImplementation Libs.AndroidX.Test.espressoCore

    // AndroidX
    implementation Libs.AndroidX.appcompat
    implementation Libs.AndroidX.coreKtx
    implementation Libs.AndroidX.constraintlayout

    // Material Design
    implementation Libs.Google.material

    // Kotlin
    implementation Libs.Kotlin.stdlib
}
