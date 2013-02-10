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
		this.setTexture(texture);
		this.name = name;
		this.height = this.getTexture().getHeight();
		this.width = this.getTexture().getWidth();
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
	    canvas.drawBitmap(getTexture(), x, y, null);
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

	/**
	 * @return the texture
	 */
	public Bitmap getTexture() {
		return texture;
	}

	/**
	 * @param texture the texture to set
	 */
	public void setTexture(Bitmap texture) {
		this.texture = texture;
	}

}
