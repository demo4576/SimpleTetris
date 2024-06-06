package main;

import mino.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class PlayManager {
    final int WIDTH = 360;
    final int HEIGHT = 600;
    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;

    Mino currentMino;
    final int MINO_START_X;
    final int MINO_START_Y;

    Mino nextMino;
    final int NEXT_MINO_X;
    final int NEXT_MINO_Y;
    public static ArrayList<Block> staticBlocks = new ArrayList<>();

    public static int dropInterval = 60;

    private boolean effectCounterOn;
    private int effectCounter;
    private ArrayList<Integer> effectY =  new ArrayList<>();

    public boolean gameOver;

    int level = 1;
    int score;
    int lines;

    public PlayManager() {
        left_x = (GamePanel.WIDTH/2) - (WIDTH/2);
        right_x = left_x + WIDTH;
        top_y = 50;
        bottom_y = top_y + HEIGHT;

        MINO_START_X = left_x + (WIDTH/2) - Block.SIZE;
        MINO_START_Y = top_y + Block.SIZE;

        NEXT_MINO_X = right_x + 175;
        NEXT_MINO_Y = top_y + 500;

        currentMino = pickMino();
        currentMino.setXY(MINO_START_X, MINO_START_Y);

        nextMino = pickMino();
        nextMino.setXY(NEXT_MINO_X, NEXT_MINO_Y);
    }

    private Mino pickMino() {
        Mino mino = null;
        int i = new Random().nextInt(7);

        mino = switch (i) {
            case 0 -> new Mino_L1();
            case 1 -> new Mino_L2();
            case 2 -> new Mino_Square();
            case 3 -> new Mino_Bar();
            case 4 -> new Mino_T();
            case 5 -> new Mino_Z1();
            case 6 -> new Mino_Z2();
            default -> mino;
        };
        return mino;
    }

    public void update() {
        if(currentMino.isActive) {
            currentMino.update();
        }
        else {
            Collections.addAll(staticBlocks, currentMino.b);

            if(currentMino.b[0].x == MINO_START_X && currentMino.b[0].y == MINO_START_Y) {
                gameOver = true;
                GamePanel.music.stop();
                GamePanel.soundEffect.play(2, false);
            }

            currentMino.deactivating = false;

            currentMino = nextMino;
            currentMino.setXY(MINO_START_X, MINO_START_Y);

            nextMino = pickMino();
            nextMino.setXY(NEXT_MINO_X, NEXT_MINO_Y);

            checkDelete();
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x-4, top_y-4, WIDTH+8, HEIGHT+8);

        int x = right_x + 100;
        int y = bottom_y - 200;
        g2.drawRect(x, y, 200, 200);
        g2.setFont(new Font("Courier New", Font.PLAIN, 20));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("next", x+70, y+40);

        g2.drawRect(x, top_y, 200, 300);
        x += 40;
        y = top_y + 90;
        g2.drawString(STR."level: \{level}", x, y);
        y += 70;
        g2.drawString(STR."lines: \{lines}", x, y);
        y += 70;
        g2.drawString(STR."scores: \{score}", x, y);

        if(currentMino != null) {
            currentMino.draw(g2);
        }

        nextMino.draw(g2);

        for(Block b : staticBlocks) b.draw(g2);

        if(effectCounterOn) {
            effectCounter++;

            g2.setColor(Color.red);

            for (int y1 : effectY) g2.fillRect(left_x, y1, WIDTH, Block.SIZE);

            if(effectCounter == 10) {
                effectCounterOn = false;
                effectCounter = 0;
                effectY.clear();
            }
        }

        g2.setColor(Color.red);
        g2.setFont(g2.getFont().deriveFont(30f));
        if(gameOver) {
            x = 180;
            y = top_y + 320;
            g2.drawString("lol gg", x, y);
        }
        if(KeyHandler.pausePressed) {
            x = 105;
            y = top_y + 320;
            g2.drawString("don't stop now!", x, y);
        }

        x = 150;
        y = top_y + 100;
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(50f));
        g2.drawString("simple", x, y);
        g2.drawString("tetris", x, y+80);

        y = bottom_y - 100;
        g2.drawString("by", x+55, y);
        g2.drawString("dehish", x, y+80);
    }

    private void checkDelete() {
        int x = left_x;
        int y = top_y;
        int blockCount = 0;
        int lineCount = 0;

        while (x < right_x && y < bottom_y) {
            for(Block b : staticBlocks) if(b.x == x && b.y == y) blockCount++;

            x += Block.SIZE;

            if(x == right_x) {
                if(blockCount == 12) {
                    effectCounterOn = true;
                    effectY.add(y);

                    for(int i = staticBlocks.size()-1; i >= 0; i--)
                        if(staticBlocks.get(i).y == y) staticBlocks.remove(i);

                    lineCount++;
                    lines++;
                    if(lines % 10 == 0 && dropInterval > 1) {
                        level++;
                        if(dropInterval > 10) dropInterval -= 10;
                        else dropInterval--;
                    }
                    for(Block b : staticBlocks) if(b.y < y) b.y += Block.SIZE;
                }
                x = left_x;
                y += Block.SIZE;
                blockCount = 0;
            }
        }

        if(lineCount > 0) {
            GamePanel.soundEffect.play(1, false);
            int singleLineScore = 10 * level;
            score += singleLineScore * lineCount;
        }
    }
}
