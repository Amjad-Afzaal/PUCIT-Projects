package com.pucit.andriodtermproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FloatingActivity extends Activity{

    

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floating);
        Button ok = (Button) findViewById(R.id.okButton);
        Button cancil = (Button) findViewById(R.id.cancelButton);
        final EditText playerName = (EditText) findViewById(R.id.playerName);
        ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(playerName.getText().toString().equals(""))
					Toast.makeText(FloatingActivity.this, "Please Enter Your Name!", Toast.LENGTH_LONG).show();
				else
				{
					finish();
					Intent intent = new Intent (FloatingActivity.this, Play1Activity.class);
					SharedPreferences pref = getSharedPreferences("GamePreference", MODE_PRIVATE);
					SharedPreferences.Editor editor = pref.edit();
					editor.putString("Name", playerName.getText().toString());
					editor.putLong("Score", 0);
					editor.commit();
					startActivity(intent);
				}
			}
		});
        cancil.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				Intent intent = new Intent (FloatingActivity.this, MainMenuActivity.class);
				startActivity(intent);
			}
		});     
    }

	
		
    
}
