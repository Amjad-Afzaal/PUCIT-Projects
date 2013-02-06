package com.pucit.andriodtermproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class Play2Activity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_2);
        ImageView play2_image = (ImageView) findViewById(R.id.play2_image);
        play2_image.setBackgroundResource(R.drawable.play2_image);
        TextView name = (TextView) findViewById(R.id.play2Name);
        name.setText(this.getIntent().getStringExtra("playerName"));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				finish();
				Intent intent = new Intent (Play2Activity.this, Play3Activity.class);
				intent.putExtra("playerName", Play2Activity.this.getIntent().getStringExtra("playerName"));
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
