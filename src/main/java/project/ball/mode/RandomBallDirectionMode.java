package project.ball.mode;

import java.util.List;
import java.util.Random;
import project.ball.Ball;
import project.Application;

public class RandomBallDirectionMode extends AbstractBallDirectionMode {

    private final int minX = 100;
    private final int maxX = 500;
    private final int minY = 130;
    private final int maxY = 430;
    private final Random random = new Random();

    public RandomBallDirectionMode(List<Ball> balls, Application application) {
        super(balls, application);
    }

    @Override
    Ball getInitialRedBall() {
        return getNewRedBall();
    }

    @Override
    Ball getInitialBlueBall() {
        return getNewBlueBall();
    }

    @Override
    Ball getNewRedBall() {
        return getRandomBall(true);
    }

    @Override
    Ball getNewBlueBall() {
        return getRandomBall(false);
    }

    private Ball getRandomBall(boolean maxPriority) {
        return new Ball(getRandom(minX, maxX), getRandom(minY, maxY), ballSize, ballSpeed, true, maxPriority, application);
    }

    private int getRandom(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
}
