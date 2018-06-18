# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


# Firebase Messaging ~~~~~~~~~~
-keepattributes EnclosingMethod
-keepattributes InnerClasses

# Firebase Authentication, Storage, Database ~~~~~~~~~~
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses

# Firebase Authentication ~~~~~~~~~~
# Required for Twitter Authentication
# https://docs.fabric.io/android/twitter/twitter.html#set-up-kit
-dontwarn com.squareup.okhttp.**
-dontwarn com.google.appengine.api.urlfetch.**
-dontwarn rx.**
-dontwarn retrofit.**
-dontwarn retrofit2.**
-dontwarn okio.**
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

# Firebase Database ~~~~~~~~~~
# This rule will properly ProGuard all the model classes in
# the package com.yourcompany.models. Modify to fit the structure
# of your app.
-keep class com.google.firebase.quickstart.database.viewholder.** {
    *;
}
-keepclassmembers class com.google.firebase.quickstart.database.models.** {
    *;
}

# Firebase Crashlytics ~~~~~~~~~~
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
# For faster builds with ProGuard, exclude Crashlytics. Add the following lines to your config file:
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**