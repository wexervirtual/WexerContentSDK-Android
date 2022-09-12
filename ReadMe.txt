How to implement SDK :

There are 2 methods to implement SDK :

1. Using AAR file:
	1. Copy contentsdk.aar and paste it in SdkSample/app/libs folder.
	2. Go to app/build.gradle and add the following lines dependencies :
		implementation 'com.squareup.retrofit2:retrofit:2.6.2'
		implementation 'com.squareup.retrofit2:converter-gson:2.6.2' 
		implementation(name: 'contentsdk', ext: 'aar')
	3. Go to root build.gradle and enable flat dir by pasting the following in allprojects -> repositories ->
		flatDir { 
			dirs 'libs' 
		}

2. Using gradle(recommended for future release, one time setup):
	1. Go to app/build.gradle and add the following lines dependencies :
		implementation 'com.squareup.retrofit2:retrofit:2.6.2'
		implementation 'com.squareup.retrofit2:converter-gson:2.6.2'
	2. Add it in your root build.gradle at the end of repositories:
           allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	  }
	3. Add following lines in module's build.gradle :
		implementation ('com.github.wexervirtual:WexerContentSDK-Android:1.2.1@aar'){transitive = true}




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
