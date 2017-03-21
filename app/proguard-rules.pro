# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android-sdks/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
 #class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
  public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}
# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontwarn android.support.**
-dontwarn com.tencent.**
-keep class com.tencent.**{*;}
#假设调用不产生任何影响，在proguard代码优化时会将该调用remove掉。如system.out.println和Log.v等等
#-assumenosideeffects class_specification
# -libraryjars libs/tbs_webview_v2.6.jar

# OkHttp3
-dontwarn okhttp3.**
-keep class okhttp3.** { *;}
-dontwarn okio.**


# Okio
-dontwarn okio.**
-keep class okio.**{*;}

#-keep class org.xutils.**{*;}
#-dontwarn org.xutils.**
 #-keepattributes Signature

 -libraryjars xutils
 -dontwarn  org.xutils.**
 -keep class org.xutils.** { *;}
 -keep interface org.xutils.** {*;}
 -keep class * extends java.lang.annotation.Annotation { *; }
 -keep class * implements org.xutils.common.CommonCallback
 #-keep （class * implements org.xutils.common.Callback）$*

 #反射处理
 -keepattributes Signature
 -keepattributes EnclosingMethod


#不混淆R类里及其所有内部static类中的所有static变量字段
-keepclassmembers class **.R$* {
    public static <fields>;
}





#-keep class com.dgkj.tianmi.consts.TimmyConst{*;}

#不进行混淆保持原样
-keep public class * extends android.support.v7.app.AppCompatActivity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment

#gson
################gson##################
#-libraryjars libs/gson-2.2.4.jar
-keep class com.google.gson.** {*;}
#-keep class com.google.**{*;}
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.google.** {
    <fields>;
    <methods>;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-dontwarn com.google.gson.**

#实体类
#-keep class com.dgkj.tianmi.bean.Theme.**{*;}
#-keep class com.dgkj.tianmi.bean.Them2.**{*;}
#-keepclasseswithmembers class com.dgkj.tianmi.bean.Theme { *; }
#-keepclasseswithmembers class com.dgkj.tianmi.bean.Them2 { *; }
