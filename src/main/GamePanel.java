package main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    public static final int HEIGHT = 720;
    public static final int WIDTH = 1280;
    public static final int FPS = 60;
    Thread gameThread;
    PlayManager pm;
    public static Sound music = new Sound();
    public static Sound soundEffect = new Sound();

    public GamePanel() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setLayout(null);
        this.pm = new PlayManager();
        this.addKeyListener(new KeyHandler());
        this.setFocusable(true);
    }

    public void launchGame() {
        gameThread = new Thread(this);
        gameThread.start();
        music.play(0, true);
        music.loop();
    }

    @Override
    public void run() {

        //Game_Loop
        double drawInterval = (double) 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    private void update() {
        if(!KeyHandler.pausePressed && !pm.gameOver) pm.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        pm.draw(g2);
    }
}
