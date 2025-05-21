package project.ball.mode;

import java.util.List;
import project.ball.Ball;
import project.ball.BallSize;
import project.ball.BallSpeed;
import project.Application;

public class StraightBallDirectionMode extends AbstractBallDirectionMode {

    private int additionalStartIdY;
    private final int[] additionalStartValuesByY = new int[]{130, 180, 230, 330, 380, 430};

    public StraightBallDirectionMode(List<Ball> balls, Application application) {
        super(balls, application);
    }

    @Override
    public void init(BallSize ballSize, BallSpeed ballSpeed) {
        super.init(ballSize, ballSpeed);
        additionalStartIdY = -1;
    }

    @Override
    Ball getInitialRedBall() {
        return new Ball(100, 280, ballSize, ballSpeed, false, true, application);
    }

    @Override
    Ball getInitialBlueBall() {
        return new Ball(100, 280, ballSize, ballSpeed, false, false, application);
    }

    @Override
    Ball getNewRedBall() {
        return new Ball(200, additionalStartValuesByY[getNexAdditionalStartIdY()],
                ballSize, ballSpeed, false, true, application);
    }

    @Override
    Ball getNewBlueBall() {
        return new Ball(200, additionalStartValuesByY[getNexAdditionalStartIdY()],
                ballSize, ballSpeed, false, false, application);
    }

    private int getNexAdditionalStartIdY() {
        return ++additionalStartIdY < additionalStartValuesByY.length ? additionalStartIdY : (additionalStartIdY = 0);
    }
}
