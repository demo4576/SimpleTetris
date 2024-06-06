package mino;

import main.GamePanel;
import main.KeyHandler;
import main.PlayManager;

import java.awt.*;

public class Mino {
    public Block[] b = new Block[4];
    public Block[] tempB = new Block[4];
    int autoDropCounter = 0;
    int direction = 1;
    boolean leftCollision, rightCollision, bottomCollision;
    public boolean isActive = true;
    public boolean deactivating;
    int deactivateCounter = 0;

    public void create(Color c) {
        b[0] = new Block(c);
        b[1] = new Block(c);
        b[2] = new Block(c);
        b[3] = new Block(c);

        tempB[0] = new Block(c);
        tempB[1] = new Block(c);
        tempB[2] = new Block(c);
        tempB[3] = new Block(c);
    }

    public void setXY(int x, int y) {}
    public void updateXY(int direction) {
        checkRotationCollision();

        if(!leftCollision && !rightCollision  && !bottomCollision) {
            this.direction = direction;
            b[0].x = tempB[0].x;
            b[1].x = tempB[1].x;
            b[2].x = tempB[2].x;
            b[3].x = tempB[3].x;
            b[0].y = tempB[0].y;
            b[1].y = tempB[1].y;
            b[2].y = tempB[2].y;
            b[3].y = tempB[3].y;
        }
    }
    public void update() {
        if(deactivating) deactivating();

        if(KeyHandler.upPressed) {
            switch (direction) {
                case 1: getDirection2();
                        break;
                case 2: getDirection3();
                        break;
                case 3: getDirection4();
                        break;
                case 4: getDirection1();
                        break;
            }

            GamePanel.soundEffect.play(3, false);

            KeyHandler.upPressed = false;
        }

        checkMovementCollision();

        if (KeyHandler.downPressed) {
            if(!bottomCollision) {
                b[0].y += Block.SIZE;
                b[1].y += Block.SIZE;
                b[2].y += Block.SIZE;
                b[3].y += Block.SIZE;

                autoDropCounter = 0;
            }

            KeyHandler.downPressed = false;
        }
        if (KeyHandler.leftPressed) {
            if(!leftCollision) {
                b[0].x -= Block.SIZE;
                b[1].x -= Block.SIZE;
                b[2].x -= Block.SIZE;
                b[3].x -= Block.SIZE;
            }

            KeyHandler.leftPressed = false;
        }
        if (KeyHandler.rightPressed) {
            if(!rightCollision) {
                b[0].x += Block.SIZE;
                b[1].x += Block.SIZE;
                b[2].x += Block.SIZE;
                b[3].x += Block.SIZE;
            }

            KeyHandler.rightPressed = false;
        }

        if(bottomCollision) {
            if(!deactivating) GamePanel.soundEffect.play(4, false);
            deactivating = true;
        }
        else {
            autoDropCounter++;
            if(autoDropCounter == PlayManager.dropInterval) {
                b[0].y += Block.SIZE;
                b[1].y += Block.SIZE;
                b[2].y += Block.SIZE;
                b[3].y += Block.SIZE;
                autoDropCounter = 0;
            }
        }
    }
    public void draw(Graphics2D g2) {
        int mar = 2;
        g2.setColor(b[0].c);
        g2.fillRect(b[0].x+mar, b[0].y+mar, Block.SIZE-(mar*2), Block.SIZE-(mar*2));
        g2.fillRect(b[1].x+mar, b[1].y+mar, Block.SIZE-(mar*2), Block.SIZE-(mar*2));
        g2.fillRect(b[2].x+mar, b[2].y+mar, Block.SIZE-(mar*2), Block.SIZE-(mar*2));
        g2.fillRect(b[3].x+mar, b[3].y+mar, Block.SIZE-(mar*2), Block.SIZE-(mar*2));
    }

    public void checkRotationCollision() {
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        checkStaticBlockCollision();

        for (Block block : tempB) {
            if (block.x < PlayManager.left_x) leftCollision = true;
            if (block.x + Block.SIZE > PlayManager.right_x) rightCollision = true;
            if (block.y + Block.SIZE > PlayManager.bottom_y) bottomCollision = true;
        }

//        for (Block block : b) {
//            if (block.x + Block.SIZE == PlayManager.right_x) rightCollision = true;
//        }
//
//        for (Block block : b) {
//            if (block.y + Block.SIZE == PlayManager.bottom_y) bottomCollision = true;
//        }
    }
    public void checkMovementCollision() {
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        checkStaticBlockCollision();

        for (Block block : b) {
            if (block.x == PlayManager.left_x) leftCollision = true;
            if (block.x + Block.SIZE == PlayManager.right_x) rightCollision = true;
            if (block.y + Block.SIZE == PlayManager.bottom_y) bottomCollision = true;
        }

//        for (Block block : b) {
//            if (block.x + Block.SIZE == PlayManager.right_x) rightCollision = true;
//        }
//
//        for (Block block : b) {
//            if (block.y + Block.SIZE == PlayManager.bottom_y) bottomCollision = true;
//        }
    }

    public void checkStaticBlockCollision() {
        for (Block mBlock : PlayManager.staticBlocks) {
            int targetX = mBlock.x;
            int targetY = mBlock.y;
            for (Block block : b) {
                if (block.x - Block.SIZE == targetX && block.y == targetY) leftCollision = true;
                if (block.x + Block.SIZE == targetX && block.y == targetY) rightCollision = true;
                if (block.y + Block.SIZE == targetY && block.x == targetX) bottomCollision = true;
            }
        }
    }

    public void deactivating() {
        deactivateCounter++;
        if(deactivateCounter == 45) {
            deactivateCounter = 0;
            checkMovementCollision();
            if(bottomCollision) isActive = false;
        }
    }
    public void getDirection1() {}
    public void getDirection2() {}
    public void getDirection3() {}
    public void getDirection4() {}
}
