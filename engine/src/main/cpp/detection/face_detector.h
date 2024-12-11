//
// Created by yuanhao on 20-6-10.
//

#ifndef LIVEBODYEXAMPLE_FACE_DETECTOR_H
#define LIVEBODYEXAMPLE_FACE_DETECTOR_H

#include <opencv2/core/mat.hpp>
#include "../include/ncnn/net.h"
#include "../definition.h"


/**
 * @class FaceDetector
 * @brief A class for detecting faces in images using a neural network.
 * 
 * This class provides functionalities to load a face detection model, set the minimum face size for detection,
 * and detect faces in a given image.
 */

/**
 * @brief Constructor for the FaceDetector class.
 * 
 * Initializes the FaceDetector object.
 */
FaceDetector();

/**
 * @brief Destructor for the FaceDetector class.
 * 
 * Cleans up resources used by the FaceDetector object.
 */
~FaceDetector();

/**
 * @brief Sets the minimum face size for detection.
 * 
 * @param size The minimum size of faces to be detected.
 */
void SetMinFaceSize(int size);

/**
 * @brief Loads the face detection model.
 * 
 * @param assetManager A pointer to the AAssetManager to load the model from assets.
 * @return int Returns 0 on success, or a non-zero error code on failure.
 */
int LoadModel(AAssetManager* assetManager);

/**
 * @brief Detects faces in the given image.
 * 
 * @param src The input image in which to detect faces.
 * @param boxes A vector to store the detected face bounding boxes.
 * @return int Returns the number of faces detected.
 */
int Detect(cv::Mat& src, std::vector<FaceBox>& boxes);

private:
    ncnn::Net net_; ///< The neural network used for face detection.
    int input_size_ = 192; ///< The input size for the neural network.
    const std::string net_input_name_ = "data"; ///< The input layer name of the neural network.
    const std::string net_output_name_ = "detection_out"; ///< The output layer name of the neural network.
    ncnn::Option option_; ///< Options for configuring the neural network.
    float threshold_; ///< The threshold for face detection.
    const float mean_val_[3] = {104.f, 117.f, 123.f}; ///< The mean values for input normalization.
    int thread_num_; ///< The number of threads to use for inference.
    int min_face_size_; ///< The minimum face size to be detected.
};



#endif //LIVEBODYEXAMPLE_FACE_DETECTOR_H
