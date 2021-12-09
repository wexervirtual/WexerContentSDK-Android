package com.dmi.sdksample

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dmi.mykotlinlib.pojo.*
import com.dmi.mykotlinlib.start.ISdkInstance
import com.dmi.mykotlinlib.start.WCSDK
import com.dmi.mykotlinlib.start.WCSDKConfig
import com.dmi.mykotlinlib.start.exposedcallbacks.*
import com.dmi.mykotlinlib.vplayer.WCSDKView
import com.google.android.material.textfield.TextInputEditText
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    var mCheckBox: CheckBox? = null
    var searchEt: TextInputEditText? = null
    var mUsernameEditText: TextInputEditText? = null
    var collectionIdEt: TextInputEditText? = null
    var mPassowrdEditText: TextInputEditText? = null
    var mClasstag: TextInputEditText? = null
    var classtagDetEt: TextInputEditText? = null
    var consentTag: TextInputEditText? = null
    var wexerVideoView: WCSDKView? = null

    var buttonPlay: Button? = null
    var buttonPause: Button? = null
    var buttonExit: Button? = null
    var buttonFullScreen: Button? = null

    var orderByEt: TextInputEditText? = null
    var filterBy: TextInputEditText? = null

    var pageSizeEt: TextInputEditText? = null
    var pageNumberEt: TextInputEditText? = null
    lateinit var wexerSdk: ISdkInstance
    companion object{
        var videoViewBuilder: ISdkInstance.VideoViewBuilder? = null
    }



    private fun showProgress() {
        mContentLoadingProgressBar?.visibility = View.VISIBLE
        contentLl?.visibility = View.GONE
    }

    private fun hideProgress() {
        mContentLoadingProgressBar?.visibility = View.GONE
        contentLl?.visibility = View.VISIBLE
    }

    private fun showToastMsg(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    var contentLl: LinearLayout? = null
    var mContentLoadingProgressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mContentLoadingProgressBar = findViewById(R.id.pb)
        contentLl = findViewById(R.id.contentLL)
        wexerVideoView = findViewById(R.id.playerView)

        setConfig()

        initUi()
    }

    private fun initUi() {
        mCheckBox = findViewById(R.id.showPassCheckbox)
        searchEt = findViewById(R.id.searchEt)
        collectionIdEt = findViewById(R.id.collectionIdEt)
        classtagDetEt = findViewById(R.id.classtagDetEt)
        mUsernameEditText = findViewById(R.id.usernameEt)
        mPassowrdEditText = findViewById(R.id.passwordEt)
        mClasstag = findViewById(R.id.classtagEt)
//        consentTag = findViewById(R.id.consentTag)

        filterBy = findViewById(R.id.filterByEt)
        orderByEt = findViewById(R.id.orderByEt)

        pageSizeEt = findViewById(R.id.pageSizeEt)
        pageNumberEt = findViewById(R.id.pageNumberEt)

        buttonPlay = findViewById(R.id.buttonPlay)
        buttonPause = findViewById(R.id.buttonPause)
        buttonExit = findViewById(R.id.buttonExit)
        buttonFullScreen = findViewById(R.id.buttonFullScreen)


        buttonPlay?.setOnClickListener{
            videoViewBuilder?.playVideo()
            videoViewBuilder?.hideOverlayView()
        }

        buttonPause?.setOnClickListener{
            videoViewBuilder?.pauseVideo()
            videoViewBuilder?.showOverlayView()
        }

        buttonExit?.setOnClickListener{
            videoViewBuilder?.stopVideo()
        }

        buttonFullScreen?.setOnClickListener{
            isGoingSmallScreenToFullScreen = true
            videoViewBuilder?.openFullScreen()
        }

        mCheckBox?.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked) {
                    mPassowrdEditText?.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
                } else {
                    mPassowrdEditText?.setTransformationMethod(PasswordTransformationMethod.getInstance())
                }
            }
        })
    }



    override fun onPause() {
//        if(!isGoingSmallScreenToFullScreen)
//            videoViewBuilder?.pauseVideo()
        super.onPause()
    }

    override fun onResume() {
        isGoingSmallScreenToFullScreen = false
        super.onResume()
    }

    fun setConfig(view: View) {
        videoViewBuilder?.stopVideo()
        setConfig()
    }

    private fun setConfig() {
        // set config object
        val config = WCSDKConfig(
            applicationContext, // app context
            "",
            "",
            "",
            ""
        )

        // now init sdk with config object
        wexerSdk = WCSDK.initialize(config)

        // enable debug log
        wexerSdk.enableDebugLog(true)

        // set api header language
        //wexerSdk.setLanguageForRestApi("en-UK")

        if (wexerSdk != null)
            showToastMsg("SDK init successfully")
        else
            showToastMsg("SDK initialisation  failed")
    }

    fun signIn(view: View) {
        videoViewBuilder?.stopVideo()

        val userName: String = mUsernameEditText?.getText().toString().trim()
        if (TextUtils.isEmpty(userName)) {
            showToastMsg("Please enter Username")
            return
        }

        /*val password: String = mPassowrdEditText?.getText().toString().trim()
        if (TextUtils.isEmpty(password)) {
            showToastMsg("Please enter password")
            return
        }*/

        showProgress()

        wexerSdk.startSession(userName, object : StartSessionListener {
            override fun onError(error: SdkError) {
                hideProgress()
                showToastMsg(error.message)
            }

            override fun onSuccess(msg: String, mWCSDKUser: WCSDKUser?) {
                hideProgress()
                showToastMsg(msg)
            }
        })
    }

    fun ondemandPerform(view: View) {
        videoViewBuilder?.stopVideo()
        val classTag: String = mClasstag?.getText().toString().trim()
        if (TextUtils.isEmpty(classTag)) {
            showToastMsg("Please enter class id")
            return
        }


        // 1. subscribe
        // 3. now play

        showProgress()

        val userSubscription = WCSDKUserSubscriptionRequest("2019-01-02T06:20:49.000", "monthly")

        wexerSdk.activateSubscription(userSubscription, object : SubscriptionActivatedListener {
            override fun onError(error: SdkError) {
                hideProgress()
                showToastMsg(error.message)

            }

            override fun onSuccess(msg: String) {
                playPerform()
            }
        })

    }

    private var onDemandClass: WCSDKOnDemandClass? = null
    private fun playPerform() {

        val classTag = mClasstag?.getText().toString().trim()

        wexerSdk.performOnDemandContent(classTag, object : OnDemandPerformListener {

            override fun onError(error: SdkError) {
                showToastMsg(error.message)
                hideProgress()
            }

            override fun onSuccess(msg: String, mWCSDKOnDemandClasses: List<WCSDKOnDemandClass>?) {
                //showToastMsg("${response}")
                onDemandClass = mWCSDKOnDemandClasses?.get(0)
                hideProgress()
            }

        })
    }

    fun getSignupConsent(view: View) {
        showToastMsg("It is under development")
    }

    fun getClasses(view: View) {
        showProgress()

        val pageSizeText: Int = pageSizeEt?.getText().toString().trim().toInt()
        val pageNumberText: Int = pageNumberEt?.getText().toString().trim().toInt()

        val filterBy: String = filterBy?.getText().toString().trim()
        val orderBy: String = orderByEt?.getText().toString().trim()

        // tag 50122
        wexerSdk.getOnDemandClasses(
            pageSizeText,
            pageNumberText,
            filterBy,
            orderBy,
            object : ClassDataFetchListener {
                override fun onError(error: SdkError) {
                    hideProgress()
                    showToastMsg(error.message)
                }

                override fun onSuccess(
                    msg: String,
                    mOnDemandClassResponse: OnDemandClassResponse?
                ) {
                    hideProgress()
                    showToastMsg(msg)
                }

            })
    }

    fun searchOndemand(view: View) {
        val url =
            "https://cflondemandcontentprod.blob.core.windows.net/asset-cacc6cb6-52a3-47d4-bec8-7bb6d846b420/WE_01_Yoga_Spirits_OV_wexer_prof_1280x720_3400.mp4?sv=2015-07-08&sr=c&si=93c9f30f-866d-4815-8e32-713fef4ee0b3&sig=jA4Nicrc%2FnvME%2FvjMzcyK%2FQarNOUN%2BEeZ8SoUX7KXto%3D&se=2028-10-19T05%3A45%3A10Z"

        val urlStream =
            "https://0fcb8e4c4b5b4e069c231a71b0ff5f12.azureedge.net/b3db8bcd-224d-4a36-963e-70405b236724/WE_01_Yoga_Spirits_OV_wexer_prof.ism/manifest(format=m3u8-aapl)"
        val urlStream1 =
            "https://0fcb8e4c4b5b4e069c231a71b0ff5f12.azureedge.net/59d2a981-f469-4ff5-9651-3e6cffc12c34/PF_26_Aerobics_OV_1_wexer_profil.ism/manifest(format=m3u8-aapl)"
        val urlStream2 =
            "https://0fcb8e4c4b5b4e069c231a71b0ff5f12.azureedge.net/ddb69f0a-679a-495b-9bb8-0eb83fee9a87/MV_YOG30_GB019_YOGA_BASICS_CULTI.ism/manifest(format=m3u8-aapl)"

        showProgress()

        val searchEt: String = searchEt?.getText().toString().trim()

        val mOnDemandSearch = WCSDKOnDemandFilterRequest()
        searchEt.let {
            mOnDemandSearch.query = it
        }
        mOnDemandSearch.take = 200
        mOnDemandSearch.skip = 0
        //mOnDemandSearch.type = "Cycling,Weight Loss"
        //mOnDemandSearch.provider = "L1FT"
        //mOnDemandSearch.classLanguage = "L1FT"
        //mOnDemandSearch.duration = "1,109"
        //mOnDemandSearch.intensity = "1,10"
        //mOnDemandSearch.keywords = "hi"
        mOnDemandSearch.level = ExerciseLevel.Advanced // beginner intermediate advanced

        wexerSdk.getOnDemandClassesForCriteria(
            mOnDemandSearch,
            object : OnDemandMetadataSearchListener {
                override fun onError(error: SdkError) {
                    hideProgress()
                    showToastMsg(error.message)
                }

                override fun onSuccess(
                    msg: String,
                    mWCSDKOnDemandSearchResult: WCSDKOnDemandSearchResult?
                ) {
                    hideProgress()
                    showToastMsg(msg)
                }

            })
    }

    fun cancelSubscription(view: View) {
        //showToastMsg("Coming soon...")
        //convertDate()
        showProgress()
        wexerSdk.cancelSubscription(object : SubscriptionCanceledListener {
            override fun onError(error: SdkError) {
                hideProgress()
                showToastMsg(error.message)
            }

            override fun onSuccess(msg: String) {
                hideProgress()
                showToastMsg(msg)
            }
        })
    }

    fun activateSubscription(view: View) {
        //showToastMsg("Coming soon...")
        showProgress()

        val userSubscription = WCSDKUserSubscriptionRequest("2019-01-02T06:20:49.000", "monthly")

        wexerSdk.activateSubscription(userSubscription, object : SubscriptionActivatedListener {
            override fun onError(error: SdkError) {
                hideProgress()
                showToastMsg(error.message)
            }

            override fun onSuccess(msg: String) {
                hideProgress()
                showToastMsg(msg)
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun convertDate() {

        val strDate = "02-1-2018 06:07:59"

        val simpleDate = SimpleDateFormat("dd-M-yyyy hh:mm:ss")

        var date = Date()

        date = simpleDate.parse(strDate)

        val newSimpleDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")

        val ab = newSimpleDate.format(date)

        println("============ converted date : $ab")
    }

    fun getTenantCollection(view: View) {

        val collectionId: String = collectionIdEt?.getText().toString().trim()

        showProgress()
        wexerSdk.getOnDemandCollections(collectionId, 16, object : OnDemandCollectionListener {
            override fun onError(error: SdkError) {
                hideProgress()
                showToastMsg(error.message)
            }

            override fun onSuccess(
                msg: String,
                mWCSDKOnDemandCollections: List<WCSDKOnDemandCollection>?
            ) {
                hideProgress()
                showToastMsg(msg)
            }
        })
    }

    fun getMetadata(view: View) {
        showProgress()
        wexerSdk.getOnDemandMetadata(object : OnDemandMetadataListener {
            override fun onError(error: SdkError) {
                hideProgress()
                showToastMsg(error.message)
            }

            override fun onSuccess(
                msg: String,
                mWCSDKOnDemandMetaData: WCSDKOnDemandMetaData?
            ) {
                hideProgress()
                showToastMsg(msg)
            }
        })
    }

    fun ondemandClassdetails(view: View) {

        val classTag: String = classtagDetEt?.getText().toString().trim()
        if (TextUtils.isEmpty(classTag)) {
            showToastMsg("Please enter class id")
            return
        }
        showProgress()

        wexerSdk.getOndemandClassDetails(classTag, object : OnDemandClassDetailsListener {
            override fun onError(error: SdkError) {
                showToastMsg(error.message)
                hideProgress()
            }

            override fun onSuccess(msg: String, mOnDemandClassResponse: OnDemandClassResponse?) {

                showToastMsg(msg)
                hideProgress()
            }

        })
    }
    private fun getFormattedTimeInString(time: Long?) = Utils.getStringForTime(
        formatBuilder,
        formatter,
        time ?: 0L
    )

    private val formatBuilder = StringBuilder()
    private val formatter = Formatter(formatBuilder, Locale.getDefault())


    private fun startVideoView(){
        if(onDemandClass == null) return

        videoViewBuilder = wexerSdk.getVideoViewBuilder()

        videoViewBuilder?.setOnDemandClass(onDemandClass!!, mVideoPlayerStateListener)
        videoViewBuilder?.useCustomOverlayView(true)

        videoViewBuilder?.setCustomOverlayView(R.layout.client_playback_overlay_view, mSmallScreenOverlayViewProvider, mFullScreenOverlayViewProvider)

        videoViewBuilder?.setDefaultPlayButton(drawableResId = R.drawable.ic_default_play, colorTint = R.color.red)
        videoViewBuilder?.setDefaultPauseButton(R.drawable.ic_default_pause/*, R.color.green*/)
        videoViewBuilder?.setDefaultFullScreenOpenButton(drawableResId = R.drawable.ic_default_open_fullscreen, colorTint = R.color.green)
        videoViewBuilder?.setDefaultFullScreenCloseButton(drawableResId = R.drawable.ic_default_close_fullscreen, colorTint = R.color.red)
        videoViewBuilder?.setDefaultStopButton(/*drawableResId = R.drawable.ic_default_stop,*/ colorTint = R.color.light_green)
        videoViewBuilder?.setDefaultSeekBarProgressColorTint(colorTint = R.color.red)
        videoViewBuilder?.setDefaultSeekBarThumbColorTint(colorTint = R.color.red)

        videoViewBuilder?.startVideoIn(wexerVideoView) // to start small screen video..

    }

    val mSmallScreenOverlayViewProvider = object: OverlayViewProvider{
        override fun getOverlayView(view: View) {
            setSmallScreenOverlayViewControls(view)
        }
    }

    val mFullScreenOverlayViewProvider = object: OverlayViewProvider{
        override fun getOverlayView(view: View) {
            setFullScreenOverlayViewControls(view)
        }
    }

    val mVideoPlayerStateListener = object: VideoPlayerStateListener{

        override fun playerStatus(status: Int) {
            if(status == PLAYER_STATE_PLAYING){
                Log.e("MainActivity", "playerState:PLAYER_STATE_PLAYING")
            }

            if(status == PLAYER_STATE_PAUSED){
                Log.e("MainActivity", "playerState:PLAYER_STATE_PAUSED")
            }
        }

        override fun playerExit(duration: Long?) {
        }

        override fun currentVideoPosition(currentVideoPosition: Long) {
            customSmallScreenPosition?.text = "${getFormattedTimeInString(currentVideoPosition)}"
            customFullScreenPosition?.text = "${getFormattedTimeInString(currentVideoPosition)}"
            customSmallScreenVideoSeekBar?.progress = currentVideoPosition?.toInt()
            customFullScreenVideoSeekBar?.progress = currentVideoPosition?.toInt()
        }

        override fun onViewTouchEvent() {
            videoViewBuilder?.showOverlayView()
        }

        override fun totalVideoDuration(totalVideoDuration: Long) {
            customSmallScreenDuration?.text = "${getFormattedTimeInString(totalVideoDuration)}"
            customFullScreenDuration?.text = "${getFormattedTimeInString(totalVideoDuration)}"
            customSmallScreenVideoSeekBar?.max = totalVideoDuration.toInt()
            customFullScreenVideoSeekBar?.max = totalVideoDuration.toInt()
        }
    }

    private var customSmallScreenPosition: TextView? = null
    private var customSmallScreenDuration: TextView? = null
    private var customSmallScreenVideoSeekBar: SeekBar? = null

    private fun setSmallScreenOverlayViewControls(overlayView: View?){
        val customClose = overlayView?.findViewById<ImageButton>(R.id.customClose)
        val customPlay = overlayView?.findViewById<ImageButton>(R.id.customPlay)
        val customPause = overlayView?.findViewById<ImageButton>(R.id.customPause)
        val customToggleFullScreen = overlayView?.findViewById<ImageButton>(R.id.customToggleFullScreen)
        var title = overlayView?.findViewById<TextView>(R.id.title)
        customSmallScreenPosition = overlayView?.findViewById<TextView>(R.id.customPosition)
        customSmallScreenDuration = overlayView?.findViewById<TextView>(R.id.customDuration)
        customSmallScreenVideoSeekBar = overlayView?.findViewById<SeekBar>(R.id.customVideoSeekBar)

        title?.text = onDemandClass?.className?:""
        customPlay?.setOnClickListener{
            videoViewBuilder?.playVideo()
            videoViewBuilder?.hideOverlayView()
        }
        customPause?.setOnClickListener{
            videoViewBuilder?.pauseVideo()
            videoViewBuilder?.showOverlayView()
        }
        customClose?.setOnClickListener{
            videoViewBuilder?.stopVideo()
        }
        customToggleFullScreen?.setOnClickListener{
            isGoingSmallScreenToFullScreen = true
            videoViewBuilder?.openFullScreen()
        }
        customSmallScreenVideoSeekBar?.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                videoViewBuilder?.seekTo(seekBar?.progress?.toLong()?:0L)
            }
        })

    }

    private var isGoingSmallScreenToFullScreen = false
    private var customFullScreenPosition: TextView? = null
    private var customFullScreenDuration: TextView? = null
    private var customFullScreenVideoSeekBar: SeekBar? = null

    private fun setFullScreenOverlayViewControls(overlayView: View){
        val customClose = overlayView.findViewById<ImageButton>(R.id.customClose)
        val customPlay = overlayView.findViewById<ImageButton>(R.id.customPlay)
        val customPause = overlayView.findViewById<ImageButton>(R.id.customPause)
        val customToggleFullScreen = overlayView.findViewById<ImageButton>(R.id.customToggleFullScreen)
        var title = overlayView.findViewById<TextView>(R.id.title)
        customFullScreenPosition = overlayView.findViewById<TextView>(R.id.customPosition)
        customFullScreenDuration = overlayView.findViewById<TextView>(R.id.customDuration)
        customFullScreenVideoSeekBar = overlayView.findViewById<SeekBar>(R.id.customVideoSeekBar)

        title.text = onDemandClass?.className?:""
        customPlay?.setOnClickListener{
            videoViewBuilder?.playVideo()
            videoViewBuilder?.hideOverlayView()
        }
        customPause?.setOnClickListener{
            videoViewBuilder?.pauseVideo()
            videoViewBuilder?.showOverlayView()
        }
        customClose?.setOnClickListener{
            videoViewBuilder?.stopVideo()
        }
        customToggleFullScreen?.background = ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_close)
        customToggleFullScreen?.setOnClickListener{
            videoViewBuilder?.closeFullScreen()
        }
        customFullScreenVideoSeekBar?.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            private var isFirstTime = true
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                videoViewBuilder?.seekTo(seekBar?.progress?.toLong()?:0L)
            }

        })
    }

    fun onSmallViewClientOverlay(v: View){
        isGoingSmallScreenToFullScreen = false
        videoViewBuilder?.stopVideo()
        if(onDemandClass == null) return

        videoViewBuilder = wexerSdk.getVideoViewBuilder()

        videoViewBuilder?.setOnDemandClass(onDemandClass!!, mVideoPlayerStateListener)
        videoViewBuilder?.useCustomOverlayView(true)
        videoViewBuilder?.setCustomOverlayView(R.layout.client_playback_overlay_view, mSmallScreenOverlayViewProvider, mFullScreenOverlayViewProvider)

        videoViewBuilder?.startVideoIn(wexerVideoView) // to start small screen video..
    }

    fun onSmallViewDefaultOverlay(v: View){
        isGoingSmallScreenToFullScreen = false
        videoViewBuilder?.stopVideo()
        wexerVideoView?.invalidate()
        if(onDemandClass == null) return

        videoViewBuilder = wexerSdk.getVideoViewBuilder()

        videoViewBuilder?.setOnDemandClass(onDemandClass!!, mVideoPlayerStateListener)
        videoViewBuilder?.useCustomOverlayView(false)

        videoViewBuilder?.startVideoIn(wexerVideoView) // to start small screen video..
    }

    fun onSmallViewDefaultOverlayCustomTheme(v: View){
        isGoingSmallScreenToFullScreen = false
        videoViewBuilder?.stopVideo()
        wexerVideoView?.invalidate()
        if(onDemandClass == null) return

        videoViewBuilder = wexerSdk.getVideoViewBuilder()

        videoViewBuilder?.setOnDemandClass(onDemandClass!!, mVideoPlayerStateListener)
        videoViewBuilder?.useCustomOverlayView(false)
        videoViewBuilder?.setDefaultPlayButton(drawableResId = R.drawable.ic_default_play, colorTint = R.color.red)
        videoViewBuilder?.setDefaultPauseButton(R.drawable.ic_default_pause/*, R.color.green*/)
        videoViewBuilder?.setDefaultFullScreenOpenButton(drawableResId = R.drawable.ic_default_open_fullscreen, colorTint = R.color.green)
        videoViewBuilder?.setDefaultFullScreenCloseButton(drawableResId = R.drawable.ic_default_close_fullscreen, colorTint = R.color.red)
        videoViewBuilder?.setDefaultStopButton(/*drawableResId = R.drawable.ic_default_stop,*/ colorTint = R.color.light_green)
        videoViewBuilder?.setDefaultSeekBarProgressColorTint(colorTint = R.color.red)
        videoViewBuilder?.setDefaultSeekBarThumbColorTint(colorTint = R.color.red)

        videoViewBuilder?.startVideoIn(wexerVideoView) // to start small screen video..

    }

    fun onFSViewClientOverlay(v: View){
        isGoingSmallScreenToFullScreen = false
        videoViewBuilder?.stopVideo()
        wexerVideoView?.invalidate()
        if(onDemandClass == null) return

        videoViewBuilder = wexerSdk.getVideoViewBuilder()

        videoViewBuilder?.setOnDemandClass(onDemandClass!!, mVideoPlayerStateListener)
        videoViewBuilder?.useCustomOverlayView(true)
        videoViewBuilder?.setCustomOverlayView(R.layout.client_playback_overlay_view, mSmallScreenOverlayViewProvider, mFullScreenOverlayViewProvider)
        videoViewBuilder?.openFullScreen() //can be used to start individual full screen mode or take video from small to fullscreen
    }

    fun onFSViewDefaultOverlay(v: View){
        isGoingSmallScreenToFullScreen = false
        videoViewBuilder?.stopVideo()
        wexerVideoView?.invalidate()
        if(onDemandClass == null) return

        videoViewBuilder = wexerSdk.getVideoViewBuilder()

        videoViewBuilder?.setOnDemandClass(onDemandClass!!, mVideoPlayerStateListener)
        videoViewBuilder?.useCustomOverlayView(false)
        videoViewBuilder?.openFullScreen() //can be used to start individual full screen mode or take video from small to fullscreen
    }

    fun onFSViewDefaultOverlayCustomTheme(v: View){
        isGoingSmallScreenToFullScreen = false
        videoViewBuilder?.stopVideo()
        wexerVideoView?.invalidate()
        if(onDemandClass == null) return

        videoViewBuilder = wexerSdk.getVideoViewBuilder()

        videoViewBuilder?.setOnDemandClass(onDemandClass!!, mVideoPlayerStateListener)
        videoViewBuilder?.useCustomOverlayView(false)
        videoViewBuilder?.setDefaultPlayButton(drawableResId = R.`drawable`.ic_default_play, colorTint = R.color.red)
        videoViewBuilder?.setDefaultPauseButton(R.drawable.ic_default_pause/*, R.color.green*/)
        videoViewBuilder?.setDefaultFullScreenOpenButton(drawableResId = R.drawable.ic_default_open_fullscreen, colorTint = R.color.green)
        videoViewBuilder?.setDefaultFullScreenCloseButton(drawableResId = R.drawable.ic_default_close_fullscreen, colorTint = R.color.red)
        videoViewBuilder?.setDefaultStopButton(/*drawableResId = R.drawable.ic_default_stop,*/ colorTint = R.color.light_green)
        videoViewBuilder?.setDefaultSeekBarProgressColorTint(colorTint = R.color.red)
        videoViewBuilder?.setDefaultSeekBarThumbColorTint(colorTint = R.color.red)

        videoViewBuilder?.openFullScreen() //can be used to start individual full screen mode or take video from small to fullscreen
    }
}