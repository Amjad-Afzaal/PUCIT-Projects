/**
 * 
 */
package com.pucit.andriodtermproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * @author Amjad Afzaal
 *
 */
public class GameStartingView extends SurfaceView implements Callback{

	Context context;
	GameMainThread dominoKnight;
	public static long Score;
	public GameStartingView(Context context) {
		super(context);
		Score = 0;
		this.context = context;
		getHolder().addCallback(this);
		this.dominoKnight = new GameMainThread(getHolder(), this.context, new Handler()
		{
			@Override
		      public void handleMessage(Message m) {
		        // Use for pushing back messages.
		      }
		});
		setFocusable(true);
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		Log.d(this.getClass().toString(), "in SurfaceChanged()");
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.d(this.getClass().toString(), "in SurfaceCreated()");
		dominoKnight.isRunning = true;
		dominoKnight.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.d(this.getClass().toString(), "in SurfaceDestroyed()");
	    boolean retry = true;
	    dominoKnight.isRunning = false;
	    if(dominoKnight.isMusicOn())
	    {
	    	MainMenuActivity.mediaPlayer.stop();
	    	MainMenuActivity.mediaPlayer.release();
	    	MainMenuActivity.mediaPlayer = null;
	    }
		if(MainMenuActivity.isMusicOn)
		{
			MainMenuActivity.mediaPlayer = MediaPlayer.create(context, R.raw.main_audio);
			MainMenuActivity.mediaPlayer.setLooping(true);
			MainMenuActivity.mediaPlayer.start();
		}
	    SharedPreferences pref = context.getSharedPreferences("GamePreference", Context.MODE_PRIVATE);
	    SharedPreferences.Editor editor = pref.edit();
	    editor.putLong("Score", Score);
	    editor.commit(); 
		new DataBaseHandler(context).addTask(pref.getString("Name", "Player"), pref.getLong("Score", 0));
	    while (retry) {
	      try {
	    	dominoKnight.join();
	        retry = false;
	      } catch (Exception e) {}
	    }
	}

}
