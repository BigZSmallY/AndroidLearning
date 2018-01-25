#include <main.h>
/* Header for class com_example_hellojni_HelloJni */

#ifndef _Included_com_cityu_huangzheng_androidlearning_HelloJniActivity

#define _Included_com_cityu_huangzheng_androidlearning_HelloJniActivity

#ifdef __cplusplus

extern "C" {

#endif

/*Method1: stringFromJNI*/
JNIEXPORT jstring JNICALL Java_com_cityu_huangzheng_androidlearning_HelloJniActivity_stringFromJNI

  (JNIEnv *, jobject);


/*Method2:unimplements*/
JNIEXPORT jstring JNICALL Java_com_cityu_huangzheng_androidlearning_HelloJniActivity_unimplementedStringFromJNI

  (JNIEnv *, jobject);


#ifdef __cplusplus

}

#endif

#endif