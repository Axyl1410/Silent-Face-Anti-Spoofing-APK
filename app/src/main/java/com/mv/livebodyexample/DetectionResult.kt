package com.mv.livebodyexample

import android.graphics.Rect
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.mv.engine.FaceBox

/**
 * A class representing the result of a face detection operation.
 * This class extends [BaseObservable] to support data binding.
 */
class DetectionResult(): BaseObservable() {

    /**
     * The left coordinate of the detected face bounding box.
     */
    @get:Bindable
    var left: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.left)
        }

    /**
     * The top coordinate of the detected face bounding box.
     */
    @get:Bindable
    var top: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.top)
        }

    /**
     * The right coordinate of the detected face bounding box.
     */
    @get:Bindable
    var right: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.right)
        }

    /**
     * The bottom coordinate of the detected face bounding box.
     */
    @get:Bindable
    var bottom: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.bottom)
        }

    /**
     * The confidence score of the face detection.
     */
    @get:Bindable
    var confidence: Float = 0.toFloat()
        set(value) {
            field = value
            notifyPropertyChanged(BR.confidence)
        }

    /**
     * The timestamp of the detection result.
     */
    var time: Long = 0

    /**
     * The threshold value for face detection.
     */
    var threshold: Float = 0F

    /**
     * A flag indicating whether a face has been detected.
     */
    @get:Bindable
    var hasFace: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.hasFace)
        }

    /**
     * Secondary constructor to initialize the DetectionResult with a [FaceBox], timestamp, and face detection flag.
     *
     * @param faceBox The bounding box and confidence score of the detected face.
     * @param time The timestamp of the detection result.
     * @param hasFace A flag indicating whether a face has been detected.
     */
    constructor(faceBox: FaceBox, time: Long, hasFace: Boolean) : this() {
        this.left = faceBox.left
        this.top = faceBox.top
        this.right = faceBox.right
        this.bottom = faceBox.bottom
        this.confidence = faceBox.confidence
        this.time = time
        this.hasFace = hasFace
    }

    /**
     * Updates the location of the detected face bounding box.
     *
     * @param rect The new bounding box coordinates.
     * @return The updated [DetectionResult] instance.
     */
    fun updateLocation(rect: Rect): DetectionResult {
        this.left = rect.left
        this.top = rect.top
        this.right = rect.right
        this.bottom = rect.bottom

        return this
    }
}

