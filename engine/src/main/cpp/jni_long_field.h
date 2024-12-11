//
// Created by yuanhao on 20-6-9.
//

#ifndef LIVEBODYEXAMPLE_JNI_LONG_FIELD_H
#define LIVEBODYEXAMPLE_JNI_LONG_FIELD_H

#include <jni.h>

/**
 * @class JniLongField
 * @brief A helper class to get and set long fields in Java objects from native C++ code using JNI.
 *
 * This class provides methods to get and set the value of a long field in a Java object.
 * It caches the field ID to avoid repeated lookups, improving performance.
 *
 * @param field_name_ The name of the Java field to be accessed.
 * @param field_id_ The cached field ID of the Java field.
 */

/**
 * @brief Constructs a JniLongField object with the specified field name.
 * @param field_name The name of the Java field to be accessed.
 */
explicit JniLongField(const char* field_name);

/**
 * @brief Gets the value of the long field from the specified Java object.
 * @param env The JNI environment pointer.
 * @param instance The Java object instance from which the field value is to be retrieved.
 * @return The value of the long field.
 */
int64_t get(JNIEnv* env, jobject instance);

/**
 * @brief Sets the value of the long field in the specified Java object.
 * @param env The JNI environment pointer.
 * @param instance The Java object instance in which the field value is to be set.
 * @param value The value to set in the long field.
 */
void set(JNIEnv* env, jobject instance, int64_t value);
class JniLongField {
public:
    explicit JniLongField(const char* field_name)
            : field_name_(field_name),
              field_id_(nullptr) {
    }

    int64_t get(JNIEnv* env, jobject instance) {
        if(field_id_ == nullptr) {
            jclass cls = env->GetObjectClass(instance);
            field_id_ = env->GetFieldID(cls, field_name_, "J");
            env->DeleteLocalRef(cls);
        }
        return env->GetLongField(instance, field_id_);
    }

    void set(JNIEnv* env, jobject instance, int64_t value) {
        if(field_id_ == nullptr) {
            jclass cls = env->GetObjectClass(instance);
            field_id_ = env->GetFieldID(cls, field_name_, "J");
            env->DeleteLocalRef(cls);
        }
        env->SetLongField(instance, field_id_, value);
    }

private:
    const char* const field_name_;
    jfieldID field_id_;
};

#endif //LIVEBODYEXAMPLE_JNI_LONG_FIELD_H
