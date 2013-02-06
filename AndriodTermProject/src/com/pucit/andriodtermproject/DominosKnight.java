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
public class DominosKnight 
{
	private int x, y, speedX, speedY, width, height;
	private Bitmap texture;
	
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
	 * @return the speedX
	 */
	public int getSpeedX() {
		return speedX;
	}

	/**
	 * @param speedX the speedX to set
	 */
	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	/**
	 * @return the speedY
	 */
	public int getSpeedY() {
		return speedY;
	}

	/**
	 * @param speedY the speedY to set
	 */
	public void setSpeedY(int speedY) {
		this.speedY = speedY;
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

	public DominosKnight(Bitmap texture)
	{
		this.texture = texture;
		this.width = this.texture.getWidth();
		this.height = this.texture.getHeight();
		this.speedX = 5;
		this.speedY = 5;
	}
	
	public void draw (Canvas canvas)
	{
		canvas.drawBitmap(texture, x, y, null);
	}
}
