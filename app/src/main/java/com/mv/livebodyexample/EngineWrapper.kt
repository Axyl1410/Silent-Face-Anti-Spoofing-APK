package com.mv.livebodyexample

import android.content.res.AssetManager
import com.mv.engine.FaceBox
import com.mv.engine.FaceDetector
import com.mv.engine.Live

/**
 * EngineWrapper class that wraps the functionality of FaceDetector and Live classes.
 *
 * @property assetManager AssetManager instance to load models.
 * @constructor Creates an instance of EngineWrapper with the provided AssetManager.
 */
class EngineWrapper(private var assetManager: AssetManager) {

    private var faceDetector: FaceDetector = FaceDetector()
    private var live: Live = Live()

    /**
     * Initializes the face detection and live detection models.
     *
     * @return Boolean indicating whether both models were successfully loaded.
     */
    fun init(): Boolean {
        var ret = faceDetector.loadModel(assetManager)
        if (ret == 0) {
            ret = live.loadModel(assetManager)
            return ret == 0
        }

        return false
    }

    /**
     * Destroys the face detection and live detection models.
     */
    fun destroy() {
        faceDetector.destroy()
        live.destroy()
    }

    /**
     * Detects faces and performs live detection on the first detected face.
     *
     * @param yuv ByteArray containing the YUV image data.
     * @param width Int representing the width of the image.
     * @param height Int representing the height of the image.
     * @param orientation Int representing the orientation of the image.
     * @return DetectionResult containing the detected face box, detection time, and success status.
     */
    fun detect(yuv: ByteArray, width: Int, height: Int, orientation: Int): DetectionResult {
        val boxes = detectFace(yuv, width, height, orientation)
        if (boxes.isNotEmpty()) {
            val begin = System.currentTimeMillis()
            val box = boxes[0].apply {
                val c = detectLive(yuv, width, height, orientation, this)
                confidence = c
            }
            val end = System.currentTimeMillis()
            return DetectionResult(box, end - begin, true)
        }

        return DetectionResult()
    }

    /**
     * Detects faces in the provided YUV image data.
     *
     * @param yuv ByteArray containing the YUV image data.
     * @param width Int representing the width of the image.
     * @param height Int representing the height of the image.
     * @param orientation Int representing the orientation of the image.
     * @return List of FaceBox containing detected face boxes.
     */
    private fun detectFace(
        yuv: ByteArray,
        width: Int,
        height: Int,
        orientation: Int
    ): List<FaceBox> = faceDetector.detect(yuv, width, height, orientation)

    /**
     * Performs live detection on the provided face box in the YUV image data.
     *
     * @param yuv ByteArray containing the YUV image data.
     * @param width Int representing the width of the image.
     * @param height Int representing the height of the image.
     * @param orientation Int representing the orientation of the image.
     * @param faceBox FaceBox representing the detected face box.
     * @return Float representing the confidence score of the live detection.
     */
    private fun detectLive(
        yuv: ByteArray,
        width: Int,
        height: Int,
        orientation: Int,
        faceBox: FaceBox
    ): Float = live.detect(yuv, width, height, orientation, faceBox)

}