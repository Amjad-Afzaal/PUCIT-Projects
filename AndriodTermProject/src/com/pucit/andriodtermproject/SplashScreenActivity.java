package com.pucit.andriodtermproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.widget.ImageView;

public class SplashScreenActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen); 
        ImageView splash_screen = (ImageView) findViewById(R.id.splash_screen_image);
        splash_screen.setBackgroundResource(R.drawable.splash_screen_image);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				finish();
				Intent intent = new Intent(SplashScreenActivity.this, MainMenuActivity.class);
				startActivity(intent);
			}
		}, 5000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_splash_screen, menu);
        return true;
    }
}
