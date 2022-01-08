package jade;

import components.SpriteRenderer;
import components.SpriteSheet;
import org.joml.Vector2f;
import util.AssetPool;

import static util.Constants.SHADERS_DEFAULT_GLSL;
import static util.Constants.SPRITE_SHEET;

public class LevelEditorScene extends Scene {
    private GameObject obj1;
    private GameObject obj2;
    private SpriteSheet sprites;

    public LevelEditorScene() {
    }


    @Override
    public void init() {
        loadResources();
        super.init();

        sprites = AssetPool.getSpriteSheet(SPRITE_SHEET);
        camera = new Camera(new Vector2f(0, 0));
        obj1 = new GameObject("Obj 1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        obj1.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        addGameObjectToScene(obj1);

        obj2 = new GameObject("Obj 1", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)));
        obj2.addComponent(new SpriteRenderer(sprites.getSprite(10)));
        addGameObjectToScene(obj2);


    }

    private void loadResources() {
        AssetPool.getShader(SHADERS_DEFAULT_GLSL);
        AssetPool.addSpriteSheet(SPRITE_SHEET,
                new SpriteSheet(AssetPool.getTexture(SPRITE_SHEET),
                        16,
                        16,
                        26,
                        0));
    }

    private int sprIndex = 0;
    private float spriteFlipTime = 0.2f;
    private float spriteFlipTimeLeft = 0.0f;

    @Override
    public void update(float dt) {
//        System.out.printf("FPS: %f \n", 1f / dt);
        spriteFlipTimeLeft -= dt;
        if (spriteFlipTimeLeft <= 0) {
            spriteFlipTimeLeft = spriteFlipTime;
            sprIndex++;
            if (sprIndex > 4) {
                sprIndex = 0;
            }

            obj1.getComponent(SpriteRenderer.class).setSprite(sprites.getSprite(sprIndex));
        }
        obj1.transform.position.x += 10 * dt;
        gameObjects.forEach(gameObject -> gameObject.update(dt));
        renderer.render();
    }


}
