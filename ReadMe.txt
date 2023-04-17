How to implement SDK :

There are 2 methods to implement SDK :

1. Using AAR file:
	1. Copy contentsdk.aar and paste it in SdkSample/app/libs folder.

	2. Go to app/build.gradle and add the following lines dependencies :
		implementation 'com.squareup.retrofit2:retrofit:2.6.2'
		implementation 'com.squareup.retrofit2:converter-gson:2.6.2' 
		implementation files('libs/contentsdk.aar')
	
	3. Add the following line in gradle.properties file: 
		android.enableJetifier=true 


2. Using gradle(recommended for future release, one time setup):
	1. Go to app/build.gradle and add the following lines dependencies :
		implementation 'com.squareup.retrofit2:retrofit:2.6.2'
		implementation 'com.squareup.retrofit2:converter-gson:2.6.2'

	2. Go to the settings.gradle file and add the maven { url 'https://jitpack.io' } 
	     line below all the repositories :
		dependencyResolutionManagement {
    		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    		repositories {
       			 google()
       			 mavenCentral()
     			   maven { url 'https://jitpack.io' }
   			 }
		}

	3. 	Add the following line in gradle.properties file: 

		android.enableJetifier=true 

	4.  Add following lines in module's build.gradle :
		    implementation ('com.github.wexervirtual:WexerContentSDK-Android:1.3.0@aar'){transitive = true}
		OR
		implementation 'com.github.wexervirtual:WexerContentSDK-Android:1.3.0'





How to configure SDK :

In MainActivity.kt file

Step(s)

1. Go into setConfig() method. This is called from activity onCreate() method.
2. Create WCSDKConfig object by passing real values in constructor. for example
val config = WCSDKConfig(
            applicationContext, // app context
            "http://google.com",
            "<CLIENT_ID>",
            "<CLIENT_SECRET>",
            "<TENANT_ID>"
        ) 
		
Note. It require to put proper formatted values like (url with http/https) in constructor, otherwise app will crash.

Integration Guid => https://github.com/wexervirtual/WexerContentSDK-Android/blob/master/Wexer-Android-Kotlin-Content%20SDK%20Integration%20Guide.pdf

localytics.xml ==> 
https://github.com/wexervirtual/WexerContentSDK-Android/blob/master/SdkSample/app/src/main/res/values/localytics.xml
