package com.meatpaw;

public class BallEntity extends Entity {
	
	GameScreen gameScreen;
	
	public BallEntity(GameScreen gameScreen, String ref, int x, int y) {
		super(ref, x, y);
		
		this.gameScreen = gameScreen;
	}

	@Override
	public void collidedWith(Entity other) {
		if (other instanceof PaddleEntity) {
			
			setHorizontalMovement((float)1.05 *(dx * -1));
			setVerticalMovement((float)1.05 * (dy * -1));
			
			
		}
		
	}
	
	public void resetBall(String lastPoint) {
		
		setX(400 - SpriteStore.get().getSprite("res/ball.png").getWidth() / 2);
		setY((float)(25 + (Math.random() * 725)));
		
		if (lastPoint.equals("p1")) {
			setHorizontalMovement(-GameScreen.BALL_MOVE_SPEED);
		} else {
			setHorizontalMovement(GameScreen.BALL_MOVE_SPEED);
		}
		
		
		
		if (Math.random() > 0.5) {
			setVerticalMovement(GameScreen.BALL_MOVE_SPEED);
		} else {
			setVerticalMovement(-GameScreen.BALL_MOVE_SPEED);
		}
		
	}

	@Override
	public void move(long delta) {
		// TODO Auto-generated method stub
		super.move(delta);
	}

	public void hitCeiling() {
		this.dy = dy * -1;
		
	}

	public void hitFloor() {
		this.dy = dy * -1;
		
	}

	
	

}
