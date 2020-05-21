-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}


-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}


#-libraryjars libs/android-async-http-1.4.6.jar
-keep class com.loopj.android.http** { *; }

#-libraryjars libs/httpmime-4.1.2.jar
-keep class org.apache.http.mime.** { *; }

#-libraryjars libs/nineoldandroids.jar
-keep class com.nineoldandroids.** { *; }

#-libraryjars libs/universal-image-loader-1.8.6-with-sources.jar
-keep class com.nostra13.universalimageloader.** { *; }

#-libraryjars libs/xUtils-2.6.14.jar
-keep class com.lidroid.xutils.** { *; }

#-libraryjars libs/zxing.jar
-keep class com.google.zxing.** { *; }

#-libraryjars libs/gson-2.2.4.jar
-keep class com.google.gson.** { *; }

#个推
#-libraryjars libs/GetuiExt-2.0.3.jar
#-libraryjars libs/GetuiSDK2.6.1.0.jar
-dontwarn com.igexin.**
-keep class com.igexin.** { *; }
-keep class org.json.** { *; }
-keep class com.igexin.getuiext.** { *; }

#高德地图
#-libraryjars libs/AMap_Location_v1.4.1_20150917.jar
-keep class com.amap.api.location.** { *; }
-keep class com.aps.** { *; }
#-libraryjars libs/AMap_Navi_v1.4.0_20150916.jar
-keep class com.amap.api.navi.** { *; }
-keep class com.autonavi.** { *; }
#-libraryjars libs/Android_Map_2.5.1.20150827.jar
-keep class com.amap.api.** { *; }
-keep class com.autonavi.amap.** { *; }
#-libraryjars libs/Msc.jar
-keep class com.iflytek.cloud.** { *; }
-keep class com.iflytek.msc.** { *; }


#-libraryjars libs/android-support-v4.jar
-dontwarn android.support.v4.**  
-keep class android.support.v4.** { *; }  
-keep class * extends android.support.v4.**  
-keep class * extends android.app.Fragment  

-ignorewarnings

-keepattributes SourceFile,LineNumberTable

-keep class org.xmlpull.v1.XmlSerializer { 

*; 

}

#友盟分享
-dontshrink
-dontoptimize
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**


-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**

-keep public class com.umeng.socialize.* {*;}
-keep public class javax.**
-keep public class android.webkit.**

-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**

-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}

-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}


-keep public class com.yilian.mall.R$*{
    public static final int *;
}

##---------------Begin: proguard configuration for Gson  ----------
    # Gson uses generic type information stored in a class file when working with fields. Proguard
    # removes such information by default, so configure it to keep all of it.
    -keepattributes Signature  
    # Gson specific classes
    -keep class sun.misc.Unsafe { *; }
    #-keep class com.google.gson.stream.** { *; }  
    # Application classes that will be serialized/deserialized over Gson
    -keep class com.google.gson.examples.android.model.** { *; }  
    ##---------------End: proguard configuration for Gson  ----------
    # For using GSON @Expose annotation
-keepattributes *Annotation*

-keepclassmembers class com.yilian.mall.ui.AndroidJs {
  public *;
}
-keepattributes *JavascriptInterface*


#融云混淆规则
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
 public *;
}
-keepattributes Exceptions,InnerClasses

-keepattributes Signature

-keepattributes *Annotation*

-keep class com.google.gson.examples.android.model.** { *; }

-keep class **$Properties
-dontwarn org.eclipse.jdt.annotation.**
-keep class io.agora.rtc.** {*;}

-keep class io.rong.** {*;}
-keep class io.agora.rtc.** {*; }
-keep class * implements io.rong.imlib.model.MessageContent{*;}

#-dontwarn io.rong.push.**
#-dontnote com.xiaomi.**
#-dontnote com.huawei.android.pushagent.**
#-dontnote com.google.android.gms.gcm.**
#-dontnote io.rong.**
#-ignorewarnings


-keepclassmembers class com.yilian.mall.ui.AndroidJs {
  public *;
}
-keepattributes *JavascriptInterface*
#bugly混淆规则
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keep class com.baidu.tts.**{*;}
-keep class com.baidu.speechsynthesizer.**{*;}
#BaseRecyclerAdapter
-keep class com.chad.library.adapter.** {
*;
}
-keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
-keep public class * extends com.chad.library.adapter.base.BaseViewHolder
-keepclassmembers  class **$** extends com.chad.library.adapter.base.BaseViewHolder {
     <init>(...);
}

#保持实体类不被混淆 防止gson解析异常
-keep class com.yilian.networkingmodule.entity.**{*;}
-keep class com.yilian.mall.entity.** { *; }
-keep class rxfamily.entity.** { *; }

-keep class com.gyf.barlibrary.* {*;}