package GUI;

import BL.Score;
import BL.ScoreManager;
import Entities.Ball;
import Entities.Paddle;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable {

    static final int GAME_WIDTH = 1000;
    static final int GAME_HEIGHT = (int)(GAME_WIDTH * 0.5555);
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    static final int BALL_DIAMETER = 50;
    static final int PADDLE_WIDTH = 25;
    static final int PADDLE_HEIGHT = 100;

    Thread gameThread;
    Paddle paddle1;
    Paddle paddle2;
    Ball ball;
    Score score;

    private GameFrame parentFrame;

    private String player1Name = "Jugador 1";
    private String player2Name = "Jugador 2";

    Image backgroundImage;

    public GamePanel(GameFrame frame) {
        this.parentFrame = frame;

        backgroundImage = new ImageIcon("Java-Ping-Pong-Game-main/Utilies/Fondo.jpg").getImage()
                .getScaledInstance(GAME_WIDTH, GAME_HEIGHT, Image.SCALE_SMOOTH);
        newPaddles();
        newBall();
        score = new Score(GAME_WIDTH, GAME_HEIGHT);
        this.setFocusable(true);
        this.addKeyListener(new AL());
        this.setPreferredSize(SCREEN_SIZE);
            this.addKeyListener(new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                endGameManually(); 
            }
        }
    });
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void newBall() {
        Random random = new Random();
        ball = new Ball((GAME_WIDTH / 2) - (BALL_DIAMETER / 2),
                random.nextInt(GAME_HEIGHT - BALL_DIAMETER),
                BALL_DIAMETER, BALL_DIAMETER);
    }

    public void newPaddles() {
        paddle1 = new Paddle(0, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2),
                PADDLE_WIDTH, PADDLE_HEIGHT, 1);
        paddle2 = new Paddle(GAME_WIDTH - PADDLE_WIDTH, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2),
                PADDLE_WIDTH, PADDLE_HEIGHT, 2);
    }

    public void setPlayerNames(String player1, String player2) {
        this.player1Name = player1;
        this.player2Name = player2;
    }

    public void draw(Graphics g) {
        paddle1.draw(g);
        paddle2.draw(g);
        ball.draw(g);
        score.draw(g);

        g.setFont(new Font("Consolas", Font.PLAIN, 20));
        g.setColor(Color.WHITE);
        g.drawString(player1Name, GAME_WIDTH / 2 - 85, 90);
        g.drawString(player2Name, GAME_WIDTH / 2 + 20, 90);
    }

    public void move() {
        paddle1.move();
        paddle2.move();
        ball.move();
    }

    public void checkCollision() {
        if (ball.y <= 0) ball.setYDirection(-ball.yVelocity);
        if (ball.y >= GAME_HEIGHT - BALL_DIAMETER) ball.setYDirection(-ball.yVelocity);

        if (ball.intersects(paddle1)) {
            ball.xVelocity = Math.abs(ball.xVelocity) + 1;
            ball.yVelocity += (ball.yVelocity > 0) ? 1 : -1;
            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }

        if (ball.intersects(paddle2)) {
            ball.xVelocity = -Math.abs(ball.xVelocity) - 1;
            ball.yVelocity += (ball.yVelocity > 0) ? 1 : -1;
            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }

        if (paddle1.y <= 0) paddle1.y = 0;
        if (paddle1.y >= GAME_HEIGHT - PADDLE_HEIGHT) paddle1.y = GAME_HEIGHT - PADDLE_HEIGHT;
        if (paddle2.y <= 0) paddle2.y = 0;
        if (paddle2.y >= GAME_HEIGHT - PADDLE_HEIGHT) paddle2.y = GAME_HEIGHT - PADDLE_HEIGHT;

        if (ball.x <= 0) {
            score.player2++;
            newPaddles();
            newBall();
        }

        if (ball.x >= GAME_WIDTH - BALL_DIAMETER) {
            score.player1++;
            newPaddles();
            newBall();
        }
    }

    public void endGameManually() {
      if (this.gameThread != null && this.gameThread.isAlive()) {
        this.gameThread.interrupt();
    }
    JOptionPane.showMessageDialog(this, "La partida ha terminado manualmente.\n" + this.player1Name + ": " + this.score.player1 + "\n" + this.player2Name + ": " + this.score.player2);

    if (this.score.player1 != this.score.player2) {
        String winner = this.score.player1 > this.score.player2 ? this.player1Name : this.player2Name;
        String loser = this.score.player1 > this.score.player2 ? this.player2Name : this.player1Name;
        int winnerScore = Math.max(this.score.player1, this.score.player2);
        int loserScore = Math.min(this.score.player1, this.score.player2);
    
        ScoreManager.saveScore(winner, winnerScore, loser, loserScore);
    } else {
        ScoreManager.saveScore(this.player1Name, this.score.player1, this.player2Name, this.score.player2);
    }
    this.parentFrame.closeGameAndShowMenu();
    }

    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1_000_000_000 / amountOfTicks;
        double delta = 0;

        while (!Thread.currentThread().isInterrupted()) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            if (delta >= 1) {
                move();
                checkCollision();
                repaint();
                delta--;
            }
        }
    }

    public class AL extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            paddle1.keyPressed(e);
            paddle2.keyPressed(e);
        }

        public void keyReleased(KeyEvent e) {
            paddle1.keyReleased(e);
            paddle2.keyReleased(e);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        draw(g);
    }
}
