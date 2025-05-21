package project.ball;

public enum BallSize {
    SMALL(20), MEDIUM(30), LARGE(50);
    
    private final int value;
    
    BallSize(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
}
