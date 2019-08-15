package objects;

import game.Game;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class LivesBar extends HBox
{
    
    private static final Image FULL_HEART_IMAGE;
    private static final Image EMPTY_HEART_IMAGE;
    static
    {
        FULL_HEART_IMAGE = new Image(LivesBar.class.getResource("/resources/full_heart_sprite.png").toString());
        EMPTY_HEART_IMAGE = new Image(LivesBar.class.getResource("/resources/empty_heart_sprite.png").toString());
    }
    private static final double HEART_WIDTH  = 40.0;
    private static final double HEART_HEIGHT = 40.0;
    private static final double HEART_SPACING = 20.0;
    
    private final Rectangle[] lives;
    
    private int noLives;
    
    public LivesBar()
    {
        super(HEART_SPACING);
        
        lives = new Rectangle[Game.MAX_NO_LIVES];
        
        for (int i = 0; i < Game.MAX_NO_LIVES; i++)
        {
            Rectangle rectangle = new Rectangle(HEART_WIDTH, HEART_HEIGHT);
            rectangle.setFill(new ImagePattern(FULL_HEART_IMAGE, 0.0, 0.0, 1.0, 1.0, true));
            
            lives[i] = rectangle;
        }
        
        noLives = Game.MAX_NO_LIVES;
        
        this.getChildren().addAll(lives);
    }
    
    public double getBarWidth() {return Game.MAX_NO_LIVES * HEART_WIDTH + (Game.MAX_NO_LIVES - 1) * HEART_SPACING;}
    
    public double getBarHeight() {return HEART_HEIGHT;}
    
    public int getNoLives() {return noLives;}
    
    public void incrementNoLives()
    {
        if (noLives < Game.MAX_NO_LIVES)
        {
            lives[noLives].setFill(new ImagePattern(FULL_HEART_IMAGE));
            noLives++;
        }
    }
    
    public void decrementNoLives()
    {
        if (noLives > 0)
        {
            noLives--;
            lives[noLives].setFill(new ImagePattern(EMPTY_HEART_IMAGE));
        }
    }
}
