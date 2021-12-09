How to implement SDK :

There are 2 methods to implement SDK :

1. Using AAR file:
	1. Copy Framework/contentsdk.aar and paste it in SdkSample/app/libs folder.
	2. Go to app/build.gradle and add the following lines dependencies :
		implementation 'com.squareup.retrofit2:retrofit:2.6.2'
		implementation 'com.squareup.retrofit2:converter-gson:2.6.2' 
	3. Go to root build.gradle and enable flat dir by pasting the following in allprojects -> repositories ->
		flatDir { 
			dirs 'libs' 
		}

2. Using gradle(recommended for future release, one time setup):
	1. In root level build.gradle add the follwing lines:
		maven {
	            url  "https://maven.pkg.github.com/wexervirtual/WexerContentSDK-Android"
        	    credentials {
        	        username = githubProperties['gpr.usr'] ?: System.getenv("GPR_USER")
        	        password = githubProperties['gpr.key'] ?: System.getenv("GPR_API_KEY")
        	    }
        	}
	2. Add following lines at the end in your local.properties :
        	gpr.usr=<github_username>
        	gpr.key=<github_Personal_Access_Token>
	3. Add following lines in module's build.gradle :
		implementation ('com.wexer.virtual:contentsdk:1.0.0'){transitive = true}




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
