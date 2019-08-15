package objects;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;

public class MovingCamera extends PerspectiveCamera implements EventHandler<Event>
{
    
    private static final double FIELD_OF_VIEW = 30.0;
    private static final double FAR_CLIP = 10_000;
    private static final double CAMERA_ROTATION_STEP = 5.0;
    private static final double STARTING_X_ROTATION_ANGLE = -15.0;
    private static final double MIN_X_ROTATION_ANGLE = -90.0;
    private static final double MAX_X_ROTATION_ANGLE = 0.0;
    private static final double CAMERA_CLIMB_STEP = 100.0;
    private static final double MIN_Y_TRANSLATE = -10000.0;
    private static final double MAX_Y_TRANSLATE = -100.0;
    
    private double xAxisRotationAngle;
    
    public MovingCamera()
    {
        super(true);
        
        setFieldOfView(FIELD_OF_VIEW);
        setFarClip(FAR_CLIP);
        
        xAxisRotationAngle = STARTING_X_ROTATION_ANGLE;
        this.getTransforms().add(new Rotate(STARTING_X_ROTATION_ANGLE, Rotate.X_AXIS));
    }

    private double degreesToRadians(double angle) {return Math.PI / 180.0 * angle;}
    
    private void handleKeyEvent(KeyEvent e)
    {
        if ((e.getCode().equals(KeyCode.UP) || e.getCode().equals(KeyCode.KP_UP)) && xAxisRotationAngle < MAX_X_ROTATION_ANGLE)
        {
            xAxisRotationAngle += CAMERA_ROTATION_STEP;
            this.getTransforms().add(new Rotate(CAMERA_ROTATION_STEP, Rotate.X_AXIS));
        }
        else if ((e.getCode().equals(KeyCode.DOWN) || e.getCode().equals(KeyCode.KP_DOWN)) && xAxisRotationAngle > MIN_X_ROTATION_ANGLE)
        {
            xAxisRotationAngle -= CAMERA_ROTATION_STEP;
            this.getTransforms().add(new Rotate(-CAMERA_ROTATION_STEP, Rotate.X_AXIS));
        }
        else if (e.getCode().equals(KeyCode.LEFT) || e.getCode().equals(KeyCode.KP_LEFT))
        {
            this.getTransforms().add(new Rotate(-xAxisRotationAngle, Rotate.X_AXIS));
            this.getTransforms().add(new Rotate(-CAMERA_ROTATION_STEP, Rotate.Y_AXIS));
            this.getTransforms().add(new Rotate(xAxisRotationAngle, Rotate.X_AXIS));
        }
        else if (e.getCode().equals(KeyCode.RIGHT) || e.getCode().equals(KeyCode.KP_RIGHT))
        {
            this.getTransforms().add(new Rotate(-xAxisRotationAngle, Rotate.X_AXIS));
            this.getTransforms().add(new Rotate(CAMERA_ROTATION_STEP, Rotate.Y_AXIS));
            this.getTransforms().add(new Rotate(xAxisRotationAngle, Rotate.X_AXIS));
        }
    }
    
    private void handleScrollEvent(ScrollEvent e)
    {
        if (e.getDeltaY() >= 0 && this.getTranslateY() > MIN_Y_TRANSLATE)
            this.setTranslateY(this.getTranslateY() - CAMERA_CLIMB_STEP);
        else if (e.getDeltaY() < 0 && this.getTranslateY() < MAX_Y_TRANSLATE)
            this.setTranslateY(this.getTranslateY() + CAMERA_CLIMB_STEP);
    }
    
    @Override
    public void handle(Event event)
    {
        if (event instanceof ScrollEvent)
            handleScrollEvent((ScrollEvent)event);
        else if (event instanceof KeyEvent)
            handleKeyEvent((KeyEvent)event); 
    }
}
