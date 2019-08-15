package objects;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;

public class Heart extends Collectible
{
    
    private static final double SCALE = 0.65;
    
    private Group heart;
    
    public Heart(Tile _tile)
    {
        super(_tile);
     
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(this.getClass().getResource("/resources/heart_model.fxml"));
        try
        {
            heart = fxmlLoader.<Group>load();
        }
        catch (IOException ex)
        {
            Logger.getLogger(Heart.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.getChildren().add(heart);
        
        this.setTranslateY(-Player.HEIGHT / 1.5);
        
        this.setScaleX(SCALE);
        this.setScaleY(SCALE);
        this.setScaleZ(SCALE);
    }
}
