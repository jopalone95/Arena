package objects;

import geometry.Vector;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;

public class Watch extends Collectible
{
    
    private static final long INITIAL_TIME_LIMIT  = 10;
    private static final long INITIAL_TIME_TO_ADD = 30; 
     
    private static final double OUTER_CYLINDER_HEIGHT = 5.0;
    private static final double INNER_CYLINDER_HEIGHT = 8.0;
    private static final double OUTER_CYLINDER_RADIUS = 50.0;
    private static final double INNER_CYLINDER_RADIUS = 40.0;
    
    private static final int NO_HOURS = 12;
    
    private static final double HOUR_HAND_WIDTH  = INNER_CYLINDER_RADIUS - INNER_CYLINDER_RADIUS / 4.0;
    private static final double HOUR_HAND_HEIGHT = INNER_CYLINDER_HEIGHT + 2.5;
    private static final double HOUR_HAND_DEPTH  = 5.0;
    
    private static final double MINUTE_HAND_WIDTH  = INNER_CYLINDER_RADIUS;
    private static final double MINUTE_HAND_HEIGHT = INNER_CYLINDER_HEIGHT + 2.5;
    private static final double MINUTE_HAND_DEPTH  = 5.0;
    
    private int noSpawns;
    private long timeLimit;
    private long timeToAdd;
    
    private final Cylinder outerCylinder;
    private final Cylinder innerCylinder;
    //private Text[] hours;
    
    private final Box hourHand;
    private final Box minuteHand;
    
    /*private void addHours()
    {
        double dFi = 360.0 / NO_HOURS;
        double fi = dFi;
        
        hours = new Text[NO_HOURS];
        
        for (int i = 1; i <= NO_HOURS; i++)
        {
            Text text = new Text(Integer.toString(i));
            text.setFont(new Font(100));
            
            //text.setTranslate
            //text.setTranslateY(INNER_CYLINDER_HEIGHT);
            
            hours[i - 1] = text;
        }
    }*/
    
    public Watch(Tile _tile)
    {
        super(_tile);
        noSpawns = 0;
        timeLimit = INITIAL_TIME_LIMIT;
        timeToAdd = INITIAL_TIME_TO_ADD;
        
        PhongMaterial blackMaterial = new PhongMaterial(Color.BLACK);
        PhongMaterial whiteMaterial = new PhongMaterial(Color.WHITE);
        
        outerCylinder = new Cylinder(OUTER_CYLINDER_RADIUS, OUTER_CYLINDER_HEIGHT);
        outerCylinder.setMaterial(blackMaterial);
        
        innerCylinder = new Cylinder(INNER_CYLINDER_RADIUS, INNER_CYLINDER_HEIGHT);
        innerCylinder.setMaterial(whiteMaterial);
        
        hourHand = new Box(HOUR_HAND_WIDTH, HOUR_HAND_HEIGHT, HOUR_HAND_DEPTH); //width, height, depth
        hourHand.setMaterial(blackMaterial);
        hourHand.setTranslateX(HOUR_HAND_WIDTH / 2 - HOUR_HAND_WIDTH / 4);
        
        minuteHand = new Box(MINUTE_HAND_WIDTH, MINUTE_HAND_HEIGHT, MINUTE_HAND_DEPTH);
        minuteHand.setMaterial(blackMaterial);
        minuteHand.setTranslateX(MINUTE_HAND_WIDTH / 2 - HOUR_HAND_WIDTH / 4);
        minuteHand.getTransforms().add(new Rotate(75.0, -MINUTE_HAND_WIDTH / 2 + HOUR_HAND_WIDTH / 4, 0.0, 0.0, Rotate.Y_AXIS));
        
        this.getTransforms().add(new Rotate(90, 0, 0, 0, Rotate.Z_AXIS));
        
        this.getChildren().addAll(outerCylinder, innerCylinder, hourHand, minuteHand);
        
        this.setTranslateY(-Player.HEIGHT / 1.5 - OUTER_CYLINDER_RADIUS);
    }
    
    public int getNoSpawns() {return noSpawns;}
    
    public long getTimeLimit() {return timeLimit;}
    
    public long getTimeToAdd() {return timeToAdd;}
    
    public void incrementNoSpawns()
    {
        noSpawns++;
        timeLimit /= 2;
        timeToAdd /= 2;
    }
}
