package objects;

import geometry.Vector;
import java.util.ArrayList;
import java.util.Random;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class Collectible extends GameObject
{
 
    private Tile tile;
    
    private final RotateTransition animation;
    
    public Collectible(Tile _tile)
    {
        super(new Vector());
        tile = _tile;
        
        if (tile != null)
        {
            this.setTranslateX(tile.getTranslateX());
            this.setTranslateZ(tile.getTranslateZ());
        }
        
        animation = new RotateTransition(Duration.millis(2000), this);
        animation.setAxis(Rotate.Y_AXIS);
        animation.setFromAngle(0.0);
        animation.setToAngle(360.0);
        animation.setInterpolator(Interpolator.LINEAR);
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
    }
    
    public static void spawnCollectible(Collectible collectible, FloorWithSpikes floorWithSpikes, ArrayList<GameObject> collisionObjects, Group gameRoot)
    {
        ArrayList<Tile> freeTilesForCollectibles = floorWithSpikes.getFreeTilesForCollectibles();
        int index = (new Random()).nextInt(freeTilesForCollectibles.size());
        Tile tile = freeTilesForCollectibles.get(index);
        tile.setCollectible(collectible);
        collectible.setTile(tile);
                    
        collisionObjects.add(collectible);
        gameRoot.getChildren().add(collectible);
    }
    
    public Tile getTile() {return tile;}
    
    public void setTile(Tile _tile) {
        tile = _tile;
        if (tile != null)
        {
            this.setTranslateX(tile.getTranslateX());
            this.setTranslateZ(tile.getTranslateZ());
        }
    }
}
