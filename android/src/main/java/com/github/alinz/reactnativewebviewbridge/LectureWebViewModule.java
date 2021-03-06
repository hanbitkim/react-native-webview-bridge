package com.github.alinz.reactnativewebviewbridge;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class LectureWebViewModule extends ReactContextBaseJavaModule {
    public LectureWebViewModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "LectureWebViewModule";
    }

    @ReactMethod
    public void clear() {
        LectureWebViewBridgeManager.lectureWebViews.clear();
        LectureWebViewBridgeManager.lectureWebViewInUse.clear();
    }
}
