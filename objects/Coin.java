package objects;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;

public class Coin extends Collectible
{
    
    private static final double OUTER_CYLINDER_HEIGHT = 5.0;
    private static final double INNER_CYLINDER_HEIGHT = 8.0;
    private static final double OUTER_CYLINDER_RADIUS = 50.0;
    private static final double INNER_CYLINDER_RADIUS = 40.0;
    
    private static final double LOGO_WIDTH  = 20.0;
    private static final double LOGO_HEIGHT = 11.0;
    private static final double LOGO_DEPTH  = 20.0;
    
    private Tile tile;
    
    private final Cylinder outerCylinder;
    private final Cylinder innerCylinder;
    
    private final Box logo;
    private final Box logo1;
    
    public Coin(Tile _tile)
    {
        super(_tile);
        
        PhongMaterial goldMaterial = new PhongMaterial(Color.GOLD);
        PhongMaterial whiteMaterial = new PhongMaterial(Color.WHITE);
        
        outerCylinder = new Cylinder(OUTER_CYLINDER_RADIUS, OUTER_CYLINDER_HEIGHT);
        outerCylinder.setMaterial(goldMaterial);
        
        innerCylinder = new Cylinder(INNER_CYLINDER_RADIUS, INNER_CYLINDER_HEIGHT);
        innerCylinder.setMaterial(whiteMaterial);
        
        logo = new Box(LOGO_WIDTH, LOGO_HEIGHT, LOGO_DEPTH);
        logo.setMaterial(goldMaterial);
        
        logo1 = new Box(LOGO_WIDTH, LOGO_HEIGHT, LOGO_DEPTH);
        logo1.setMaterial(goldMaterial);
        logo1.getTransforms().add(new Rotate(45, 0, 0, 0, Rotate.Y_AXIS));
        
        this.getTransforms().add(new Rotate(90, 0, 0, 0, Rotate.Z_AXIS));
        
        this.getChildren().addAll(outerCylinder, innerCylinder, logo, logo1);
        
        this.setTranslateY(-Player.HEIGHT / 1.5 - OUTER_CYLINDER_RADIUS);
    }
}
