/**
 * 
 */
package com.pucit.andriodtermproject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * @author Amjad Afzaal
 *
 */
public class GameMainThread extends Thread {
	
	private SurfaceHolder surfaceHolder;
	private Context context;
	private int width, height;
	public boolean isRunning;
	private ArrayList <Sprites> gameObjects;
	public static DominosKnight dominosKnight;
	private Sprites up, down, left, right, grass, music;
	private int [] [] map;
	private int numOfCoins;
	private int level, life;
	private Bitmap upDomino, rightDomino, leftDomino, downDomino, musicOn, musicOff;
	private boolean isMusicOn;

	public GameMainThread(SurfaceHolder surfaceHolder, Context context, Handler handler)
	{
		this.context = context;
		this.setNumOfCoins(0);
		this.level = 1;
		this.life = 15;
		this.setSurfaceHolder(surfaceHolder);
		this.isRunning = false;
		this.gameObjects = new ArrayList<Sprites>();
		this.grass = new Sprites(BitmapFactory.decodeResource(context.getResources(), R.drawable.grass), GameElements.GRASS);
		dominosKnight = new DominosKnight(BitmapFactory.decodeResource(context.getResources(), R.drawable.dominos));
		this.up = new Sprites(BitmapFactory.decodeResource(context.getResources(), R.drawable.up), GameElements.UP);
		this.down = new Sprites(BitmapFactory.decodeResource(context.getResources(), R.drawable.down), GameElements.DOWN);
		this.right = new Sprites(BitmapFactory.decodeResource(context.getResources(), R.drawable.right), GameElements.RIGHT);
		this.left = new Sprites(BitmapFactory.decodeResource(context.getResources(), R.drawable.left), GameElements.LEFT);
		this.music = new Sprites(BitmapFactory.decodeResource(context.getResources(), R.drawable.musicon), GameElements.MUSIC);
		this.upDomino = BitmapFactory.decodeResource(context.getResources(), R.drawable.updominos);
		this.downDomino = BitmapFactory.decodeResource(context.getResources(), R.drawable.downdominos);
		this.rightDomino = BitmapFactory.decodeResource(context.getResources(), R.drawable.rightdominos);
		this.leftDomino = BitmapFactory.decodeResource(context.getResources(), R.drawable.leftdominos);
		this.musicOn = BitmapFactory.decodeResource(context.getResources(), R.drawable.musicon);
		this.musicOff = BitmapFactory.decodeResource(context.getResources(), R.drawable.musicoff);
		MainMenuActivity.mediaPlayer = MediaPlayer.create(context, R.raw.in_game_audio);
		MainMenuActivity.mediaPlayer.setLooping(true);
		MainMenuActivity.mediaPlayer.start();
		this.isMusicOn = true;
	}
	
	public void StartGame()
	{
		Log.i(this.toString(), "Game Started");
	}
	
	@Override
	public void run ()
	{
		while (isRunning)
		{
			Canvas canvas = null;
			try
			{
				canvas = surfaceHolder.lockCanvas();
				if(width == 0)
				{
					width = canvas.getWidth();
					height = canvas.getHeight();
					dominosKnight.setX(width - 100);
					dominosKnight.setY(height - 115);
					this.up.setX(10 + this.up.getWidth());
					this.up.setY(this.height - 2 * this.up.getHeight());
					this.down.setX(10 + this.up.getWidth());
					this.down.setY(this.height - this.down.getHeight());
					this.right.setX(this.up.getX() + this.down.getWidth());
					this.right.setY(this.height - this.down.getHeight());
					this.left.setX(10);
					this.left.setY(this.height - this.down.getHeight());
					this.music.setX(this.width - this.music.getWidth());
					this.music.setY(this.height - this.music.getHeight());
					loadMap();
					draw(canvas);
				}
				
				synchronized (surfaceHolder) 
				{
					update(canvas);
					if(noCoinLeft()){
						this.numOfCoins = 0;
						this.level++;
						if(this.level <= 3)	loadMap();
					}
				    draw(canvas);
				    try {
						Thread.sleep(17);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				      
				}
			}
			
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			finally
			{
				if(canvas != null)
					surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}
	}
	
	private boolean noCoinLeft() {
		// TODO Auto-generated method stub
		Log.i("Num of coins = ", "---->" + this.numOfCoins);
		return this.numOfCoins <= 0 ? true : false;
	}

	private void loadMap() 
	{
		try 
		{
			AssetManager asset = context.getAssets();
			BufferedReader reader = null;
			if(this.level == 1)	reader = new BufferedReader(new InputStreamReader(asset.open("map1.txt")));
			if(this.level == 2)	reader = new BufferedReader(new InputStreamReader(asset.open("map2.txt")));
			if(this.level == 3)	reader = new BufferedReader(new InputStreamReader(asset.open("map3.txt")));
			this.gameObjects.removeAll(gameObjects);
			ArrayList<String> mapLines = new ArrayList<String>();
			String line = "";
			while((line = reader.readLine()) != null)
			{
				Log.i("line read from map1 is ", line);
				if(line.startsWith("//") || line.length() == 0)
					continue;
				mapLines.add(line);
			}
			reader.close();
			this.map = new int [mapLines.size()] [mapLines.get(0).length()];
			Log.i("map size","rows = " + mapLines.size() + " column = " + mapLines.get(0).length());
			int column = 0;
			int row = 0;
			long total = 0;
			boolean shouldRunning = true;
			for(int i=0; i<mapLines.size(); i++, row++)
			{
				for(int j=0; j<mapLines.get(i).length(); j++, column++)
				{
					this.map[i][j] = mapLines.get(i).charAt(j);
					switch(mapLines.get(i).charAt(j))
					{
						case '0' :
							// GRASS
							{
								Sprites object = new Sprites(BitmapFactory.decodeResource(context.getResources(), R.drawable.grass), GameElements.GRASS);
								if(column * object.getWidth() >= this.width) column += 1;
								if(row * object.getHeight() >= this.height) row += 1;
								if(row < this.height && column < this.width)
								{
									object.setY(row * object.getHeight());
									object.setX(column * object.getWidth());
									total += object.getWidth() * object.getHeight();
									if(total >= (this.width * this.height))
										shouldRunning = false;
									this.gameObjects.add(object);
								}
							}
							break ;
						case '1' :
							// COIN
							{
								Sprites object = new Sprites(BitmapFactory.decodeResource(context.getResources(), R.drawable.coin), GameElements.COIN);
								if(column * object.getWidth() >= this.width) column = 0;
								if(row * object.getHeight() >= this.height) row = 0;
								if(row < this.height && column < this.width)
								{
									object.setY(row * object.getHeight());
									object.setX(column * object.getWidth());
									total += object.getWidth() * object.getHeight();
									if(total >= (this.width * this.height))
										shouldRunning = false;
									if(shouldRunning)
									{
										this.gameObjects.add(object);
										this.numOfCoins++;
									}
								}
							}
							break ;
						case '2' :
							// TREE
							{
								Sprites object = new Sprites(BitmapFactory.decodeResource(context.getResources(), R.drawable.tree), GameElements.TREE);
								if(column * object.getWidth() >= this.width) column = 0;
								if(row * object.getHeight() >= this.height) row = 0;
								if(row < this.height && column < this.width)
								{
									object.setY(row * object.getHeight());
									object.setX(column * object.getWidth());
									total += object.getWidth() * object.getHeight();
									if(total >= (this.width * this.height))
										shouldRunning = false;
									this.gameObjects.add(object);
									
								}
								Log.i("Load Map", "TREE = (" + object.getX() + ", " + object.getY() + ")");
							}
							break ;
						case '3' :
							// OBSTICAL
							{
								Sprites object = new Sprites(BitmapFactory.decodeResource(context.getResources(), R.drawable.obstical), GameElements.OBSTICAL);
								if(column * object.getWidth() >= this.width) column = 0;
								if(row * object.getHeight() >= this.height) row = 0;
								if(row < this.height && column < this.width)
								{
									object.setY(row * object.getHeight());
									object.setX(column * object.getWidth());
									total += object.getWidth() * object.getHeight();
									if(total >= (this.width * this.height))
										shouldRunning = false;
									this.gameObjects.add(object);
									
								}
							}
							break;
						case '4' :
							// FIRE
						{
							Sprites object = new Sprites(BitmapFactory.decodeResource(context.getResources(), R.drawable.fire), GameElements.OBSTICAL);
							if(column * object.getWidth() >= this.width) column = 0;
							if(row * object.getHeight() >= this.height) row = 0;
							if(row < this.height && column < this.width)
							{
								object.setY(row * object.getHeight());
								object.setX(column * object.getWidth());
								total += object.getWidth() * object.getHeight();
								if(total >= (this.width * this.height))
									shouldRunning = false;
								this.gameObjects.add(object);
							}
						}
					}
					Log.i("Total = ", "Total = " + total);
					if(shouldRunning == false)	break;
				}	
				if(shouldRunning == false)	break;
			}
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void draw(Canvas canvas) 
	{
		// Drawing GRASS on the whole screen
		if(this.level < 4 && this.life >= 0)
		{
			for(int i=0; i<this.height; i++)
			{
				for(int j=0; j<this.width; j++)
				{
					this.grass.setX(j * this.grass.getWidth());
					this.grass.setY(i * this.grass.getHeight());
					this.grass.draw(canvas);
				}
			}
		
			// Drawing game objects like GRASS, COIN, TREE, OBSTICAL
			for(int i=0; i<this.gameObjects.size(); i++)
			{
				//Log.i("Sprite name = " + gameObjects.get(i).getName(), "Sprite co-ordinates = (" + gameObjects.get(i).getX() + ", " + gameObjects.get(i).getY() + ")");
				gameObjects.get(i).draw(canvas);
			}
		
			// Drawing Dominos Knight
			dominosKnight.draw(canvas);
			this.up.draw(canvas);
			this.down.draw(canvas);
			this.right.draw(canvas);
			this.left.draw(canvas);
			if(this.isMusicOn)
			{
				this.music.setTexture(this.musicOn);
				this.music.draw(canvas);
			}
			else
			{
				this.music.setTexture(this.musicOff);
				this.music.draw(canvas);
			}
			Paint paint = new Paint();
			paint.setColor(Color.CYAN);
			paint.setAntiAlias(true);
			paint.setFakeBoldText(true);
			paint.setStyle(Style.FILL_AND_STROKE);
			paint.setTextSize((float) 15.0);
			paint.setTypeface(Typeface.SANS_SERIF);
			canvas.drawText("Score: " + GameStartingView.Score, 15, 15, paint);
			paint.setColor(Color.YELLOW);
			canvas.drawText("Level: " + this.level, this.width - 60, 15, paint);
			paint.setColor(Color.CYAN);
			canvas.drawText("Life: " + this.life, this.width / 2 - 20, 15, paint);
		}
		else if(life >= 0 && this.level >= 4)
		{
			Paint paint = new Paint();
			canvas.drawColor(Color.BLACK);
			paint.setColor(Color.GREEN);
			paint.setAntiAlias(true);
			paint.setStyle(Style.FILL_AND_STROKE);
			paint.setTextSize((float) 20.0);
			paint.setTypeface(Typeface.SANS_SERIF);
			canvas.drawText("Congratulations!  :-)", 10, 80, paint);
			canvas.drawText("You have end the game!", 10, 130, paint);
			canvas.drawText("Your Score is " + GameStartingView.Score, 10, 180, paint);
			canvas.drawText("Press back key to exit", 10, 230, paint);
		}
		else if(this.life < 0)
		{
			Paint paint = new Paint();
			canvas.drawColor(Color.BLACK);
			paint.setColor(Color.RED);
			paint.setAntiAlias(true);
			paint.setStyle(Style.FILL_AND_STROKE);
			paint.setTextSize((float) 20.0);
			paint.setTypeface(Typeface.SANS_SERIF);
			canvas.drawText("OHHHH! Game Over! :-(", 10, 80, paint);
			canvas.drawText("You have Died!", 10, 130, paint);
			canvas.drawText("Your Score is " + GameStartingView.Score, 10, 180, paint);
			canvas.drawText("Press back key to exit", 10, 230, paint);
		}
	}

	public void update(Canvas canvas)
	{
		// Collision Detection
		for (int i=0; i<gameObjects.size(); i++)
		{
			// Disappearing Coin and show Grass there
			Sprites tmp = gameObjects.get(i);
				if(tmp.getName() == GameElements.COIN)
				{
						// First condition of Dominos intersection
						if((dominosKnight.getX() >= tmp.getX()
						&& dominosKnight.getY() >= tmp.getY()
						&& dominosKnight.getX() <= tmp.getWidth() + tmp.getX()
						&& dominosKnight.getY() <= tmp.getHeight() + tmp.getY())
						// Second condition of Dominos intersection
						||(dominosKnight.getX() + dominosKnight.getWidth() >= tmp.getX()
						&& dominosKnight.getY() >= tmp.getY()
						&& dominosKnight.getX() + dominosKnight.getWidth() <= tmp.getX() + tmp.getWidth()
						&& dominosKnight.getY() <= tmp.getY() + tmp.getHeight())
						// Third condition of Dominos intersection
						||(dominosKnight.getX() >= tmp.getX()
						&& dominosKnight.getY() + dominosKnight.getHeight() >= tmp.getY()
						&& dominosKnight.getX() <= tmp.getX() + tmp.getWidth()
						&& dominosKnight.getY() + dominosKnight.getHeight() <= tmp.getY() + tmp.getHeight())
						// Fourth condition of Dominos intersection
						||(dominosKnight.getX() + dominosKnight.getWidth() >= tmp.getX()
						&& dominosKnight.getY() + dominosKnight.getHeight() >= tmp.getY()
						&& dominosKnight.getX() + dominosKnight.getWidth() <= tmp.getX() + tmp.getWidth()
						&& dominosKnight.getY() + dominosKnight.getHeight() <= tmp.getY() + tmp.getHeight())
						// First condition of Coin intersection
						&& (tmp.getX() >= dominosKnight.getX()
						&& tmp.getY() >= dominosKnight.getY()
						&& tmp.getX() <= dominosKnight.getX() + dominosKnight.getWidth()
						&& tmp.getY() <= dominosKnight.getY() + dominosKnight.getHeight())
						// Second condition of Coin intersection
						||(tmp.getX() + tmp.getWidth() >= dominosKnight.getX()
						&& tmp.getY() >= dominosKnight.getY()
						&& tmp.getX() + tmp.getWidth() <= dominosKnight.getX() + dominosKnight.getWidth()
						&& tmp.getY() <= dominosKnight.getY() + dominosKnight.getHeight())
						// Third condition of Coin intersection
						||(tmp.getX() >= dominosKnight.getX()
						&& tmp.getY() + tmp.getHeight() >= dominosKnight.getY()
						&& tmp.getX() <= dominosKnight.getX() + dominosKnight.getWidth()
						&& tmp.getY() + tmp.getHeight() <= dominosKnight.getY() + dominosKnight.getHeight())
						// Fourth condition of Coin intersection
						||(tmp.getX() + tmp.getWidth() >= dominosKnight.getX()
						&& tmp.getY() + tmp.getHeight() >= dominosKnight.getY()
						&& tmp.getX() + tmp.getWidth() <= dominosKnight.getX() + dominosKnight.getWidth()
						&& tmp.getY() + tmp.getHeight() <= dominosKnight.getY() + dominosKnight.getHeight())
						)
						{
							this.grass.setX(tmp.getX());
							this.grass.setY(tmp.getY());
							this.grass.draw(canvas);
							gameObjects.remove(tmp);
							this.numOfCoins--;
							GameStartingView.Score += 10;
						}
				}				
		}
	}
	
	
	
	public void onTouch(MotionEvent event)
	{
		//Log.i("Touch co-ordinates are ", " (" + event.getX() + ", " + event.getY() + ")");
		// Up arrow key pressed
		if(event.getX() >= this.up.getX() && event.getY() >= this.up.getY() && event.getX() <= this.up.getWidth() + this.up.getX()
				&& event.getY() <= this.up.getHeight() + this.up.getY())
		{
			Log.i("Up arrow key pressed!","Up arrow key pressed!");
			if(dominosKnight.getY() - dominosKnight.getSpeedY() >= 0)
			{
				dominosKnight.setY(dominosKnight.getY() - dominosKnight.getSpeedY());
				if(isCollidedWithObstical()){
					dominosKnight.setY(dominosKnight.getY() + dominosKnight.getSpeedY());
					return;
				}
				dominosKnight.setTexture(upDomino);
			}
		}
		// Down arrow key pressed
		if(event.getX() >= this.down.getX() && event.getY() >= this.down.getY() && event.getX() <= this.down.getWidth() + this.down.getX()
				&& event.getY() <= this.down.getHeight() + this.down.getY())
		{
			Log.i("Down arrow key pressed!","Down arrow key pressed!");
			if(dominosKnight.getY() + dominosKnight.getSpeedY() <= this.getHeight()){
				dominosKnight.setY(dominosKnight.getY() + dominosKnight.getSpeedY());
				if(isCollidedWithObstical())
				{
					dominosKnight.setY(dominosKnight.getY() - dominosKnight.getSpeedY());
					return;
				}
				dominosKnight.setTexture(downDomino);
			}
			
		}
		// Right arrow key pressed
		if(event.getX() >= this.right.getX() && event.getY() >= this.right.getY() && event.getX() <= this.right.getWidth() + this.right.getX()
				&& event.getY() <= this.right.getHeight() + this.right.getY())
		{
			Log.i("Right arrow key pressed!","Right arrow key pressed!");
			if(dominosKnight.getX() + dominosKnight.getSpeedX() <= this.getWidth()){
				dominosKnight.setX(dominosKnight.getX() + dominosKnight.getSpeedX());
				if(isCollidedWithObstical())
				{
					dominosKnight.setX(dominosKnight.getX() - dominosKnight.getSpeedX());
					return;
				}
				dominosKnight.setTexture(rightDomino);
			}
		}
		// Left arrow key pressed
		if(event.getX() >= this.left.getX() && event.getY() >= this.left.getY() && event.getX() <= this.left.getWidth() + this.left.getX()
				&& event.getY() <= this.left.getHeight() + this.left.getY())
		{
			Log.i("Left arrow key pressed!","Left arrow key pressed!");
			if(dominosKnight.getX() - dominosKnight.getSpeedX() >= 0){
				dominosKnight.setX(dominosKnight.getX() - dominosKnight.getSpeedX());
				if(isCollidedWithObstical())
				{
					dominosKnight.setX(dominosKnight.getX() + dominosKnight.getSpeedX());
					return;
				}
				dominosKnight.setTexture(leftDomino);
			}
			
		}
		// Music Button Press
		if(event.getX() >= this.music.getX() && event.getY() >= this.music.getY() && event.getX() <= this.music.getX() + this.music.getWidth()
				&& event.getY() <= this.music.getY() + this.music.getHeight())
		{
			Log.i("Music Button pressed!","Music Button pressed!");
			this.isMusicOn = ! this.isMusicOn;
			Log.i("this.isMusicOn ------>","------------->" + this.isMusicOn);
			if(this.isMusicOn)
			{
				MainMenuActivity.mediaPlayer = MediaPlayer.create(context, R.raw.in_game_audio);
				MainMenuActivity.mediaPlayer.setLooping(true);
				MainMenuActivity.mediaPlayer.start();
				return;
			}
			else
			{
				MainMenuActivity.mediaPlayer.stop();
				MainMenuActivity.mediaPlayer.release();
				MainMenuActivity.mediaPlayer = null;
				return;
			}
		}
	}
	
	private boolean isCollidedWithObstical() {
		for(int i=0; i<gameObjects.size(); i++)
		{
			Sprites tmp = gameObjects.get(i);
			if(tmp.getName() == GameElements.OBSTICAL || tmp.getName() == GameElements.TREE)   
			{
					// First condition of Dominos intersection
					if ((dominosKnight.getX() >= tmp.getX()
					&& dominosKnight.getY() >= tmp.getY()
					&& dominosKnight.getX() <= tmp.getWidth() + tmp.getX()
					&& dominosKnight.getY() <= tmp.getHeight() + tmp.getY())
					// Second condition of Dominos intersection
					||(dominosKnight.getX() + dominosKnight.getWidth() >= tmp.getX()
					&& dominosKnight.getY() >= tmp.getY()
					&& dominosKnight.getX() + dominosKnight.getWidth() <= tmp.getX() + tmp.getWidth()
					&& dominosKnight.getY() <= tmp.getY() + tmp.getHeight())
					// Third condition of Dominos intersection
					||(dominosKnight.getX() >= tmp.getX()
					&& dominosKnight.getY() + dominosKnight.getHeight() >= tmp.getY()
					&& dominosKnight.getX() <= tmp.getX() + tmp.getWidth()
					&& dominosKnight.getY() + dominosKnight.getHeight() <= tmp.getY() + tmp.getHeight())
					// Fourth condition of Dominos intersection
					||(dominosKnight.getX() + dominosKnight.getWidth() >= tmp.getX()
					&& dominosKnight.getY() + dominosKnight.getHeight() >= tmp.getY()
					&& dominosKnight.getX() + dominosKnight.getWidth() <= tmp.getX() + tmp.getWidth()
					&& dominosKnight.getY() + dominosKnight.getHeight() <= tmp.getY() + tmp.getHeight())
					// First condition of Tmp intersection
					&& (tmp.getX() >= dominosKnight.getX()
					&& tmp.getY() >= dominosKnight.getY()
					&& tmp.getX() <= dominosKnight.getX() + dominosKnight.getWidth()
					&& tmp.getY() <= dominosKnight.getY() + dominosKnight.getHeight())
					// Second condition of Tmp intersection
					||(tmp.getX() + tmp.getWidth() >= dominosKnight.getX()
					&& tmp.getY() >= dominosKnight.getY()
					&& tmp.getX() + tmp.getWidth() <= dominosKnight.getX() + dominosKnight.getWidth()
					&& tmp.getY() <= dominosKnight.getY() + dominosKnight.getHeight())
					// Third condition of Tmp intersection
					||(tmp.getX() >= dominosKnight.getX()
					&& tmp.getY() + tmp.getHeight() >= dominosKnight.getY()
					&& tmp.getX() <= dominosKnight.getX() + dominosKnight.getWidth()
					&& tmp.getY() + tmp.getHeight() <= dominosKnight.getY() + dominosKnight.getHeight())
					// Fourth condition of Tmp intersection
					||(tmp.getX() + tmp.getWidth() >= dominosKnight.getX()
					&& tmp.getY() + tmp.getHeight() >= dominosKnight.getY()
					&& tmp.getX() + tmp.getWidth() <= dominosKnight.getX() + dominosKnight.getWidth()
					&& tmp.getY() + tmp.getHeight() <= dominosKnight.getY() + dominosKnight.getHeight())
					)	
					{
						this.life --;
						return true;
					}
						
			}
			if(tmp.getName() == GameElements.FIRE)   
			{
					// First condition of Dominos intersection
					if ((dominosKnight.getX() >= tmp.getX()
					&& dominosKnight.getY() >= tmp.getY()
					&& dominosKnight.getX() <= tmp.getWidth() + tmp.getX()
					&& dominosKnight.getY() <= tmp.getHeight() + tmp.getY())
					// Second condition of Dominos intersection
					||(dominosKnight.getX() + dominosKnight.getWidth() >= tmp.getX()
					&& dominosKnight.getY() >= tmp.getY()
					&& dominosKnight.getX() + dominosKnight.getWidth() <= tmp.getX() + tmp.getWidth()
					&& dominosKnight.getY() <= tmp.getY() + tmp.getHeight())
					// Third condition of Dominos intersection
					||(dominosKnight.getX() >= tmp.getX()
					&& dominosKnight.getY() + dominosKnight.getHeight() >= tmp.getY()
					&& dominosKnight.getX() <= tmp.getX() + tmp.getWidth()
					&& dominosKnight.getY() + dominosKnight.getHeight() <= tmp.getY() + tmp.getHeight())
					// Fourth condition of Dominos intersection
					||(dominosKnight.getX() + dominosKnight.getWidth() >= tmp.getX()
					&& dominosKnight.getY() + dominosKnight.getHeight() >= tmp.getY()
					&& dominosKnight.getX() + dominosKnight.getWidth() <= tmp.getX() + tmp.getWidth()
					&& dominosKnight.getY() + dominosKnight.getHeight() <= tmp.getY() + tmp.getHeight())
					// First condition of Tmp intersection
					&& (tmp.getX() >= dominosKnight.getX()
					&& tmp.getY() >= dominosKnight.getY()
					&& tmp.getX() <= dominosKnight.getX() + dominosKnight.getWidth()
					&& tmp.getY() <= dominosKnight.getY() + dominosKnight.getHeight())
					// Second condition of Tmp intersection
					||(tmp.getX() + tmp.getWidth() >= dominosKnight.getX()
					&& tmp.getY() >= dominosKnight.getY()
					&& tmp.getX() + tmp.getWidth() <= dominosKnight.getX() + dominosKnight.getWidth()
					&& tmp.getY() <= dominosKnight.getY() + dominosKnight.getHeight())
					// Third condition of Tmp intersection
					||(tmp.getX() >= dominosKnight.getX()
					&& tmp.getY() + tmp.getHeight() >= dominosKnight.getY()
					&& tmp.getX() <= dominosKnight.getX() + dominosKnight.getWidth()
					&& tmp.getY() + tmp.getHeight() <= dominosKnight.getY() + dominosKnight.getHeight())
					// Fourth condition of Tmp intersection
					||(tmp.getX() + tmp.getWidth() >= dominosKnight.getX()
					&& tmp.getY() + tmp.getHeight() >= dominosKnight.getY()
					&& tmp.getX() + tmp.getWidth() <= dominosKnight.getX() + dominosKnight.getWidth()
					&& tmp.getY() + tmp.getHeight() <= dominosKnight.getY() + dominosKnight.getHeight())
					)	
					{
						this.life -= 2;
						Log.i("Collided with ----->", "-------> FIRE");
						return true;
					}
			}
			
		}
		return false;
	}

	
	/**
	 * @return the surfaceHolder
	 */
	public SurfaceHolder getSurfaceHolder() {
		return surfaceHolder;
	}


	/**
	 * @param surfaceHolder the surfaceHolder to set
	 */
	public void setSurfaceHolder(SurfaceHolder surfaceHolder) {
		this.surfaceHolder = surfaceHolder;
	}


	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}


	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}


	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the gameObjects
	 */
	public ArrayList <Sprites> getGameObjects() {
		return gameObjects;
	}

	/**
	 * @param gameObjects the gameObjects to set
	 */
	public void setGameObjects(ArrayList <Sprites> gameObjects) {
		this.gameObjects = gameObjects;
	}

	/**
	 * @return the grass
	 */
	public Sprites getGrass() {
		return grass;
	}

	/**
	 * @param grass the grass to set
	 */
	public void setGrass(Sprites grass) {
		this.grass = grass;
	}

	/**
	 * @return the numOfCoins
	 */
	public int getNumOfCoins() {
		return numOfCoins;
	}

	/**
	 * @param numOfCoins the numOfCoins to set
	 */
	public void setNumOfCoins(int numOfCoins) {
		this.numOfCoins = numOfCoins;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}
	
	/**
	 * @return the life
	 */
	public int getLife() {
		return life;
	}

	/**
	 * @param life the life to set
	 */
	public void setLife(int life) {
		this.life = life;
	}

	/**
	 * @return the leftDomino
	 */
	public Bitmap getLeftDomino() {
		return leftDomino;
	}

	/**
	 * @param leftDomino the leftDomino to set
	 */
	public void setLeftDomino(Bitmap leftDomino) {
		this.leftDomino = leftDomino;
	}

	/**
	 * @return the rightDomino
	 */
	public Bitmap getRightDomino() {
		return rightDomino;
	}

	/**
	 * @param rightDomino the rightDomino to set
	 */
	public void setRightDomino(Bitmap rightDomino) {
		this.rightDomino = rightDomino;
	}

	/**
	 * @return the upDomino
	 */
	public Bitmap getUpDomino() {
		return upDomino;
	}

	/**
	 * @param upDomino the upDomino to set
	 */
	public void setUpDomino(Bitmap upDomino) {
		this.upDomino = upDomino;
	}

	/**
	 * @return the downDomino
	 */
	public Bitmap getDownDomino() {
		return downDomino;
	}

	/**
	 * @param downDomino the downDomino to set
	 */
	public void setDownDomino(Bitmap downDomino) {
		this.downDomino = downDomino;
	}

	/**
	 * @return the isMusicOn
	 */
	public boolean isMusicOn() {
		return isMusicOn;
	}

	/**
	 * @param isMusicOn the isMusicOn to set
	 */
	public void setMusicOn(boolean isMusicOn) {
		this.isMusicOn = isMusicOn;
	}

	/**
	 * @return the music
	 */
	public Sprites getMusic() {
		return music;
	}

	/**
	 * @param music the music to set
	 */
	public void setMusic(Sprites music) {
		this.music = music;
	}

	/**
	 * @return the musicOff
	 */
	public Bitmap getMusicOff() {
		return musicOff;
	}

	/**
	 * @param musicOff the musicOff to set
	 */
	public void setMusicOff(Bitmap musicOff) {
		this.musicOff = musicOff;
	}

	/**
	 * @return the musicOn
	 */
	public Bitmap getMusicOn() {
		return musicOn;
	}

	/**
	 * @param musicOn the musicOn to set
	 */
	public void setMusicOn(Bitmap musicOn) {
		this.musicOn = musicOn;
	}

}
