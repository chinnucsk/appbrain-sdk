package com.appbrain.example;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.appbrain.AdService;
import com.appbrain.AppBrain;
import com.appbrain.RemoteSettings;

public class ExampleActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppBrain.init(this);
        setContentView(R.layout.main);

        final RemoteSettings settings = AppBrain.getSettings();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // fetch the welcome message from the remote settings
                String welcomeMessage = settings.get("welcome_message",
                    "Hello (this comes from the app)");
                // show as toast
                Toast.makeText(ExampleActivity.this, welcomeMessage, Toast.LENGTH_LONG).show();
            }
        }, 2500);

        
        final AdService ads = AppBrain.getAds();
        findViewById(R.id.maybe_show_interstitial).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!ads.maybeShowInterstitial(ExampleActivity.this)) {
                    Toast.makeText(ExampleActivity.this,
                        "not showing, since it was shown already recently", Toast.LENGTH_LONG).show();
                }
            }
        });
        
        findViewById(R.id.show_interstitial).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!ads.showInterstitial(ExampleActivity.this)) {
                    Toast.makeText(ExampleActivity.this,
                        "Not showing, no internet connection?", Toast.LENGTH_LONG).show();
                }
            }
        });        

        findViewById(R.id.show_offerwall).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ads.showOfferWall(ExampleActivity.this);
            }
        });
        
        findViewById(R.id.show_banners).setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(ExampleActivity.this, BannerActivity.class);
                startActivity(intent);
            }
        });
    }

    // @Override
    public void onBackPressed() {
        AppBrain.getAds().maybeShowInterstitial(this);
        finish();
    }

    @Override
    @TargetApi(value=5)
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (android.os.Build.VERSION.SDK_INT < 5 && keyCode == KeyEvent.KEYCODE_BACK
            && event.getRepeatCount() == 0) {
            // Take care of calling this method on earlier versions of
            // the platform where it doesn't exist.
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }
}