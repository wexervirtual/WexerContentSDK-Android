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
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.time.LocalDate
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

    var lv_signIn  : LinearLayout? = null
    var lv_getclasses : LinearLayout? = null
    var lv_serchondemand : LinearLayout? = null
    var btn_getconsent : Button? = null
    var lv_getclassdetail : LinearLayout? = null
    var lv_playvideo : LinearLayout? = null
    var btn_acceptconsent : Button? = null
    lateinit var wexerSdk: ISdkInstance
    var lv_playoverlay : LinearLayout? = null
   // var lv_voverlay : LinearLayout? = null
    var lv_getcollections : LinearLayout? = null
    var btn_metadata : Button? = null
    var tv_listoffunctions : TextView? = null

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

        lv_signIn = findViewById(R.id.lv_signIn)
        lv_getclasses =findViewById(R.id.lv_getclasses)
        lv_getclassdetail = findViewById(R.id.lv_getclassdetail)
        lv_playvideo = findViewById(R.id.lv_playvideo)
        btn_acceptconsent = findViewById(R.id.btn_acceptconsent)
        lv_playoverlay = findViewById(R.id.lv_playoverlay)
       // lv_voverlay = findViewById(R.id.lv_voverlay)
        btn_getconsent = findViewById(R.id.btn_getconsent)
        lv_serchondemand = findViewById(R.id.lv_searchOndemand)
        lv_getcollections = findViewById(R.id.lv_getcollections)
        btn_metadata  = findViewById(R.id.btn_getmetadata)
        tv_listoffunctions = findViewById(R.id.tv_listoffunctions)

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

    fun hideView(){

        lv_playvideo?.visibility = View.GONE
        btn_acceptconsent?.visibility =View.GONE
        lv_getclasses?.visibility = View.GONE
        lv_getclassdetail?.visibility = View.GONE
        lv_serchondemand?.visibility = View.GONE
        btn_getconsent?.visibility = View.GONE
        lv_getcollections?.visibility = View.GONE
        btn_metadata?.visibility = View.GONE
        tv_listoffunctions?.visibility =View.GONE
    }
    fun setConfig(view: View) {
        lv_signIn?.visibility = View.GONE
        hideView()
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
            "")

        // now init sdk with config object
        wexerSdk = WCSDK.initialize(config)

        // enable debug log
        wexerSdk.enableDebugLog(true)

        // set api header language
        //wexerSdk.setLanguageForRestApi("en-UK")

        if (wexerSdk != null) {
            showToastMsg("SDK init successfully")
            tv_listoffunctions?.visibility = View.VISIBLE
            lv_signIn?.visibility =View.VISIBLE
        }
        else {
            lv_signIn?.visibility =View.GONE
            showToastMsg("SDK initialisation  failed")
        }
    }

    fun signIn(view: View) {
        videoViewBuilder?.stopVideo()
        hideView()
        tv_listoffunctions?.visibility =View.VISIBLE



        val userName: String = mUsernameEditText?.getText().toString().trim()
        if (TextUtils.isEmpty(userName)) {
            showToastMsg("Please enter Username")
            return
        }

        showProgress()
        wexerSdk.startSession(userName, object : StartSessionListener {
            override fun onError(error: SdkError) {
                hideProgress()
                lv_getclasses?.visibility =View.GONE
                showToastMsg(error.message)
            }

            override fun onSuccess(msg: String, mWCSDKUser: WCSDKUser?) {
                hideProgress()
                lv_getclasses?.visibility =View.VISIBLE
                showToastMsg(msg)
            }
        })
    }


    private var onDemandClass: WCSDKOnDemandClass? = null
    lateinit var classTag :String
     fun playPerform(view: View) {

      //  val classTagTxt = mClasstag?.getText().toString().trim()
        // if (classTagTxt.isEmpty()==null){
          //   return}

        wexerSdk.performOnDemandContent(classTag, object : OnDemandPerformListener {

            override fun onError(error: SdkError) {
                showToastMsg(error.message)
                lv_playoverlay?.visibility = View.GONE
                hideProgress()
            }

            override fun onSuccess(msg: String, mWCSDKOnDemandClasses:WCSDKGetStreamingLinkResponse?) {
                //showToastMsg("${response}")

                onDemandClass?.streamingLink= mWCSDKOnDemandClasses?.results?.streamingUrl
                hideProgress()
                lv_playoverlay?.visibility = View.VISIBLE
              //  lv_voverlay?.visibility = View.VISIBLE
                startVideoView()
            }

        })
    }

    fun getSignupConsent(view: View) {
        showToastMsg("It is under development")
    }

    fun getClasses(view: View) {
        showProgress()

        val take: Int = pageSizeEt?.getText().toString().trim().toInt()
        val skip: Int = pageNumberEt?.getText().toString().trim().toInt()

        val sort: String = filterBy?.getText().toString().trim()
        val dir: String = orderByEt?.getText().toString().trim()

        // tag 50122
        wexerSdk.getOnDemandClasses(
            take,
            skip,
            sort,
            dir,
            object : ClassDataFetchListener {
                override fun onError(error: SdkError) {
                    hideProgress()
                    showToastMsg(error.message)
                    lv_getclassdetail?.visibility =View.GONE

                }

                override fun onSuccess(
                    msg: String,
                    mOnDemandClassResponse: WCSDKOnDemandSearchResult?
                ) {
                    classTag = mOnDemandClassResponse?.items?.get(0)?.classTag.toString()
                    val className = mOnDemandClassResponse?.items?.get(0)?.virtualClass?.className
                    mClasstag?.setText("$className")
                    classtagDetEt?.setText("$className")
                    onDemandClass = mOnDemandClassResponse?.items?.get(0)?.virtualClass
                    hideProgress()
                    lv_getclassdetail?.visibility =View.VISIBLE
                    lv_serchondemand?.visibility = View.VISIBLE
                    btn_getconsent?.visibility = View.VISIBLE
                    lv_getcollections?.visibility = View.VISIBLE
                    btn_metadata?.visibility = View.VISIBLE
                    showToastMsg(msg)
                }

            })
    }

    fun searchOndemand(view: View) {
        val url = "https://cdn.jwplayer.com/manifests/19MkdMGa.m3u8"
           // "https://cflondemandcontentprod.blob.core.windows.net/asset-cacc6cb6-52a3-47d4-bec8-7bb6d846b420/WE_01_Yoga_Spirits_OV_wexer_prof_1280x720_3400.mp4?sv=2015-07-08&sr=c&si=93c9f30f-866d-4815-8e32-713fef4ee0b3&sig=jA4Nicrc%2FnvME%2FvjMzcyK%2FQarNOUN%2BEeZ8SoUX7KXto%3D&se=2028-10-19T05%3A45%3A10Z"

        val urlStream =
            "https://0fcb8e4c4b5b4e069c231a71b0ff5f12.azureedge.net/b3db8bcd-224d-4a36-963e-70405b236724/WE_01_Yoga_Spirits_OV_wexer_prof.ism/manifest(format=m3u8-aapl)"
        val urlStream1 =
            "https://0fcb8e4c4b5b4e069c231a71b0ff5f12.azureedge.net/59d2a981-f469-4ff5-9651-3e6cffc12c34/PF_26_Aerobics_OV_1_wexer_profil.ism/manifest(format=m3u8-aapl)"
        val urlStream2 =
            "https://0fcb8e4c4b5b4e069c231a71b0ff5f12.azureedge.net/ddb69f0a-679a-495b-9bb8-0eb83fee9a87/MV_YOG30_GB019_YOGA_BASICS_CULTI.ism/manifest(format=m3u8-aapl)"

        showProgress()

        val searchEt: String = searchEt?.getText().toString().trim()

        val mOnDemandSearch = WCSDKOnDemandClassRequest()
        searchEt.let {
            mOnDemandSearch.query = it
        }
        mOnDemandSearch.take = 20
        mOnDemandSearch.skip = 0
        mOnDemandSearch.dir="desc"
        mOnDemandSearch.sort="scheduledate"
        mOnDemandSearch.categoryId="1001"
        mOnDemandSearch.query = "core"
        //mOnDemandSearch.type = "Cycling,Weight Loss"
        //mOnDemandSearch.provider = "L1FT"
        //mOnDemandSearch.classLanguage = "L1FT"
        //mOnDemandSearch.duration = "1,109"
        //mOnDemandSearch.intensity = "1,10"
        //mOnDemandSearch.keywords = "hi"
        //mOnDemandSearch.level = ExerciseLevel.Advanced // beginner intermediate advanced

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

        val collectionId: String = "eb112d95-fe7c-4949-9f63-ec5d37422ca5"//collectionIdEt?.getText().toString().trim()

        showProgress()
        wexerSdk.getOnDemandCollections(collectionId, object : OnDemandCollectionListener {
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

       // val classTag: String = classtagDetEt?.getText().toString().trim()
        if (TextUtils.isEmpty(classTag)) {
            showToastMsg("Please enter class id")
            return
        }
        showProgress()

        wexerSdk.getOndemandClassDetails(classTag, object : OnDemandClassDetailsListener {
            override fun onError(error: SdkError) {
                showToastMsg(error.message)
                hideProgress()
                lv_playoverlay?.visibility = View.GONE
                lv_playvideo?.visibility =View.GONE

            }

            override fun onSuccess(msg: String, mOnDemandClassResponse: WCSDKOnDemandClass?) {
                showToastMsg(msg)
                hideProgress()
                lv_playoverlay?.visibility = View.GONE
                lv_playvideo?.visibility =View.VISIBLE

            }

        })
    }
    lateinit var mConsentTag : String
     fun getConsent(view: View)
    {
        showProgress()
        wexerSdk.getConsent(object : GetConsentListener{
            override fun onError(error: SdkError) {
                hideProgress()
                showToastMsg(error.message)
                btn_acceptconsent?.visibility = View.GONE
            }

            override fun onSuccess(msg: String, mConsent: List<Consent>?) {

                showToastMsg(msg)
                mConsentTag = mConsent?.get(0)?.consentTag.toString()
                hideProgress()
                btn_acceptconsent?.visibility = View.VISIBLE

            }

        })
    }
    fun acceptConsent(view: View){
        showProgress()
      //  val mConsentTag ="aa8ad028-ef2d-408a-bcd6-ccd53437ca4c"
        wexerSdk.acceptConsent(
            mConsentTag,
            object : AcceptConsentListener {
                override fun onError(error: SdkError) {
                    hideProgress()
                    showToastMsg(error.message)

                }

                override fun onSuccess(
                    msg: String,
                    mWCSDKAcceptConsentResponse: List<WCSDKAcceptConsentResponse>
                ) {
                    hideProgress()
                    showToastMsg(msg)                }

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