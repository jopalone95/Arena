package objects;

import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TextDisplayer extends SubScene
{
    
    private static final double VERTICAL_PADDING     = 15.0;
    private static final double HORIZONTAL_PADDING   = 30.0;
    
    private static final double GAME_INFO_FONT_SIZE  = 35.0;
    private static final double GAME_OVER_FONT_SIZE  = 100.0;
    
    private final Group root;
    
    private final double windowWidth;
    private final double windowHeight;
    
    private final Text timeRemaining;
    private final Text points;
    private final Text gameOver;
    private final StaminaBar staminaBar;
    private final LivesBar livesBar;
    
    private void setUpperLeftCorner(Text text)
    {
        text.setX(HORIZONTAL_PADDING);
        text.setY(text.getLayoutBounds().getHeight());
    }
    
    private void setUpperRightCorner(Text text)
    {
        text.setX(windowWidth - text.getLayoutBounds().getWidth() - HORIZONTAL_PADDING);
        text.setY(text.getLayoutBounds().getHeight());
    }
    
    private void setCenter(Text text)
    {
        text.setX((windowWidth - text.getLayoutBounds().getWidth()) / 2);
        text.setY(windowHeight / 2);
    }
    
    private void setLowerLeftCorner(StaminaBar staminaBar)
    {
        staminaBar.setTranslateX(HORIZONTAL_PADDING);
        staminaBar.setTranslateY(windowHeight - staminaBar.getBarHeight() - VERTICAL_PADDING);
    }
    
    private void setLowerRightCorner(LivesBar livesBar)
    {
        livesBar.setTranslateX(windowWidth - livesBar.getBarWidth() - HORIZONTAL_PADDING);
        livesBar.setTranslateY(windowHeight - livesBar.getBarHeight() - VERTICAL_PADDING);
    }
    
    public TextDisplayer(Group root, double windowWidth, double windowHeight, StaminaBar staminaBar, LivesBar livesBar)
    {
        super(root, windowWidth, windowHeight);
        this.root = root;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.staminaBar = staminaBar;
        this.livesBar = livesBar;
        this.setDepthTest(DepthTest.DISABLE);
        
        timeRemaining = new Text();
        timeRemaining.setFont(new Font(GAME_INFO_FONT_SIZE));
        timeRemaining.setFill(Color.ORANGE);
        setUpperLeftCorner(timeRemaining);
        
        points = new Text("Points: 0");
        points.setFont(new Font(GAME_INFO_FONT_SIZE));
        points.setFill(Color.VIOLET);
        setUpperRightCorner(points);
        
        gameOver = new Text();
        gameOver.setFont(new Font(GAME_OVER_FONT_SIZE));
        gameOver.setFill(Color.WHITE);
        
        setLowerLeftCorner(this.staminaBar);
        
        setLowerRightCorner(this.livesBar);
        
        root.getChildren().addAll(timeRemaining, points, this.staminaBar, this.livesBar);
    }
    
    public void setGameover(String message)
    {
        gameOver.setText(message);
        setCenter(gameOver);
        
        root.getChildren().add(gameOver);
    }
    
    public Text getTimeRemaining() {return timeRemaining;}
    
    public Text getPoints() {return points;}
}
