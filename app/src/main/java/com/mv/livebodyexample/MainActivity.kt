/**
 * MainActivity class that handles the main functionality of the application.
 * This class is responsible for initializing the camera, handling permissions,
 * and managing the detection engine.
 */
@ObsoleteCoroutinesApi
class MainActivity : AppCompatActivity(), SetThresholdDialogFragment.ThresholdDialogListener {

    /**
     * Binding object for the activity_main layout.
     */
    private lateinit var binding: ActivityMainBinding

    /**
     * Flag to check if the engine is prepared.
     */
    private var enginePrepared: Boolean = false

    /**
     * Wrapper for the detection engine.
     */
    private lateinit var engineWrapper: EngineWrapper

    /**
     * Threshold value for detection.
     */
    private var threshold: Float = defaultThreshold

    /**
     * Camera object for capturing preview frames.
     */
    private var camera: Camera? = null

    /**
     * ID of the camera to be used (front-facing by default).
     */
    private var cameraId: Int = Camera.CameraInfo.CAMERA_FACING_FRONT

    /**
     * Width of the camera preview.
     */
    private val previewWidth: Int = 640

    /**
     * Height of the camera preview.
     */
    private val previewHeight: Int = 480

    /**
     * Orientation of the frame.
     */
    private val frameOrientation: Int = 7

    /**
     * Width of the screen.
     */
    private var screenWidth: Int = 0

    /**
     * Height of the screen.
     */
    private var screenHeight: Int = 0

    /**
     * Factor for scaling X coordinates.
     */
    private var factorX: Float = 0F

    /**
     * Factor for scaling Y coordinates.
     */
    private var factorY: Float = 0F

    /**
     * Coroutine context for detection.
     */
    private val detectionContext = newSingleThreadContext("detection")

    /**
     * Flag to check if detection is in progress.
     */
    private var working: Boolean = false

    /**
     * Animator for scaling the scan view.
     */
    private lateinit var scaleAnimator: ObjectAnimator

    /**
     * Called when the activity is created. Initializes the activity and requests permissions if needed.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (hasPermissions()) {
            init()
        } else {
            requestPermission()
        }
    }

    /**
     * Checks if the required permissions are granted.
     * @return True if all permissions are granted, false otherwise.
     */
    private fun hasPermissions(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }

    /**
     * Requests the necessary permissions.
     */
    @TargetApi(Build.VERSION_CODES.M)
    private fun requestPermission() = requestPermissions(permissions, permissionReqCode)

    /**
     * Initializes the activity, sets up the camera and detection engine.
     */
    private fun init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.result = DetectionResult()

        calculateSize()

        binding.surface.holder.let {
            it.setFormat(ImageFormat.NV21)
            it.addCallback(object : SurfaceHolder.Callback, Camera.PreviewCallback {
                override fun surfaceChanged(
                    holder: SurfaceHolder?,
                    format: Int,
                    width: Int,
                    height: Int
                ) {
                    if (holder?.surface == null) return

                    if (camera == null) return

                    try {
                        camera?.stopPreview()
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }

                    val parameters = camera?.parameters
                    parameters?.setPreviewSize(previewWidth, previewHeight)

                    factorX = screenWidth / previewHeight.toFloat()
                    factorY = screenHeight / previewWidth.toFloat()

                    camera?.parameters = parameters

                    camera?.startPreview()
                    camera?.setPreviewCallback(this)

                    setCameraDisplayOrientation()
                }

                override fun surfaceDestroyed(holder: SurfaceHolder?) {
                    camera?.setPreviewCallback(null)
                    camera?.release()
                    camera = null
                }

                override fun surfaceCreated(holder: SurfaceHolder?) {
                    try {
                        camera = Camera.open(cameraId)
                    } catch (e: Exception) {
                        cameraId = Camera.CameraInfo.CAMERA_FACING_BACK
                        camera = Camera.open(cameraId)
                    }

                    try {
                        camera!!.setPreviewDisplay(binding.surface.holder)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

                override fun onPreviewFrame(data: ByteArray?, camera: Camera?) {
                    if (enginePrepared && data != null) {
                        if (!working) {
                            GlobalScope.launch(detectionContext) {
                                working = true
                                val result = engineWrapper.detect(
                                    data,
                                    previewWidth,
                                    previewHeight,
                                    frameOrientation
                                )
                                result.threshold = threshold

                                val rect = calculateBoxLocationOnScreen(
                                    result.left,
                                    result.top,
                                    result.right,
                                    result.bottom
                                )

                                binding.result = result.updateLocation(rect)

                                Log.d(
                                    tag,
                                    "threshold:${result.threshold}, confidence: ${result.confidence}"
                                )

                                binding.rectView.postInvalidate()
                                working = false
                            }
                        }
                    }
                }
            })
        }

        scaleAnimator = ObjectAnimator.ofFloat(binding.scan, View.SCALE_Y, 1F, -1F, 1F).apply {
            this.duration = 3000
            this.repeatCount = ValueAnimator.INFINITE
            this.repeatMode = ValueAnimator.REVERSE
            this.interpolator = LinearInterpolator()
            this.start()
        }
    }

    /**
     * Calculates the screen size and sets the screenWidth and screenHeight variables.
     */
    private fun calculateSize() {
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        screenWidth = dm.widthPixels
        screenHeight = dm.heightPixels
    }

    /**
     * Calculates the location of the detection box on the screen.
     * @param left Left coordinate of the detection box.
     * @param top Top coordinate of the detection box.
     * @param right Right coordinate of the detection box.
     * @param bottom Bottom coordinate of the detection box.
     * @return Rect object representing the location of the detection box on the screen.
     */
    private fun calculateBoxLocationOnScreen(left: Int, top: Int, right: Int, bottom: Int): Rect =
        Rect(
            (left * factorX).toInt(),
            (top * factorY).toInt(),
            (right * factorX).toInt(),
            (bottom * factorY).toInt()
        )

    /**
     * Sets the display orientation of the camera based on the device rotation.
     */
    private fun setCameraDisplayOrientation() {
        val info = Camera.CameraInfo()
        Camera.getCameraInfo(cameraId, info)
        val rotation = windowManager.defaultDisplay.rotation
        var degrees = 0
        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }
        var result: Int
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360
            result = (360 - result) % 360 // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360
        }
        camera!!.setDisplayOrientation(result)
    }

    /**
     * Opens the threshold setting dialog.
     * @param view The view that triggered this function.
     */
    fun setting(@Suppress("UNUSED_PARAMETER") view: View) =
        SetThresholdDialogFragment().show(supportFragmentManager, "dialog")

    /**
     * Called when the positive button in the threshold dialog is clicked.
     * @param t The new threshold value.
     */
    override fun onDialogPositiveClick(t: Float) {
        threshold = t
    }

    /**
     * Called when the permissions request result is received.
     * @param requestCode The request code.
     * @param permissions The requested permissions.
     * @param grantResults The grant results for the corresponding permissions.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionReqCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                init()
            } else {
                Toast.makeText(this, "请授权相机权限", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Called when the activity is resumed. Initializes the detection engine.
     */
    override fun onResume() {
        engineWrapper = EngineWrapper(assets)
        enginePrepared = engineWrapper.init()

        if (!enginePrepared) {
            Toast.makeText(this, "Engine init failed.", Toast.LENGTH_LONG).show()
        }

        super.onResume()
    }

    /**
     * Called when the activity is destroyed. Cleans up resources.
     */
    override fun onDestroy() {
        engineWrapper.destroy()
        scaleAnimator.cancel()
        super.onDestroy()
    }

    companion object {
        const val tag = "MainActivity"
        const val defaultThreshold = 0.915F

        val permissions: Array<String> = arrayOf(Manifest.permission.CAMERA)
        const val permissionReqCode = 1
    }

}
