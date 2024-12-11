//
// Created by yuanhao on 20-6-10.
//

#ifndef LIVEBODYEXAMPLE_IMG_PROCESS_H
#define LIVEBODYEXAMPLE_IMG_PROCESS_H

#include <jni.h>
#include <opencv2/core/mat.hpp>

int ConvertBitmap2Mat(JNIEnv* env, jobject bitmap, cv::Mat& out);

/**
 * @brief Converts a YUV420sp image to a BGR image.
 *
 * This function takes a YUV420sp (NV21) formatted image and converts it to a BGR image using OpenCV.
 *
 * @param data Pointer to the input YUV420sp image data.
 * @param width Width of the input image.
 * @param height Height of the input image.
 * @param orientation Orientation of the input image. This can be used to rotate the image if necessary.
 * @param dst Reference to the output cv::Mat object where the BGR image will be stored.
 *
 * @note The input image data should be in YUV420sp (NV21) format.
 *
 * @example
 * unsigned char* yuvData = ...; // YUV420sp image data
 * int width = 640;
 * int height = 480;
 * int orientation = 0; // No rotation
 * cv::Mat bgrImage;
 * Yuv420sp2bgr(yuvData, width, height, orientation, bgrImage);
 */
void Yuv420sp2bgr(unsigned char* data, int width, int height, int orientation, cv::Mat& dst);

void RotateClockWise90(cv::Mat& image);

void RotateAntiClockWise90(cv::Mat& image);

#endif //LIVEBODYEXAMPLE_IMG_PROCESS_H
