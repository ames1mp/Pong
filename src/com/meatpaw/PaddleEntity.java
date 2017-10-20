package com.meatpaw;

public class PaddleEntity extends Entity {
	
	GameScreen gameScreen;

	@Override
	public void move(long delta) {
		if (y <= 0) {
			y = 0;
		} 
		if ((y + sprite.getHeight() >= 600)) {
			y = 600 - sprite.getHeight();
		}
		super.move(delta);
	}

	public PaddleEntity(GameScreen gameScreen, String ref, int x, int y) {
		super(ref, x, y);
		
		this.gameScreen = gameScreen;
		
	}

	@Override
	public void collidedWith(Entity other) {
		// TODO Auto-generated method stub
		
	}

}
