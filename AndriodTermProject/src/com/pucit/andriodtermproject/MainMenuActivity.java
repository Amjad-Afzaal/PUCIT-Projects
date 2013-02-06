package com.pucit.andriodtermproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainMenuActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ImageView main_screen = (ImageView) findViewById(R.id.main_menu_image);
        main_screen.setBackgroundResource(R.drawable.main_menu_image);
        Button play = (Button) findViewById(R.id.Play);
        Button high_score = (Button) findViewById(R.id.HighScore);
        Button help = (Button) findViewById(R.id.Help);
        Button quit = (Button) findViewById(R.id.Quit);
        play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent (MainMenuActivity.this, FloatingActivity.class);
				startActivity(intent);
			}
		});
        high_score.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent (MainMenuActivity.this, HighScoreMenuActivity.class);
				startActivity(intent);
			}
		});
        help.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent (MainMenuActivity.this, HelpMenuActivity.class);
				startActivity(intent);
			}
		});
        quit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent (MainMenuActivity.this, QuitMenuActivity.class);
				finish();
				startActivity(intent);
			}
		});
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_splash_screen, menu);
        return true;
    }
}
