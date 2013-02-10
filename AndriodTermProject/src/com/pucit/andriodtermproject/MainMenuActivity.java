package com.pucit.andriodtermproject;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ToggleButton;

public class MainMenuActivity extends Activity {

	public static MediaPlayer mediaPlayer;
	public static boolean isMusicOn = true;
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
        final ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.main_audio);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        toggleButton.setChecked(true);
        play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent (MainMenuActivity.this, FloatingActivity.class);
				if(MainMenuActivity.isMusicOn)
				{
					MainMenuActivity.mediaPlayer.stop();
					MainMenuActivity.mediaPlayer.release();
					MainMenuActivity.mediaPlayer = null;
					toggleButton.setChecked(true);
				}
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
        toggleButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(toggleButton.getText().equals("Music On"))
				{
					MainMenuActivity.mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.main_audio);
					MainMenuActivity.mediaPlayer.setLooping(true);
					MainMenuActivity.mediaPlayer.start();
					MainMenuActivity.isMusicOn = true;
				}
				if(toggleButton.getText().equals("Music Off"))
				{
					MainMenuActivity.mediaPlayer.stop();
					MainMenuActivity.mediaPlayer.release();
					MainMenuActivity.mediaPlayer = null;
					MainMenuActivity.isMusicOn = false;
				}
			}
		});
        quit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent (MainMenuActivity.this, QuitMenuActivity.class);
				if(MainMenuActivity.isMusicOn)
				{
					MainMenuActivity.mediaPlayer.stop();
					MainMenuActivity.mediaPlayer.release();
					MainMenuActivity.mediaPlayer = null;
				}
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
