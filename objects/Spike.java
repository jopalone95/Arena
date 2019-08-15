package objects;

import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class Spike extends MeshView
{
    
    private static final int NO_SIDES = 100; //minimum is three
    
    private static final float SPIKE_RADIUS = 120;
    private static final float SPIKE_HEIGHT = SPIKE_RADIUS * 2;
    
    public Spike()
    {
        TriangleMesh pyramid = new TriangleMesh();
        
        pyramid.getTexCoords().addAll(
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f
        );
        
        pyramid.getPoints().addAll(
            0.0f, 0.0f, 0.0f,
            0.0f, -SPIKE_HEIGHT, 0.0f
        );
        
        double dFi = 360.0 / NO_SIDES;
        double fi = 0.0;
        
        for (int i = 0; i < NO_SIDES; i++)
        {
            float dx = SPIKE_RADIUS * (float)Math.sin(degreesToRadians(fi));
            float dz = SPIKE_RADIUS * (float)Math.cos(degreesToRadians(fi));
            
            pyramid.getPoints().addAll(dx, 0.0f, dz);
            
            fi += dFi;
        }
        
        for (int i = 0; i < NO_SIDES - 1; i++)
        {
            pyramid.getFaces().addAll(1,0, i + 2,1, i + 3,2);
        }
        pyramid.getFaces().addAll(1,0, 2,1, NO_SIDES + 1,2);
        
        for (int i = 0; i < NO_SIDES - 1; i++)
        {
            pyramid.getFaces().addAll(0,0, i + 2,1, i + 3,2);
        }
        pyramid.getFaces().addAll(0,0, NO_SIDES + 1,1, 2,2);
        
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(new Image(this.getClass().getResource("/resources/metal.jpg").toString()));
        
        this.setMaterial(material);
        this.setMesh(pyramid);
        this.setDrawMode(DrawMode.FILL);
        this.setCullFace(CullFace.NONE);
    }
    
    private double degreesToRadians(double degrees) {return 2 * Math.PI * degrees / 360;}
}
