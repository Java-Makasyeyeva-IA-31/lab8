package project.ball;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;
import project.common.Point;
import project.Application;
import static project.Application.IMAGE;
import static project.ball.BallAudioUtil.getBallHitBorderClip;
import static project.ball.BallAudioUtil.getBallInHoleClip;
import project.panel.BoardPanel;
import static project.panel.BoardPanel.HORIZONTAL_BORDER;
import static project.panel.BoardPanel.POCKETS;
import static project.panel.BoardPanel.POCKET_RADIUS;
import static project.panel.BoardPanel.VERTICAL_BORDER;

public class Ball implements Runnable {

    private int x;
    private int y;
    private int shiftX;
    private int shiftY;
    private int size;
    private final int speed;
    private final int radius;

    private final BufferedImage image;
    private final BoardPanel panel;
    private boolean alive = true;
    private Point pocket = null;
    private final Application application;
    private final int priority;

    private final static Random rand = new Random();
    public final static BufferedImage RED_BALL_IMAGE = IMAGE.getSubimage(1500, 0, 500, 500);
    public final static BufferedImage BLUE_BALL_IMAGE = IMAGE.getSubimage(1500, 500, 500, 500);

    static {
        //Audio system initialization. Buffering and loading into memory.
        getBallHitBorderClip();
    }

    public Ball(int x, int y, BallSize ballSize, BallSpeed ballSpeed,
            boolean random, boolean maxPriority, Application application) {
        this.x = x;
        this.y = y;
        this.size = ballSize.getValue();
        this.speed = ballSpeed.getValue();
        this.radius = size / 2;

        this.image = maxPriority ? RED_BALL_IMAGE : BLUE_BALL_IMAGE;
        this.priority = maxPriority ? Thread.MAX_PRIORITY : Thread.MIN_PRIORITY;
        this.panel = application.getBoardPanel();
        this.application = application;

        if (random) {
            shiftX = rand.nextBoolean() ? 1 : -1;
            shiftY = rand.nextBoolean() ? 1 : -1;
        } else {
            shiftX = 1;
            shiftY = 0;
        }
    }

    @Override
    public void run() {
        try {
            while (alive) {
                //Check hitted pocket before hitted border

                if ((pocket = getHittedPocket()) != null) {
                    if (image.equals(Ball.RED_BALL_IMAGE)) {
                        application.incrementRedBallInHole();
                    } else {
                        application.incrementBlueBallInHole();
                    }

                    goInsidePocket();
                    alive = false;
                    panel.removeBall(this);
                } else if (isBorderHitted()) {
                    getBallHitBorderClip().start();
                }

                panel.repaint();
                Thread.sleep(20);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private boolean isBorderHitted() {
        boolean hitted = false;
        int nextX = x + shiftX * speed;
        int nextY = y + shiftY * speed;

        if (nextX < VERTICAL_BORDER || nextX + size > panel.getWidth() - VERTICAL_BORDER) {
            shiftX *= -1;
            hitted = true;
        } else {
            x = nextX;
        }
        if (nextY < HORIZONTAL_BORDER || nextY + size > panel.getHeight() - HORIZONTAL_BORDER) {
            shiftY *= -1;
            hitted = true;
        } else {
            y = nextY;
        }
        return hitted;
    }

    private void goInsidePocket() throws InterruptedException {
        getBallInHoleClip().start();

        int originalSize = size;

        for (int i = 2; i < 5; i++) {
            size = (int) (originalSize / i);

            x = pocket.getX() - size / 2;
            y = pocket.getY() - size / 2;

            panel.repaint();
            Thread.sleep(500);
        }
    }

    private Point getHittedPocket() {
        int nextX = x + shiftX * speed;
        int nextY = y + shiftY * speed;

        int ballCenterX = nextX + radius;
        int ballCenterY = nextY + radius;

        for (Point pocket : POCKETS) {
            int pocketCenterX = pocket.getX();
            int pocketCenterY = pocket.getY();

            double dx = ballCenterX - pocketCenterX;
            double dy = ballCenterY - pocketCenterY;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance < (POCKET_RADIUS + radius)) {
                return pocket;
            }
        }
        return null;
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, size, size, panel);
    }

    public int getPriority() {
        return priority;
    }

    public void stop() {
        alive = false;
    }
}
