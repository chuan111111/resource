import java.awt.*;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.beans.PropertyChangeListener;

public class whiteball extends Ball{
    @Override
    public void update(char keychar) {
        switch (keychar) {
            case 'a' -> this.setXSpeed(-8);
            case 'd' -> this.setXSpeed(8);
            case 'w' -> this.setYSpeed(-8);
            case 's' -> this.setYSpeed(8);
        }
    }


    @Override
    public void update(Ball ball) {

    }


    public whiteball(Color color, int xSpeed, int ySpeed, int ballSize) {
        super(color, xSpeed, ySpeed, ballSize);

    }



}
