//
// Created by yuanhao on 20-6-12.
//

#ifndef LIVEBODYEXAMPLE_LIVE_H
#define LIVEBODYEXAMPLE_LIVE_H

#include <opencv2/core/mat.hpp>
#include "../include/ncnn/net.h"
#include "../definition.h"

/**
 * @class Live
 * @brief A class for handling live face anti-spoofing detection.
 * 
 * This class provides functionalities to load models and detect spoofing in live face images.
 */

/**
 * @brief Constructor for the Live class.
 */
Live();

/**
 * @brief Destructor for the Live class.
 */
~Live();

/**
 * @brief Loads the model configurations.
 * 
 * @param assetManager A pointer to the AAssetManager for accessing assets.
 * @param configs A vector of ModelConfig objects containing model configurations.
 * @return int Returns 0 on success, non-zero on failure.
 */
int LoadModel(AAssetManager *assetManager, std::vector<ModelConfig> &configs);

/**
 * @brief Detects spoofing in the given image.
 * 
 * @param src A reference to the input image (cv::Mat).
 * @param box A reference to the FaceBox containing face bounding box information.
 * @return float Returns a score indicating the likelihood of spoofing.
 */
float Detect(cv::Mat &src, FaceBox &box);

/**
 * @brief Calculates the bounding box for the face.
 * 
 * @param box A reference to the FaceBox containing face bounding box information.
 * @param w The width of the image.
 * @param h The height of the image.
 * @param config A reference to the ModelConfig object.
 * @return cv::Rect Returns the calculated bounding box.
 */
cv::Rect CalculateBox(FaceBox &box, int w, int h, ModelConfig &config);

/**
 * @var nets_
 * @brief A vector of pointers to ncnn::Net objects representing the neural networks.
 */

/**
 * @var configs_
 * @brief A vector of ModelConfig objects containing model configurations.
 */

/**
 * @var net_input_name_
 * @brief The name of the input layer for the neural network.
 */

/**
 * @var net_output_name_
 * @brief The name of the output layer for the neural network.
 */

/**
 * @var model_num_
 * @brief The number of models loaded.
 */

/**
 * @var thread_num_
 * @brief The number of threads to be used for processing.
 */

/**
 * @var option_
 * @brief The ncnn::Option object containing options for the neural network.
 */
class Live {
public:
    Live();

    ~Live();

    int LoadModel(AAssetManager *assetManager, std::vector<ModelConfig> &configs);

    float Detect(cv::Mat &src, FaceBox &box);

private:
    cv::Rect CalculateBox(FaceBox &box, int w, int h, ModelConfig &config);

private:
    std::vector<ncnn::Net *> nets_;
    std::vector<ModelConfig> configs_;
    const std::string net_input_name_ = "data";
    const std::string net_output_name_ = "softmax";
    int model_num_;
    int thread_num_;

    ncnn::Option option_;
};

#endif //LIVEBODYEXAMPLE_LIVE_H
