package project.ball.mode;

import java.util.List;
import java.util.function.Supplier;
import project.ball.Ball;
import project.ball.BallSize;
import project.ball.BallSpeed;
import project.Application;

public abstract class AbstractBallDirectionMode implements BallDirectionMode {

    protected BallSize ballSize;
    protected BallSpeed ballSpeed;
    private boolean run = false;
    protected final List<Ball> balls;
    protected final Application application;

    private Ball initialRedBall;
    private Ball initialBlueBall;

    public AbstractBallDirectionMode(List<Ball> balls, Application application) {
        this.balls = balls;
        this.application = application;
    }

    @Override
    public void init(BallSize ballSize, BallSpeed ballSpeed) {
        run = true;

        this.ballSize = ballSize;
        this.ballSpeed = ballSpeed;

        stop();
        balls.add((initialRedBall = getInitialRedBall()));
        balls.add((initialBlueBall = getInitialBlueBall()));
    }

    abstract Ball getInitialRedBall();

    abstract Ball getInitialBlueBall();

    @Override
    public void start(boolean initialBlueBallWaitsRedBall) {
        if (!run) {
            if (initialBlueBallWaitsRedBall) {
                startWhenInitialBlueBallWaitsRedBall();
            } else {
                for (Ball ball : balls) {
                    startBallThread(ball);
                }
            }
        }
    }

    private void startWhenInitialBlueBallWaitsRedBall() {
        Thread initialRedBallThread = startBallThread(initialRedBall);
        Thread initialBlueBallThread = new Thread(() -> {
            try {
                initialRedBallThread.join(); // Wait until red ball finishes
                initialBlueBall.run();
            } catch (InterruptedException ignored) {
            }
        });
        initialBlueBallThread.setPriority(initialBlueBall.getPriority());
        initialBlueBallThread.start();
    }

    @Override
    public void stop() {
        run = false;

        for (Ball ball : balls) {
            ball.stop();
        }
        balls.clear();
        application.getBoardPanel().repaint();
    }

    @Override
    public void addRedBalls(int count) {
        addBalls(count, () -> getNewRedBall());
    }

    abstract Ball getNewRedBall();

    @Override
    public void addBlueBalls(int count) {
        addBalls(count, () -> getNewBlueBall());
    }

    abstract Ball getNewBlueBall();

    private void addBalls(int count, Supplier<Ball> newBallSupplier) {
        for (int i = 0; i < count; i++) {
            Ball ball = newBallSupplier.get();

            balls.add(ball);
            startBallThread(ball);
        }
    }

    private Thread startBallThread(Ball ball) {
        Thread ballThread = new Thread(ball);
        ballThread.setPriority(ball.getPriority());
        ballThread.start();
        return ballThread;
    }

    @Override
    public void setBallSizeAndBallSpeed(BallSize ballSize, BallSpeed ballSpeed) {
        this.ballSize = ballSize;
        this.ballSpeed = ballSpeed;
    }
}
