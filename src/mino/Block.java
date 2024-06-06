package mino;

import java.awt.*;

public class Block extends Rectangle {
    public int x, y;
    public static final int SIZE = 30;
    public Color c;

    public Block(Color c) {
        this.c = c;
    }

    public void draw(Graphics2D g2) {
        int mar = 2;
        g2.setColor(c);
        g2.fillRect(x + mar, y + mar, SIZE - (mar * 2), SIZE - (mar * 2));
    }
}
