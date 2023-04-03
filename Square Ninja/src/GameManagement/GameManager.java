package GameManagement;

import collision.AABB;
import entity.Entity;
import entity.GreenSquare;
import entity.Transform;
import gameplay.Click;
import graphics.Camera;
import org.joml.Vector2f;
import world.World;

import java.util.Random;

public class GameManager {
    public int lives;
    public int score;
    public int maxEntities;
    public World world;
    public Camera camera;

    public GameManager(Camera camera, World world, int lives, int startingScore) {
        this.lives = lives;
        this.score = startingScore;
        this.world = world;
        this.camera = camera;
        maxEntities = 1;
    }

    public void InstantiateGreenSquare(Vector2f position, float speed) {
        GreenSquare greenSquare = new GreenSquare(new Transform(position.x, position.y));
        greenSquare.speed = speed;
        world.CreateEntity(greenSquare);
    }

    public void update() {
        for (int i = world.totalEntities(); i < maxEntities; i++) {
            InstantiateGreenSquare(RandomStartingPositionOfSquare(), 1);
        }

        Click();
    }



    /** Position Determinations (x) = 1 & 48 ARE EDGES */
    public Vector2f RandomStartingPositionOfSquare() {
        return new Vector2f(DetermineRandomXOfStartingPosition(11, 47, new Random(), 1000000), -5);
    }

    public float DetermineRandomXOfStartingPosition(float min, float max, Random random, float scalar) {
        int minimum = (int) (min * scalar);
        int maximum = (int) (max * scalar);

        return ((random.nextInt(maximum - minimum) + minimum) / scalar);
    }

    /** Click Determinations */
    public void Click() {
        if (Click.clicked) {
            Vector2f positionOfClick = Click.ConvertClickPositionToRealCoordinates(camera, Click.position, world.getScale());

            for (int i = 0; i < world.totalEntities(); i++) {
                if (world.getCountingUpEntity(i).getBoundingBox().getCollision(new AABB(positionOfClick, new Vector2f())).isIntersecting) {
                    Clicked(world.getCountingUpEntity(i));
                }
            }
        }
    }

    public void Clicked(Entity entity) {
        System.out.println("CLICKED BOX " + entity);
    }
}
