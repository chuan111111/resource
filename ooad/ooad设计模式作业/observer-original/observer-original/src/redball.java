import java.awt.*;
import java.util.Random;

public class redball extends Ball{
    Random random = new Random();
    @Override
    public void update(char keychar) {
        switch (keychar) {
            case 'a' -> this.setXSpeed(-random.nextInt(3) - 1);
            case 'd' -> this.setXSpeed(random.nextInt(3) + 1);
            case 'w' -> this.setYSpeed(-random.nextInt(3) - 1);
            case 's' -> this.setYSpeed(random.nextInt(3) + 1);
        }
    }

    @Override
    public void update(Ball ball) {
        if (ball.isIntersect(this)){
            this.setXSpeed(ball.getXSpeed());
            this.setYSpeed(ball.getYSpeed());
        }
    }

    public redball(Color color, int xSpeed, int ySpeed, int ballSize) {
        super(color, xSpeed, ySpeed, ballSize);

    }




}
