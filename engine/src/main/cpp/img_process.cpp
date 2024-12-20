//
// Created by yuanhao on 20-6-10.
//

#include <android/bitmap.h>
#include <opencv2/imgproc.hpp>
#include "img_process.h"
#include "android_log.h"

int ConvertBitmap2Mat(JNIEnv* env, jobject bitmap, cv::Mat& out) {
    AndroidBitmapInfo info;
    void* pixels = nullptr;
    int ret;

    if((ret = AndroidBitmap_getInfo(env, bitmap, &info)) < 0) {
        LOG_WARN("AndroidBitmap_getInfo() failed, ret = %d", ret);
        return ret;
    }

    if(info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        LOG_WARN("Bitmap format is not ANDROID_BITMAP_FORMAT_RGBA_8888");
        return -1;
    }

    if((ret = AndroidBitmap_lockPixels(env, bitmap, &pixels)) < 0) {
        LOG_WARN("AndroidBitmap_lockPixels() failed, ret = %d", ret);
        return ret;
    }

    cv::Mat img(info.height, info.width, CV_8UC4, pixels, info.stride);
    cv::cvtColor(img, out, cv::COLOR_RGBA2BGR);
    return 0;
}

/**
 *   1       2      3       4          5           6          7            8
 *
 *  888888  888888      88  88      8888888888  88                  88  8888888888
 *  88          88      88  88      88  88      88  88          88  88      88  88
 *  8888      8888    8888  8888    88          8888888888  8888888888          88
 *  88          88      88  88
 *  88          88  888888  888888
 */
/**
 * @brief Converts YUV420sp (NV21) image data to BGR format and applies orientation transformations.
 * 
 * @param data Pointer to the input YUV420sp (NV21) image data.
 * @param width Width of the input image.
 * @param height Height of the input image.
 * @param orientation Orientation code indicating the type of transformation to apply:
 *        1 - No transformation
 *        2 - Horizontal flip
 *        3 - Horizontal and vertical flip
 *        4 - Vertical flip
 *        5 - Transpose
 *        6 - Rotate 90 degrees clockwise
 *        7 - Horizontal and vertical flip, then transpose
 *        8 - Rotate 90 degrees counterclockwise
 * @param dst Reference to the output cv::Mat object where the BGR image will be stored.
 * 
 * @note The function first converts the YUV420sp (NV21) image data to BGR format using OpenCV's cvtColor function.
 *       Then, it applies the specified orientation transformation to the BGR image.
 * 
 * @example
 * unsigned char* yuvData = ...; // Pointer to YUV420sp (NV21) data
 * int width = ...; // Width of the image
 * int height = ...; // Height of the image
 * int orientation = 6; // Rotate 90 degrees clockwise
 * cv::Mat bgrImage;
 * Yuv420sp2bgr(yuvData, width, height, orientation, bgrImage);
 */
void Yuv420sp2bgr(unsigned char* data, int width, int height, int orientation, cv::Mat& dst) {
    cv::Mat yuv(height + height / 2, width, CV_8UC1, data);
    dst.create(height, width, CV_8UC3);

    cv::cvtColor(yuv, dst, cv::COLOR_YUV2BGR_NV21);

    switch (orientation) {
        case 1: // 不需要处理
            break;
        case 2: // 水平翻转
            cv::flip(dst, dst, 1);
            break;
        case 3: // 先水平翻转 然后垂直翻转
            cv::flip(dst, dst, -1);
            break;
        case 4: // 垂直翻转
            cv::flip(dst, dst, 0);
            break;
        case 5: // transpose
            cv::transpose(dst, dst);
            break;
        case 6: // 顺时针旋转90°
            RotateClockWise90(dst);
            break;
        case 7: // 水平、垂直翻转 --> transpose
            cv::flip(dst, dst, -1);
            cv::transpose(dst, dst);
            break;
        case 8: // 逆时针旋转90°
            RotateAntiClockWise90(dst);
            break;
        default:break;
    }
}

void RotateAntiClockWise90(cv::Mat &image) {
    if (image.empty()) return;

    cv::transpose(image, image);
    cv::flip(image, image, 0);
}

void RotateClockWise90(cv::Mat &image) {
    if (image.empty()) return;

    cv::transpose(image, image);
    cv::flip(image, image, 1);
}
