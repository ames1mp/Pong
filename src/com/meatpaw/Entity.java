package com.meatpaw;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Entity {
	
	protected float x;
	protected float y;
	protected float dx;
	protected float dy;
	
	private Rectangle me = new Rectangle();
	private Rectangle him = new Rectangle();
	
	protected Sprite sprite;
	
	public Entity(String ref, float x, float y) {
		this.sprite = SpriteStore.get().getSprite(ref); 
		this.x = x;
		this.y = y;		
	}
	
	public void move(long delta) {
		x += (delta * dx) / 1000;
		y += (delta * dy) / 1000;
	}
	
	public void setHorizontalMovement(float dx) {
		this.dx = dx;
	}
	
	public void setVerticalMovement(float dy) {
		this.dy = dy;
	}
	
	public double getHorizontalMovement() {
		return dx;
	}
	
	public double getVerticalMovement1() {
		return dy;
	}
	
	public void draw(Graphics g) {
		sprite.draw(g, (int) x, (int) y);
	}
	
	public void doLogic() {}
	
	public int getX() {
		return  (int)x;
	}
	
	public int getY() {
		return (int) y;
	}
	
	public void setX(float x) {
		this.x  = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public boolean collidesWith(Entity other) {
		me.setBounds((int) x, (int) y, sprite.getWidth(), sprite.getHeight());
		him.setBounds(other.getX(), other.getY(), other.sprite.getWidth(), other.sprite.getHeight());
		
		return me.intersects(him);
		}
		
	
	
	public abstract void collidedWith(Entity other);
}
