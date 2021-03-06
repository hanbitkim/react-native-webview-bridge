package com.github.alinz.reactnativewebviewbridge;

import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.views.webview.ReactWebViewManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class WebViewBridgeManager extends ReactWebViewManager {
    private static final String REACT_CLASS = "RCTWebViewBridge";
    public static ArrayList<WebView> webViews = new ArrayList<>();
    public static int currentWebViewId = 0;
    public static final int COMMAND_SEND_TO_BRIDGE = 101;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public Map<String, Integer> getCommandsMap() {
        Map<String, Integer> commandsMap = super.getCommandsMap();
        commandsMap.put("sendToBridge", COMMAND_SEND_TO_BRIDGE);
        return commandsMap;
    }

    @Override
    protected WebView createViewInstance(ThemedReactContext reactContext) {
        WebView webView = null;
        if (webViews.size() < 3) {
            currentWebViewId = webViews.size();
            webView = super.createViewInstance(reactContext);
            webView.addJavascriptInterface(new JavascriptBridge(webView), "WebViewBridge");
            webViews.add(webView);
        }
        else {
            webView = webViews.get(currentWebViewId);
        }

        if (webView.getParent() != null ) {
            ((ViewGroup)webView.getParent()).removeView(webView);
        }

        return webView;
    }

    @Override
    public void receiveCommand(WebView root, int commandId, @Nullable ReadableArray args) {
        super.receiveCommand(root, commandId, args);

        switch (commandId) {
            case COMMAND_SEND_TO_BRIDGE:
                sendToBridge(root, args.getString(0));
                break;
            default:
                //do nothing!!!!
        }
    }

    private void sendToBridge(WebView root, String message) {
        String script = "WebViewBridge.onMessage('" + message + "');";
        WebViewBridgeManager.evaluateJavascript(root, script);
    }

    static private void evaluateJavascript(WebView root, String javascript) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            root.evaluateJavascript(javascript, null);
        } else {
            root.loadUrl("javascript:" + javascript);
        }
    }

    @ReactProp(name = "allowFileAccessFromFileURLs")
    public void setAllowFileAccessFromFileURLs(WebView root, boolean allows) {
        root.getSettings().setAllowFileAccessFromFileURLs(allows);
    }

    @ReactProp(name = "allowUniversalAccessFromFileURLs")
    public void setAllowUniversalAccessFromFileURLs(WebView root, boolean allows) {
        root.getSettings().setAllowUniversalAccessFromFileURLs(allows);
    }

    @Override
    public void onDropViewInstance(WebView webView) {
        if (webView.getParent() != null ){
            ((ViewGroup)webView.getParent()).removeView(webView);
        }
    }
}
