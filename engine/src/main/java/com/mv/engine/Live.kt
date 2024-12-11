/**
 * Live class that extends Component and provides methods to load models and detect faces.
 */
class Live : Component() {

    /**
     * Native handler for the Live instance.
     */
    @Keep
    private var nativeHandler: Long

    init {
        nativeHandler = createInstance()
    }

    /**
     * Creates an instance of the native handler.
     * @return Long value representing the native handler.
     */
    override fun createInstance(): Long = allocate()

    /**
     * Destroys the native handler instance.
     */
    override fun destroy() {
        deallocate()
    }

    /**
     * Loads the model configuration from the assets.
     * @param assetManager AssetManager to access the assets.
     * @return Int value indicating the success or failure of the model loading.
     */
    fun loadModel(assetManager: AssetManager): Int {
        val configs = parseConfig(assetManager)

        if (configs.isEmpty()) {
            Log.e(tag, "parse model config failed")
            return -1
        }

        return nativeLoadModel(assetManager, configs)
    }

    /**
     * Detects faces in the provided YUV data.
     * @param yuv ByteArray containing the YUV data.
     * @param previewWidth Int value representing the width of the preview.
     * @param previewHeight Int value representing the height of the preview.
     * @param orientation Int value representing the orientation of the preview.
     * @param faceBox FaceBox object containing the coordinates of the face box.
     * @return Float value representing the detection result.
     * @throws IllegalArgumentException if the YUV data size is invalid.
     */
    fun detect(
        yuv: ByteArray,
        previewWidth: Int,
        previewHeight: Int,
        orientation: Int,
        faceBox: FaceBox
    ): Float {
        if (previewWidth * previewHeight * 3 / 2 != yuv.size) {
            throw IllegalArgumentException("Invalid yuv data")
        }

        return nativeDetectYuv(
            yuv,
            previewWidth,
            previewHeight,
            orientation,
            faceBox.left,
            faceBox.top,
            faceBox.right,
            faceBox.bottom
        )
    }

    /**
     * Parses the model configuration from the assets.
     * @param assetManager AssetManager to access the assets.
     * @return List of ModelConfig objects parsed from the configuration file.
     */
    private fun parseConfig(assetManager: AssetManager): List<ModelConfig> {
        val inputStream = assetManager.open("live/config.json")
        val br = BufferedReader(InputStreamReader(inputStream))
        val line = br.readLine()

        val jsonArray = JSONArray(line)

        val list = mutableListOf<ModelConfig>()
        for (i in 0 until jsonArray.length()) {
            val config: JSONObject = jsonArray.getJSONObject(i)
            ModelConfig().apply {
                name = config.optString("name")
                width = config.optInt("width")
                height = config.optInt("height")
                scale = config.optDouble("scale").toFloat()
                shift_x = config.optDouble("shift_x").toFloat()
                shift_y = config.optDouble("shift_y").toFloat()
                org_resize = config.optBoolean("org_resize")

                list.add(this)
            }
        }
        return list
    }

    companion object {
        /**
         * Tag for logging.
         */
        const val tag = "Live"
    }

    ///////////////////////////////////// Native ////////////////////////////////////
    /**
     * Allocates the native handler.
     * @return Long value representing the allocated native handler.
     */
    @Keep
    private external fun allocate(): Long

    /**
     * Deallocates the native handler.
     */
    @Keep
    private external fun deallocate()

    /**
     * Loads the model configuration in the native layer.
     * @param assetManager AssetManager to access the assets.
     * @param configs List of ModelConfig objects to be loaded.
     * @return Int value indicating the success or failure of the model loading.
     */
    @Keep
    private external fun nativeLoadModel(
        assetManager: AssetManager,
        configs: List<ModelConfig>
    ): Int

    /**
     * Detects faces in the provided YUV data in the native layer.
     * @param yuv ByteArray containing the YUV data.
     * @param previewWidth Int value representing the width of the preview.
     * @param previewHeight Int value representing the height of the preview.
     * @param orientation Int value representing the orientation of the preview.
     * @param left Int value representing the left coordinate of the face box.
     * @param top Int value representing the top coordinate of the face box.
     * @param right Int value representing the right coordinate of the face box.
     * @param bottom Int value representing the bottom coordinate of the face box.
     * @return Float value representing the detection result.
     */
    @Keep
    private external fun nativeDetectYuv(
        yuv: ByteArray,
        previewWidth: Int,
        previewHeight: Int,
        orientation: Int,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ): Float
}