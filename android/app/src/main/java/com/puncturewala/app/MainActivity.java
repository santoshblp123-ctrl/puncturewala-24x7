package com.puncturewala.app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private static final int LOCATION_PERMISSION_REQUEST = 1001;
    private GeolocationPermissions.Callback geolocationCallback;
    private String geolocationOrigin;

    // Change this to your live production server URL or keep 10.0.2.2 for Android emulator
    private static final String APP_URL = "http://10.0.2.2:3000/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        setContentView(webView);

        // WebView Settings
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAllowFileAccess(true);
        settings.setMediaPlaybackRequiresUserGesture(false);

        // Expose AndroidBridge for external Google Maps & native links
        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public boolean isNative() {
                return true;
            }

            @JavascriptInterface
            public void openExternalUrl(final String rawUrl) {
                runOnUiThread(() -> openExternalTarget(rawUrl));
            }
        }, "AndroidBridge");

        // Handle External links, dialer, Google Maps
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                return openExternalTarget(url);
            }
        });

        // Geolocation Permission Chrome Client
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                geolocationOrigin = origin;
                geolocationCallback = callback;

                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            LOCATION_PERMISSION_REQUEST);
                } else {
                    callback.invoke(origin, true, false);
                }
            }
        });

        webView.loadUrl(APP_URL);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (geolocationCallback != null) {
                    geolocationCallback.invoke(geolocationOrigin, true, false);
                }
            } else {
                if (geolocationCallback != null) {
                    geolocationCallback.invoke(geolocationOrigin, false, false);
                }
            }
        }
    }

    private boolean openExternalTarget(String rawUrl) {
        if (rawUrl == null) return false;
        String trimmed = rawUrl.trim();
        String lower = trimmed.toLowerCase(Locale.ROOT);
        if (lower.startsWith("tel:")) {
            try {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(trimmed)));
            } catch (Exception ignored) {}
            return true;
        }
        if (lower.startsWith("intent:")) {
            try {
                Intent intent = Intent.parseUri(trimmed, Intent.URI_INTENT_SCHEME);
                if (intent != null) {
                    try {
                        startActivity(intent);
                        return true;
                    } catch (Exception ignored) {
                        String fallback = intent.getStringExtra("browser_fallback_url");
                        if (fallback != null && !fallback.trim().isEmpty()) {
                            return openExternalTarget(fallback);
                        }
                    }
                }
            } catch (Exception ignored) {}
            return true;
        }
        boolean isMaps = lower.startsWith("geo:") || lower.contains("maps.google.") || lower.contains("google.com/maps") || lower.contains("maps.app.goo.gl") || lower.contains("goo.gl/maps");
        if (isMaps || lower.startsWith("mailto:") || lower.startsWith("whatsapp:") || lower.contains("wa.me/") || lower.contains("api.whatsapp.com")) {
            if (isMaps) {
                try {
                    Intent mapsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(trimmed));
                    mapsIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapsIntent);
                    return true;
                } catch (Exception ignored) {}
            }
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trimmed)));
            } catch (Exception ignored) {}
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
