package objects;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

public class StaminaBar extends Rectangle
{
    
    private enum StaminaState
    {
        DRAINED,
        DRAINING,
        RECOVERING,
        RECOVERED
    }
    
    private static final double WIDTH          = 500.0; 
    private static final double HEIGHT         = 30.0;
    private static final double DRAIN_SPEED    = 0.01;
    private static final double RECOVERY_SPEED = 0.0025;
    private static final double MAX_LEVEL      = 1.0;
    private static final double MIN_LEVEL      = 0.0;
    
    private StaminaState staminaState;
    private double staminaLevel;
    
    public StaminaBar()
    {
        super(WIDTH, HEIGHT);
        this.setStrokeWidth(3.0);
        this.setStroke(Color.BLACK);
        this.setFill(Color.YELLOW);
        
        staminaState = StaminaState.RECOVERED;
        staminaLevel = 1.0; 
    }
    
    public double getBarWidth() {return WIDTH;}
    
    public double getBarHeight() {return HEIGHT;}
    
    public void handleKeyEvent(KeyEvent e)
    {
        if (e.getCode().equals(KeyCode.SHIFT))
        {
            if (e.getEventType().equals(KeyEvent.KEY_PRESSED))
            {
                if (staminaLevel > MIN_LEVEL)
                    staminaState = StaminaState.DRAINING;
            }
            else if (e.getEventType().equals(KeyEvent.KEY_RELEASED))
            {
                if (staminaLevel < MAX_LEVEL)
                    staminaState = StaminaState.RECOVERING;
            }
        }
    }
    
    public void update()
    {
        if (staminaState == StaminaState.DRAINED || staminaState == StaminaState.RECOVERED)
            return;
        
        if (staminaState == StaminaState.DRAINING)
        {
            staminaLevel -= DRAIN_SPEED;
            if (staminaLevel <= MIN_LEVEL)
            {
                staminaLevel = MIN_LEVEL;
                staminaState = StaminaState.DRAINED;
            }
        }
        else if (staminaState == StaminaState.RECOVERING)
        {
            staminaLevel += RECOVERY_SPEED;
            if (staminaLevel >= MAX_LEVEL)
            {
                staminaLevel = MAX_LEVEL;
                staminaState = StaminaState.RECOVERED;
            }
        }
        
        Stop[] stops;
        if (staminaLevel > MIN_LEVEL)
        {
            stops = new Stop[] {
                new Stop(MIN_LEVEL, Color.YELLOW), new Stop(staminaLevel, Color.YELLOW),
                new Stop(staminaLevel, Color.TRANSPARENT), new Stop(MAX_LEVEL, Color.TRANSPARENT)
            };
        }
        else
        {
            stops = new Stop[] {new Stop(MIN_LEVEL, Color.TRANSPARENT), new Stop(MAX_LEVEL, Color.TRANSPARENT)};
        }
            
        LinearGradient lg = new LinearGradient(0.0, 0.0, 1.0, 1.0, true, CycleMethod.NO_CYCLE, stops); 
                    
        this.setFill(lg);
    }
    
    public boolean isDraining() {return staminaState == StaminaState.DRAINING;}
}
