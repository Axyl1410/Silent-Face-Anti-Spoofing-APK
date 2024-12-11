/**
 * FaceDetector is a class responsible for detecting faces in images.
 * It uses native methods to perform the detection.
 */
@Keep
class FaceDetector : Component() {

    /**
     * A handle to the native instance of the face detector.
     */
    @Keep
    private var nativeHandler: Long

    init {
        nativeHandler = createInstance()
    }

    /**
     * Creates an instance of the native face detector.
     * @return A handle to the native instance.
     */
    override fun createInstance(): Long = allocate()

    /**
     * Loads the face detection model from the assets.
     * @param assetsManager The AssetManager to load the model from.
     * @return An integer indicating the success or failure of the model loading.
     */
    fun loadModel(assetsManager: AssetManager): Int = nativeLoadModel(assetsManager)

    /**
     * Detects faces in a given bitmap.
     * @param bitmap The bitmap to detect faces in.
     * @return A list of FaceBox objects representing the detected faces.
     * @throws IllegalArgumentException if the bitmap config is not ARGB_8888.
     */
    fun detect(bitmap: Bitmap): List<FaceBox> = when (bitmap.config) {
        Bitmap.Config.ARGB_8888 -> nativeDetectBitmap(bitmap)
        else -> throw IllegalArgumentException("Invalid bitmap config value")
    }

    /**
     * Detects faces in a given YUV image.
     * @param yuv The YUV byte array to detect faces in.
     * @param previewWidth The width of the preview image.
     * @param previewHeight The height of the preview image.
     * @param orientation The orientation of the image.
     * @return A list of FaceBox objects representing the detected faces.
     * @throws IllegalArgumentException if the YUV data size is invalid.
     */
    fun detect(
        yuv: ByteArray,
        previewWidth: Int,
        previewHeight: Int,
        orientation: Int
    ): List<FaceBox> {
        if (previewWidth * previewHeight * 3 / 2 != yuv.size) {
            throw IllegalArgumentException("Invalid yuv data")
        }
        return nativeDetectYuv(yuv, previewWidth, previewHeight, orientation)
    }

    /**
     * Destroys the native instance of the face detector.
     */
    override fun destroy() = deallocate()

    //////////////////////////////// Native ////////////////////////////////////
    /**
     * Allocates a new native instance of the face detector.
     * @return A handle to the native instance.
     */
    @Keep
    private external fun allocate(): Long

    /**
     * Deallocates the native instance of the face detector.
     */
    @Keep
    private external fun deallocate()

    /**
     * Loads the face detection model from the assets using native code.
     * @param assetsManager The AssetManager to load the model from.
     * @return An integer indicating the success or failure of the model loading.
     */
    @Keep
    private external fun nativeLoadModel(assetsManager: AssetManager): Int

    /**
     * Detects faces in a given bitmap using native code.
     * @param bitmap The bitmap to detect faces in.
     * @return A list of FaceBox objects representing the detected faces.
     */
    @Keep
    private external fun nativeDetectBitmap(bitmap: Bitmap): List<FaceBox>

    /**
     * Detects faces in a given YUV image using native code.
     * @param yuv The YUV byte array to detect faces in.
     * @param previewWidth The width of the preview image.
     * @param previewHeight The height of the preview image.
     * @param orientation The orientation of the image.
     * @return A list of FaceBox objects representing the detected faces.
     */
    @Keep
    private external fun nativeDetectYuv(
        yuv: ByteArray,
        previewWidth: Int,
        previewHeight: Int,
        orientation: Int
    ): List<FaceBox>
}