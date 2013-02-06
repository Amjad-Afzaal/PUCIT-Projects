package com.pucit.andriodtermproject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class Play3Activity extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_3);
        ImageView play3_image = (ImageView) findViewById(R.id.play3_image);
        play3_image.setBackgroundResource(R.drawable.play3_image);
        TextView name = (TextView) findViewById(R.id.play3Name);
        name.setText(this.getIntent().getStringExtra("playerName"));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				finish();
				//Toast.makeText(Play3Activity.this.getApplicationContext(), "Your Score is 100", 500).show();
			}
		}, 5000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_splash_screen, menu);
        return true;
    }
}
