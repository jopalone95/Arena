package objects;

import geometry.Vector;
import java.util.ArrayList;
import java.util.Random;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.util.Duration;

public class FloorWithSpikes extends Group {

    public static final double FLOOR_HEIGHT = 1;

    private static final Color DEFAULT_FLOOR_COLOR = Color.BURLYWOOD;
    private static final PhongMaterial TILE_MATERIAL = new PhongMaterial();
    static {
        TILE_MATERIAL.setDiffuseMap(new Image(FloorWithSpikes.class.getResource("/resources/sand.jpg").toString()));
    }

    private static final double SEPARATOR_THICKNESS = 10;
    private static final PhongMaterial SEPARATOR_MATERIAL = new PhongMaterial(Color.BLACK);

    public static final double ADDITIONAL_REACT_TIME_SEC = 1.25;

    private final Tile[][] tiles;
    private final Spikes[][] spikes;
    private final Coin[] coins;

    public FloorWithSpikes(double width, double length, int numOfTilesX, int numOfTilesZ, int noCoins) {
        tiles = new Tile[numOfTilesX][numOfTilesZ];
        spikes = new Spikes[numOfTilesX][numOfTilesZ];
        coins = new Coin[noCoins];

        final double tileWidth = width / numOfTilesX;
        final double tileLength = length / numOfTilesZ;
        final double timeToReact = ADDITIONAL_REACT_TIME_SEC + (tileWidth > tileLength ? tileLength : tileWidth) / Player.PLAYER_NORMAL_VELOCITY / 60; // 60 is the frequency of UpdateTimer
        for (int i = 0; i < numOfTilesX; i++) {
            for (int j = 0; j < numOfTilesZ; j++) {
                Vector position = new Vector();
                position.setX(-width / 2 + tileWidth / 2 + i * tileWidth);
                position.setY(0.0);
                position.setZ(-length / 2 + tileLength / 2 + j * tileLength);
                tiles[i][j] = new Tile(position, tileWidth, FLOOR_HEIGHT, tileLength);

                Spikes s = new Spikes(new Vector(tiles[i][j].getTranslateX(), tiles[i][j].getTranslateY() + Spikes.SPIKE_HEIGHT / 20, tiles[i][j].getTranslateZ()), tileWidth, tileLength); // the additional Y value is to avoid texture bugs
                spikes[i][j] = s;
                Timeline spikesAnimation = new Timeline(
                        new KeyFrame(Duration.seconds(timeToReact), e -> s.trigger(), new KeyValue(((PhongMaterial) tiles[i][j].getMaterial()).diffuseColorProperty(), Color.RED, Interpolator.LINEAR)),
                        new KeyFrame(Duration.seconds(timeToReact + Spikes.STAY_UP_MOMENT), new KeyValue(((PhongMaterial) tiles[i][j].getMaterial()).diffuseColorProperty(), Color.RED, Interpolator.LINEAR)),
                        new KeyFrame(Duration.seconds(timeToReact + Spikes.COME_DOWN_MOMENT), new KeyValue(((PhongMaterial) tiles[i][j].getMaterial()).diffuseColorProperty(), TILE_MATERIAL.getDiffuseColor(), Interpolator.LINEAR))
                );
                tiles[i][j].setSpikesAnimation(spikesAnimation);
            }
            this.getChildren().addAll(tiles[i]);
            this.getChildren().addAll(spikes[i]);
        }

        for (int i = 0; i < numOfTilesX - 1; i++) {
            Box separator = new Box(SEPARATOR_THICKNESS, FLOOR_HEIGHT, length);
            separator.setTranslateX(-width / 2 + (i + 1) * tileWidth);
            separator.setTranslateY(-FLOOR_HEIGHT * 2);
            separator.setMaterial(SEPARATOR_MATERIAL);
            this.getChildren().add(separator);
        }
        for (int i = 0; i < numOfTilesZ - 1; i++) {
            Box separator = new Box(width, FLOOR_HEIGHT, SEPARATOR_THICKNESS);
            separator.setTranslateZ(-length / 2 + (i + 1) * tileLength);
            separator.setTranslateY(-FLOOR_HEIGHT * 2);
            separator.setMaterial(SEPARATOR_MATERIAL);
            this.getChildren().add(separator);
        }
        
        //initialize coins
        final int[] rowIndices = new Random().ints(0, tiles.length).distinct().limit(coins.length).toArray();
        final int[] colIndices = new Random().ints(0, tiles[0].length).distinct().limit(coins.length).toArray();
        for (int i = 0; i < coins.length; i++)
        {
            int row = rowIndices[i];
            int col = colIndices[i];
            
            coins[i] = new Coin(tiles[row][col]);
            tiles[row][col].setCollectible(coins[i]);
        }
    }

    public void triggerSpikesOnRandomTile() {
        int i = (int) (Math.random() * tiles.length);
        int j = (int) (Math.random() * tiles[i].length);
        if (tiles[i][j].isFreeForSpikes() == false) {
            return;
        }
        tiles[i][j].getSpikesAnimation().play();
    }

    public Spikes[][] getSpikes() {return spikes;}
    
    public Tile[][] getTiles() {return tiles;}
    
    public Coin[] getCoins() {return coins;}
    
    public ArrayList<Tile> getFreeTilesForCollectibles()
    {
        ArrayList<Tile> freeTiles = new ArrayList<>();
        for (Tile[] tilesArray : tiles)
        {
            for (Tile tile : tilesArray)
                if (tile.isFreeForCollectibles() == true)
                    freeTiles.add(tile);
        }
        
        return freeTiles;
    }
}
