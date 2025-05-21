package project.ball.mode;

import project.ball.BallSize;
import project.ball.BallSpeed;

public interface BallDirectionMode {

    void init(BallSize ballSize, BallSpeed ballSpeed);

    void start(boolean initialBlueBallWaitsRedBall);

    void stop();

    void addRedBalls(int count);
    
    void addBlueBalls(int count);

    void setBallSizeAndBallSpeed(BallSize ballSize, BallSpeed ballSpeed);
}
