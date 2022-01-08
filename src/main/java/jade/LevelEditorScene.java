package jade;

import components.SpriteRenderer;
import org.joml.Vector2f;
import util.AssetPool;

import static util.Constants.SHADERS_DEFAULT_GLSL;

public class LevelEditorScene extends Scene {

    public LevelEditorScene() {
    }


    @Override
    public void init() {
        super.init();
        camera = new Camera(new Vector2f(0, 0));
        GameObject obj1 = new GameObject("Obj 1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        obj1.addComponent(new SpriteRenderer(AssetPool.getTexture("assets/images/testImage.png")));
        addGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("Obj 1", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)));
        obj2.addComponent(new SpriteRenderer(AssetPool.getTexture("assets/images/testImage2.png")));
        addGameObjectToScene(obj2);


        loadResources();

    }

    private void loadResources() {
        AssetPool.getShader(SHADERS_DEFAULT_GLSL);
    }

    @Override
    public void update(float dt) {
//        System.out.printf("FPS: %f \n", 1f / dt);
        gameObjects.forEach(gameObject -> gameObject.update(dt));
        renderer.render();
    }


}
