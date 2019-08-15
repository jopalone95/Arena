package objects;

import geometry.Vector;
import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.scene.DepthTest;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class Tile extends GameObject
{
    
    private final Box tile;
    private final Box hitBox;
    private Timeline spikesAnimation;
    
    private boolean playerOnTile;
    private Collectible collectible;
    
    public Tile(Vector position, double width, double height, double depth)
    {
        super(position);
        tile = new Box(width, height, depth);
        hitBox = new Box(width, 20.0, depth);
        hitBox.setTranslateY(-10.0);
        hitBox.setMaterial(new PhongMaterial(Color.TRANSPARENT));
        hitBox.setDepthTest(DepthTest.DISABLE);
        spikesAnimation = null;
        playerOnTile = false;
        collectible = null;
        
        PhongMaterial tileMaterial = new PhongMaterial();
        tileMaterial.setDiffuseMap(new Image(this.getClass().getResource("/resources/sand.jpg").toString()));
        /*tileMaterial.setSpecularMap(new Image(this.getClass().getResource("/resources/spec_map.jpg").toString()));
        tileMaterial.setSpecularPower(100.0);*/
        tile.setMaterial(tileMaterial);
        
        this.setTranslateX(position.getX());
        this.setTranslateY(position.getY());
        this.setTranslateZ(position.getZ());
        
        this.getChildren().addAll(hitBox, tile);
    }
    
    
    public Timeline getSpikesAnimation() {return spikesAnimation;}
    
    public void setSpikesAnimation(Timeline spikesAnimation) {this.spikesAnimation = spikesAnimation;}
    
    
    public void setPlayerOnTile(boolean value) {playerOnTile = value;}
    
    
    public void setCollectible(Collectible _collectible) {collectible = _collectible;}
    
    
    public Material getMaterial() {return tile.getMaterial();}
            
    public void setMaterial(Material phongMaterial) {tile.setMaterial(phongMaterial);}
    
    
    public boolean isFreeForSpikes() {return spikesAnimation.getStatus() == Animation.Status.STOPPED && collectible == null;}
    
    public boolean isFreeForCollectibles() {return spikesAnimation.getStatus() == Animation.Status.STOPPED && playerOnTile == false && collectible == null;}
}
