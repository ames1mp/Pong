package com.meatpaw;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameScreen extends Canvas {

	private BufferStrategy strategy;
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private ArrayList<Entity> removeList = new ArrayList<Entity>();

	private boolean upPressed;
	private boolean downPressed;
	private boolean waitingForKeyPress = true;
	private boolean gameRunning = true;

	public static final float BALL_MOVE_SPEED = 300;
	public static final float PADDLE_MOVE_SPEED = 300;
	private int p1Score = 0;
	private int p2Score = 0;

	private BallEntity ball;
	private PaddleEntity rightPaddle;
	private PaddleEntity leftPaddle;
	
	private static Font monoFont = new Font("Monospaced", Font.BOLD, 36);

	public GameScreen() {

		JFrame container = new JFrame("Meatpaw Games Presents: Pong");

		JPanel panel = (JPanel) container.getContentPane();
		panel.setPreferredSize(new Dimension(800, 600));
		panel.setLayout(null);

		setBounds(0, 0, 800, 600);
		panel.add(this);

		setIgnoreRepaint(true);

		container.pack();
		container.setResizable(false);
		container.setVisible(true);

		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		addKeyListener(new KeyInputHandler());

		requestFocus();

		createBufferStrategy(2);
		strategy = getBufferStrategy();

		initEntities();
	}

	private void startGame() {
		entities.clear();
		initEntities();
		ball.setHorizontalMovement(BALL_MOVE_SPEED);
		ball.setVerticalMovement(BALL_MOVE_SPEED +(float)Math.random());

		upPressed = false;
		downPressed = false;
	}

	private void initEntities() {
		ball = new BallEntity(this, "res/ball.png", 400 - SpriteStore.get()
				.getSprite("res/ball.png").getWidth() / 2, 300 - SpriteStore
				.get().getSprite("res/ball.png").getHeight() / 2);
		entities.add(ball);

		leftPaddle = new PaddleEntity(
				this,
				"res/paddle.png",
				0 + SpriteStore.get().getSprite("res/paddle.png").getWidth(),
				300 - SpriteStore.get().getSprite("res/paddle.png").getHeight() / 2);
		entities.add(leftPaddle);

		rightPaddle = new PaddleEntity(
				this,
				"res/paddle.png",
				800 - (SpriteStore.get().getSprite("res/paddle.png").getWidth()) * 2,
				300 - SpriteStore.get().getSprite("res/paddle.png").getHeight() / 2);
		entities.add(rightPaddle);

	}

	public void gameLoop() {
		long lastLoopTime = System.currentTimeMillis();

		while (gameRunning) {
			long delta = System.currentTimeMillis() - lastLoopTime;
			lastLoopTime = System.currentTimeMillis();

			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.setColor(Color.black);
			g.fillRect(0, 0, 800, 600);

			if (!waitingForKeyPress) {
				for (int i = 0; i < entities.size(); i++) {
					Entity entity = (Entity) entities.get(i);
					entity.move(delta);

				}
			}

			for (int i = 0; i < entities.size(); i++) {
				Entity entity = (Entity) entities.get(i);
				entity.draw(g);
			}

			g.setFont(monoFont);
			FontMetrics fm = g.getFontMetrics();
			int w = fm.stringWidth(String.valueOf(p1Score));
			int h = fm.getAscent();
			g.setColor(Color.green);
			g.drawString(String.valueOf(p1Score), 100, 100);
			g.drawString(String.valueOf(p2Score), 700, 100);
			
			
			g.dispose();
			strategy.show();

			rightPaddle.setVerticalMovement(0);
			if (upPressed)
				rightPaddle.setVerticalMovement(-PADDLE_MOVE_SPEED);
			if (downPressed)
				rightPaddle.setVerticalMovement(PADDLE_MOVE_SPEED);

			for (int p = 0; p < entities.size(); p++) {
				for (int s = p + 1; s < entities.size(); s++) {
					Entity me = (Entity) entities.get(p);
					Entity him = (Entity) entities.get(s);

					if (me.collidesWith(him)) {
						me.collidedWith(him);
						him.collidedWith(me);
					}
				}
			}

			if (ball.getX() > 800) {
				p1Score += 1;
				
				ball.resetBall("p1");
				
			
			}

			if (ball.getX() < 0) {
				p2Score += 1;
				
				ball.resetBall("p2");
			}
			
			if (ball.getY() < 0) {
				ball.hitCeiling();
			}
			
			if (ball.getY() > 600 - SpriteStore.get().getSprite("res/ball.png").getHeight()) {
				ball.hitFloor();
			}
			
			

			try {
				Thread.sleep(10);
			} catch (Exception e) {
			}
		}
	}

	private class KeyInputHandler extends KeyAdapter {
		private int pressCount = 1;

		@Override
		public void keyPressed(KeyEvent e) {
			if (waitingForKeyPress) {
				return;
			}

			if (e.getKeyCode() == KeyEvent.VK_W) {
				upPressed = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_S) {
				downPressed = true;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (waitingForKeyPress) {
				return;
			}

			if (e.getKeyCode() == KeyEvent.VK_W) {
				upPressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_S) {
				downPressed = false;
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			if (waitingForKeyPress) {
				if (pressCount == 1) {

					waitingForKeyPress = false;
					pressCount = 0;
					startGame();

				} else {
					pressCount++;
				}
			}
		}
	}

	public static void main(String[] args) {
		GameScreen g = new GameScreen();
		g.initEntities();
		g.gameLoop();
	}

}
