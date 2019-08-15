package game;

import geometry.Vector;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.scene.AmbientLight;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import objects.FloorWithSpikes;
import objects.GameObject;
import objects.Player;
import java.util.Arrays;
import java.util.Random;
import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.scene.PointLight;
import javafx.scene.SubScene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.util.Duration;
import objects.Coin;
import objects.Collectible;
import objects.Floor;
import objects.Heart;
import objects.MovingCamera;
import objects.Spikes;
import objects.TextDisplayer;
import objects.Tile;
import objects.Wall;
import objects.Watch;

public class Game extends Application {

    private static final double WINDOW_WIDTH = 1366;
    private static final double WINDOW_HEIGHT = 768;

    private static final double FLOOR_WIDTH = 3_000;
    private static final double FLOOR_LENGTH = 5_000;
    private static final double SAFE_ZONE_LENGTH = 750;
    private static final int TILES_X = 4;
    private static final int TILES_Z = 5;
    private static final int NO_COINS = 3;
    
    private static final double MOVING_LIGHT_WIDTH = FLOOR_WIDTH - 60.0;
    private static final double MOVING_LIGHT_LENGTH = FLOOR_LENGTH - 60.0;
    private static final double TIME_WIDTH_IN_MILLIS = 2000.0;
    private static final double TIME_LENGTH_IN_MILLIS = TIME_WIDTH_IN_MILLIS * MOVING_LIGHT_LENGTH / MOVING_LIGHT_WIDTH;
    
    private static final int GAME_DURATION_SECONDS = 60;
    
    public static final int MAX_NO_LIVES = 2;
    
    private Group mainRoot;
    private Scene mainScene;
    private Stage stage;
    
    private Group gameRoot;
    private SubScene gameSubscene;
    
    private TextDisplayer textDisplayerSubscene;
    
    private FloorWithSpikes floorWithSpikes;
    
    private Player player;

    private PointLight movingLight;
    private SequentialTransition movingLightAnimation;
    
    private PointLight pointLight;
            
    private AmbientLight ambientLight;
    private final Color defaultAmbientColor = new Color(.7, .7, .7, .7);

    private MovingCamera movingCamera;
    
    private final ArrayList<GameObject> collisionObjects = new ArrayList<>();
    private final ArrayList<Tile> tiles = new ArrayList<>();

    private final UpdateTimer timer = new UpdateTimer();
    
    private boolean gameover;

    private long lastUpdate;
    private long elapsedNanoseconds;
    
    private long timeRemaining;
    private StringProperty timeRemainingProperty;
    
    private long points;
    private StringProperty pointsProperty;
    
    private Heart heart;
    
    private Watch watch;
    
    
    
    private class UpdateTimer extends AnimationTimer {

        @Override
        public void handle(long now) {
            if (gameover == false)
            {
                elapsedNanoseconds += now - lastUpdate;
                lastUpdate = now;
                
                if (elapsedNanoseconds / 1.0e9 >= 1.0)
                {
                    elapsedNanoseconds = 0;
                    
                    timeRemaining--;
                    timeRemainingProperty.setValue("Time remaining: " + Long.toString(timeRemaining));
                    if (timeRemaining == 0)
                    {
                        gameover = true;
                        textDisplayerSubscene.setGameover("Game over, time is up!");
                        return;
                    }
                }
                
                player.update();
                checkForCollisions();
                
                //spikes
                if ((new Random()).nextDouble() > 0.98) {
                    floorWithSpikes.triggerSpikesOnRandomTile();
                }
                
                //heart
                if (heart.getTile() == null && player.getNoLives() < MAX_NO_LIVES && (new Random()).nextDouble() > 0.999)
                    Collectible.spawnCollectible(heart, floorWithSpikes, collisionObjects, gameRoot);
                
                //watch
                if (watch.getTile() == null && watch.getNoSpawns() < 2 && timeRemaining <= watch.getTimeLimit())
                    Collectible.spawnCollectible(watch, floorWithSpikes, collisionObjects, gameRoot);
            }
        }
    }
    
    private void checkForCollisions() { //ovo sluzi da zabrani kretanje sve dok je u koliziji sa zidom ili siljkom/siljcima
        Iterator<Tile> tileIt = tiles.iterator();
        while (tileIt.hasNext()) {
            Tile tile = tileIt.next();
            Bounds tileBounds = tile.getBoundsInParent();
            
            if (tileBounds.intersects((player.localToScene(player.getBody().getBoundsInParent()))))
                tile.setPlayerOnTile(true);
            else
                tile.setPlayerOnTile(false);
        }
        
        Iterator<GameObject> gameObjectIt = collisionObjects.iterator();
        while (gameObjectIt.hasNext()) {
            GameObject object = gameObjectIt.next();
            Bounds objectBounds = object.getBoundsInParent();
            
            if (objectBounds.intersects((player.localToScene(player.getBody().getBoundsInParent())))) {
                if (object instanceof Wall)
                {
                    player.setTranslateX(player.getPosition().getX());
                    player.setTranslateZ(player.getPosition().getZ());
                }
                else if (object instanceof Spikes && player.isInvincible() == false)
                {
                    player.decrementNoLives();
                    player.setInvincible(ambientLight);
                    
                    if (player.getNoLives() == 0)
                    {
                        gameover = true;
                        textDisplayerSubscene.setGameover("Game over, you died!");
                    }
                }
                else if (object instanceof Collectible)
                {
                    Collectible collectible = (Collectible)object;
                    Tile oldTile = collectible.getTile();
                    collectible.setTile(null);
                    oldTile.setCollectible(null);
                    
                    if (collectible instanceof Coin)
                    {
                        ArrayList<Tile> freeTilesForCollectibles = floorWithSpikes.getFreeTilesForCollectibles();
                        int index = (int)(Math.random() * freeTilesForCollectibles.size());
                        Tile newTile = freeTilesForCollectibles.get(index);
                        
                        Coin coin = (Coin)collectible;
                        coin.setTile(newTile);
                        newTile.setCollectible(coin);

                        points++;
                        pointsProperty.setValue("Points: " + Long.toString(points));
                    }
                    else if (collectible instanceof Heart)
                    {
                        player.incrementNoLives();
                        collisionObjects.remove(heart);
                        gameRoot.getChildren().remove(heart);
                    }
                    else if (collectible instanceof Watch)
                    {
                        timeRemaining += watch.getTimeToAdd();
                        timeRemainingProperty.setValue("Time remaining: " + Long.toString(timeRemaining));
                        watch.incrementNoSpawns();
                        collisionObjects.remove(watch);
                        gameRoot.getChildren().remove(watch);
                    }
                }
                
                break;
            }
        }
        player.getPosition().setX(player.getTranslateX());
        player.getPosition().setZ(player.getTranslateZ());
    }

    private void createGameObjects() {
        player = new Player(new Vector(0, -Player.HEIGHT / 1.5, -FLOOR_LENGTH / 2 + SAFE_ZONE_LENGTH / 2), this);
        
        movingLight = new PointLight(Color.BLUEVIOLET);
        movingLight.setTranslateX(MOVING_LIGHT_WIDTH / 2);
        movingLight.setTranslateY(- Wall.DEFAULT_HEIGHT / 2);
        movingLight.setTranslateZ(MOVING_LIGHT_LENGTH / 2);
        movingLightAnimation = new SequentialTransition();
        TranslateTransition tt = new TranslateTransition(Duration.millis(TIME_WIDTH_IN_MILLIS), movingLight);
        tt.setByX(-MOVING_LIGHT_WIDTH);
        TranslateTransition tt1 = new TranslateTransition(Duration.millis(TIME_LENGTH_IN_MILLIS), movingLight);
        tt1.setByZ(-MOVING_LIGHT_LENGTH);
        TranslateTransition tt2 = new TranslateTransition(Duration.millis(TIME_WIDTH_IN_MILLIS), movingLight);
        tt2.setByX(MOVING_LIGHT_WIDTH);
        TranslateTransition tt3 = new TranslateTransition(Duration.millis(TIME_LENGTH_IN_MILLIS), movingLight);
        tt3.setByZ(MOVING_LIGHT_LENGTH);
        movingLightAnimation.getChildren().addAll(tt, tt1, tt2, tt3);
        movingLightAnimation.setCycleCount(Animation.INDEFINITE);
        
        pointLight = new PointLight();
        pointLight.setTranslateY(-100000000000000.0);
        
        ambientLight = new AmbientLight(defaultAmbientColor);
        
        movingCamera = new MovingCamera();
        movingCamera.setTranslateY(-Wall.DEFAULT_HEIGHT / 2);
        
        floorWithSpikes = new FloorWithSpikes(FLOOR_WIDTH, FLOOR_LENGTH - 2 * SAFE_ZONE_LENGTH, TILES_X, TILES_Z, NO_COINS);
        floorWithSpikes.setTranslateY(FloorWithSpikes.FLOOR_HEIGHT / 2);
        Spikes[][] spikes = floorWithSpikes.getSpikes();
        for (Spikes[] s : spikes) {
            collisionObjects.addAll(Arrays.asList(s));
        }
        Tile[][] tilesMatrix = floorWithSpikes.getTiles();
        for (Tile[] t : tilesMatrix) {
            tiles.addAll(Arrays.asList(t));
        }
        Coin[] c = floorWithSpikes.getCoins();
        collisionObjects.addAll(Arrays.asList(c));

        Floor floor1 = new Floor(FLOOR_WIDTH, SAFE_ZONE_LENGTH);
        floor1.setTranslateY(FloorWithSpikes.FLOOR_HEIGHT / 2);
        floor1.setTranslateZ(-FLOOR_LENGTH / 2 + SAFE_ZONE_LENGTH / 2);
        Floor floor2 = new Floor(FLOOR_WIDTH, SAFE_ZONE_LENGTH);
        floor2.setTranslateY(FloorWithSpikes.FLOOR_HEIGHT / 2);
        floor2.setTranslateZ(FLOOR_LENGTH / 2 - SAFE_ZONE_LENGTH / 2);
        
        collisionObjects.add(new Wall(new Vector(0, 0, FLOOR_LENGTH / 2 + Wall.DEFAULT_THICKNESS / 2), FLOOR_WIDTH, Wall.DEFAULT_HEIGHT, Wall.DEFAULT_THICKNESS));
        collisionObjects.add(new Wall(new Vector(0, 0, -FLOOR_LENGTH / 2 - Wall.DEFAULT_THICKNESS / 2), FLOOR_WIDTH, Wall.DEFAULT_HEIGHT, Wall.DEFAULT_THICKNESS));
        collisionObjects.add(new Wall(new Vector(-FLOOR_WIDTH / 2 - Wall.DEFAULT_THICKNESS / 2, 0, 0), Wall.DEFAULT_THICKNESS, Wall.DEFAULT_HEIGHT, FLOOR_LENGTH));
        collisionObjects.add(new Wall(new Vector(FLOOR_WIDTH / 2 + Wall.DEFAULT_THICKNESS / 2, 0, 0), Wall.DEFAULT_THICKNESS, Wall.DEFAULT_HEIGHT, FLOOR_LENGTH));
        
        gameRoot.getChildren().addAll(player, ambientLight, movingLight, /*pointLight,*/ floorWithSpikes, floor1, floor2);
        gameRoot.getChildren().addAll(collisionObjects);
    }

    private EventHandler<KeyEvent> turnLightOnOff()
    {
        return (KeyEvent e) -> {
            if (e.getCharacter().equals("L") || e.getCharacter().equals("l"))
            {
                if (movingLight.isLightOn())
                    movingLight.setLightOn(false);
                else
                    movingLight.setLightOn(true);
            }
        };
    }
    
    private EventHandler<KeyEvent> changeCamera()
    {
        return (KeyEvent e) -> {
            if (e.getCharacter().equals("1"))
                gameSubscene.setCamera(player.getView());
             else if (e.getCharacter().equals("2"))
                gameSubscene.setCamera(movingCamera);
        };
    }
    
    private void setHandlers()
    {
        mainScene.addEventHandler(MouseEvent.MOUSE_MOVED, player); //stavi ovo u privatnu metodu setHandlers
        mainScene.addEventHandler(KeyEvent.ANY, player);
        
        mainScene.addEventHandler(KeyEvent.KEY_PRESSED, movingCamera);
        mainScene.addEventHandler(ScrollEvent.ANY, movingCamera);
        
        mainScene.addEventHandler(KeyEvent.KEY_TYPED, turnLightOnOff());
        mainScene.addEventHandler(KeyEvent.KEY_TYPED, changeCamera());
    }
    
    @Override
    public void start(Stage primaryStage) {
        gameover = false;
        lastUpdate = System.nanoTime();
        elapsedNanoseconds = 0;
        
        timeRemaining = GAME_DURATION_SECONDS;
        timeRemainingProperty = new SimpleStringProperty();
        timeRemainingProperty.setValue("Time remaining: " + Long.toString(timeRemaining));
        
        points = 0;
        pointsProperty = new SimpleStringProperty();
        pointsProperty.setValue("Points: " + Long.toString(points));
        
        heart = new Heart(null);
        
        watch = new Watch(null);
        
        stage = primaryStage;

        gameSubscene = new SubScene(gameRoot = new Group(), WINDOW_WIDTH, WINDOW_HEIGHT, true, SceneAntialiasing.BALANCED);
        createGameObjects();
        gameSubscene.setFill(Color.BLUE);
        gameSubscene.setCamera(player.getView());
        gameSubscene.setCursor(Cursor.NONE);
        
        textDisplayerSubscene = new TextDisplayer(new Group(), WINDOW_WIDTH, WINDOW_HEIGHT, player.getStaminaBar(), player.getLivesBar());
        textDisplayerSubscene.getTimeRemaining().textProperty().bind(timeRemainingProperty);
        textDisplayerSubscene.getPoints().textProperty().bind(pointsProperty);
        
        mainScene = new Scene(mainRoot = new Group(), WINDOW_WIDTH, WINDOW_HEIGHT, true, SceneAntialiasing.BALANCED);
        mainScene.setCursor(Cursor.NONE);
        setHandlers();
        mainRoot.getChildren().addAll(gameSubscene, textDisplayerSubscene);
        
        primaryStage.setTitle("Arena");
        primaryStage.setScene(mainScene);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();
        
        movingLightAnimation.play();
        
        timer.start();
    }

    public int getCenterX() {
        return (int) (stage.getX() + stage.getWidth() / 2.0);
    }

    public int getCenterY() {
        return (int) (stage.getY() + stage.getHeight() / 2.0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
