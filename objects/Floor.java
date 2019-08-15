package objects;

import geometry.Vector;
import java.util.Random;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class Floor extends Group {

    public static final double FLOOR_HEIGHT = 1;

    private static final Color DEFAULT_FLOOR_COLOR = Color.GREEN;

    private static final int MIN_NO_FLOWERS = 6;
    private static final int MAX_NO_FLOWERS = 12;
    
    private final Box floor;
    
    private final Flower[] flowers;
    
    public Floor(double width, double length) {
        floor = new Box(width, FLOOR_HEIGHT, length);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(new Image(getClass().getResource("/resources/grass.jpg").toString()));
        floor.setMaterial(material);
        
        int noFlowers = (new Random()).nextInt(MAX_NO_FLOWERS - MIN_NO_FLOWERS + 1) + MIN_NO_FLOWERS;
        flowers = new Flower[noFlowers];
        
        for (int i = 0; i < flowers.length; i++)
        {
            Vector position = new Vector(-width / 2, 0.0, -length / 2);
            Flower flower = new Flower(position);
            flower.setTranslateX(flower.getTranslateX() + (new Random()).nextDouble() * (width - Flower.getMaxDimension()) + Flower.getMaxDimension() / 2);
            flower.setTranslateZ(flower.getTranslateZ() + (new Random()).nextDouble() * (length - Flower.getMaxDimension()) + Flower.getMaxDimension() / 2);
            flowers[i] = flower;
        }
        
        this.getChildren().add(floor);
        this.getChildren().addAll(flowers);
    }
    
}
