/**
 * 
 */
package com.pucit.andriodtermproject;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * @author Amjad Afzaal
 *
 */
public class Sprites {
	
	private int x, y, width, height;
	private Bitmap texture;
	private GameElements name;
	
	public Sprites(Bitmap texture, GameElements name)
	{
		this.texture = texture;
		this.name = name;
		this.height = this.texture.getHeight();
		this.width = this.texture.getWidth();
	}
	
	public int getWidth()
	{
		return this.width;
	}
	
	public int getHeight()
	{
		return this.height;
	}
	
	public void draw(Canvas canvas) 
	{
	    canvas.drawBitmap(texture, x, y, null);
	}

	
	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	
	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the name
	 */
	public GameElements getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(GameElements name) {
		this.name = name;
	}

}
