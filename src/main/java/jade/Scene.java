package jade;


import renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected Renderer renderer = new Renderer();

    public Scene() {

    }

    public void init() {

    }

    public void start() {
        gameObjects.forEach(go -> {
            go.start();
            renderer.add(go);
        });
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject go) {
        if (!isRunning) {
            gameObjects.add(go);
        } else {
            gameObjects.add(go);
            go.start();
            renderer.add(go);
        }

    }

    public abstract void update(float dt);

    public Camera camera() {
        return this.camera;
    }
}
