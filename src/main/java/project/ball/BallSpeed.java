package project.ball;

public enum BallSpeed {
    SLOW(5), NORMAL(10), FAST(20);
    
    private final int value;
    
    BallSpeed(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
}
