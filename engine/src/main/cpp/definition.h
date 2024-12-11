//
// Created by yuanhao on 20-6-9.
//

#ifndef LIVEBODYEXAMPLE_DEFINITION_H
#define LIVEBODYEXAMPLE_DEFINITION_H

#include <string>

#define JAVA_ARRAY_LIST_CLASSPATH           "java/util/ArrayList"
#define ANDROID_FACE_BOX_CLASSPATH          "com/mv/engine/FaceBox"

#define FACE_DETECTOR_METHOD(METHOD_NAME) \
    Java_com_mv_engine_FaceDetector_##METHOD_NAME


#define LIVE_METHOD(METHOD_NAME) \
    Java_com_mv_engine_Live_##METHOD_NAME

struct FaceBox {
    float confidence;
    float x1;
    float y1;
    float x2;
    float y2;
};

/**
 * @struct ModelConfig
 * @brief Configuration structure for a model.
 *
 * This structure holds the configuration parameters for a model, including
 * scaling factors, shifts, dimensions, and other properties.
 *
 * @var ModelConfig::scale
 * Scaling factor for the model.
 *
 * @var ModelConfig::shift_x
 * Horizontal shift for the model.
 *
 * @var ModelConfig::shift_y
 * Vertical shift for the model.
 *
 * @var ModelConfig::height
 * Height of the model.
 *
 * @var ModelConfig::width
 * Width of the model.
 *
 * @var ModelConfig::name
 * Name of the model.
 *
 * @var ModelConfig::org_resize
 * Flag indicating whether to use original resizing.
 */
struct ModelConfig {
    float scale;
    float shift_x;
    float shift_y;
    int height;
    int width;
    std::string name;
    bool org_resize;
};

#endif //LIVEBODYEXAMPLE_DEFINITION_H
