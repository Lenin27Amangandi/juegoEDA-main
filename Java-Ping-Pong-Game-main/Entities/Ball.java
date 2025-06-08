package Entities;
import java.awt.*;
import java.util.*;
import javax.swing.*;

public class Ball extends Rectangle {

	public Random random;
	public int xVelocity;
	public int yVelocity;
	public int initialSpeed = 2;
	public Image ballImage;

	public Ball(int x, int y, int width, int height) {
		super(x, y, width, height);
		random = new Random();

		int randomXDirection = random.nextInt(2);
		if (randomXDirection == 0)
			randomXDirection--;
		setXDirection(randomXDirection * initialSpeed);

		int randomYDirection = random.nextInt(2);
		if (randomYDirection == 0)
			randomYDirection--;
		setYDirection(randomYDirection * initialSpeed);

		ballImage = new ImageIcon("Java-Ping-Pong-Game-main/Utilies/EsferaDelDragon.png")
				.getImage()
				.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
	}

	public void setXDirection(int xDirection) {
		this.xVelocity = xDirection;
	}

	public void setYDirection(int yDirection) {
		this.yVelocity = yDirection;
	}

	public int getXVelocity() {
		return xVelocity;
	}

	public int getYVelocity() {
		return yVelocity;
	}

	public int getXPosition() {
		return x;
	}

	public int getYPosition() {
		return y;
	}

	public int getBallWidth() {
		return width;
	}

	public int getBallHeight() {
		return height;
	}

	public void move() {
		x += xVelocity;
		y += yVelocity;
	}

	public void draw(Graphics g) {
		g.drawImage(ballImage, x, y, null);
	}
}

