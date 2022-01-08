package jade;

import components.SpriteRenderer;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;

import static util.Constants.SHADERS_DEFAULT_GLSL;

public class LevelEditorScene extends Scene {

    public LevelEditorScene() {
    }


    @Override
    public void init() {
        super.init();
        camera = new Camera(new Vector2f());
        int xOffset = 10, yOffset = 10;

        float totalWidth = (float) (600 - xOffset * 2);
        float totalHeight = (float) (300 - yOffset * 2);
        float sizeX = totalWidth / 100.0f;
        float sizeY = totalHeight / 100.0f;

        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                float xPos = xOffset + (x * sizeX);
                float yPos = yOffset + (y * sizeY);


                GameObject go = new GameObject("Obj %d%d".formatted(x, y), new Transform(new Vector2f(xPos, yPos), new Vector2f(sizeX, sizeY)));
                go.addComponent(new SpriteRenderer(new Vector4f(xPos / totalWidth, yPos / totalHeight, 1, 1)));
                addGameObjectToScene(go);
            }
        }

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
