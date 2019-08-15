package objects;

import geometry.Vector;
import java.util.Random;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

public class Flower extends GameObject
{
    
    private static final double FLOWER_HANDLE_HEIGHT = 60.0;
    private static final double FLOWER_HANDLE_RADIUS = 3.0;
    
    private static final double FLOWER_CENTER_RADIUS = 8.0; //ovo vrv treba da bude i radius latica
    
    private static final double FLOWER_PETAL_HEIGHT  = 8.0;
    private static final Color[] FLOWER_PETAL_COLORS;
    static
    {
        FLOWER_PETAL_COLORS = new Color[] {Color.LIGHTGREEN, Color.GREEN, Color.DARKGREEN};
    }
    
    private final Cylinder handle;
    
    private final Sphere center;
    
    private final Cylinder[] petals;
    
    private double degreesToRadians(double degrees) {return Math.PI * degrees / 180;}
    
    private void createPetals()
    {
        PhongMaterial randomMaterial = new PhongMaterial((Color.rgb((int)(Math.random() * 255.0), (int)(Math.random() * 255.0), (int)(Math.random() * 255.0))));
        double dFi = 360.0 / petals.length;
        double fi = 0.0;
        
        for (int i = 0; i < petals.length; i++)
        {
            petals[i] = new Cylinder(FLOWER_CENTER_RADIUS, FLOWER_PETAL_HEIGHT);
            petals[i].setMaterial(randomMaterial);
            petals[i].setTranslateY(-FLOWER_HANDLE_HEIGHT / 2);
            petals[i].getTransforms().add(new Rotate(90, 0, 0, 0, Rotate.X_AXIS));
            
            double dx = FLOWER_CENTER_RADIUS * Math.sin(degreesToRadians(fi));
            double dy = FLOWER_CENTER_RADIUS * Math.cos(degreesToRadians(fi));
            
            petals[i].setTranslateX(petals[i].getTranslateX() + dx);
            petals[i].setTranslateY(petals[i].getTranslateY() + dy);
            
            fi += dFi;
        }
    }
    
    public Flower(Vector position)
    {
        super(position);
        
        handle = new Cylinder(FLOWER_HANDLE_RADIUS, FLOWER_HANDLE_HEIGHT);
        int index = (new Random()).nextInt(FLOWER_PETAL_COLORS.length);
        PhongMaterial greenMaterial = new PhongMaterial(FLOWER_PETAL_COLORS[index]);
        handle.setMaterial(greenMaterial);
        
        center = new Sphere(FLOWER_CENTER_RADIUS);
        PhongMaterial randomMaterial = new PhongMaterial((Color.rgb((int)((new Random()).nextDouble() * 255.0),
                                                                    (int)((new Random()).nextDouble() * 255.0),
                                                                    (int)((new Random()).nextDouble() * 255.0))));
        center.setMaterial(randomMaterial);
        center.setTranslateY(-FLOWER_HANDLE_HEIGHT / 2);
        
        petals = new Cylinder[(int)(Math.random() * 3.0) + 4];
        createPetals();
        
        this.getChildren().addAll(handle, center);
        this.getChildren().addAll(petals);
        
        this.setTranslateX(position.getX());
        this.setTranslateY(position.getY() - FLOWER_HANDLE_HEIGHT / 2);
        this.setTranslateZ(position.getZ());
        
        this.getTransforms().add(new Rotate((new Random()).nextDouble() * 180, Rotate.Y_AXIS));
    }
    
    public static double getMaxDimension() {return FLOWER_CENTER_RADIUS * 4;}
}
