package jade;

import components.Sprite;
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
//        obj1 = new GameObject("Obj 1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        obj1 = new GameObject("Obj 1", new Transform(new Vector2f(200, 100),
                new Vector2f(256, 256)),
                2);
        obj1.addComponent(new SpriteRenderer(new Sprite("assets/images/blendImage1.png")));
        addGameObjectToScene(obj1);

        obj2 = new GameObject("Obj 2", new Transform(new Vector2f(400, 100),
                new Vector2f(256, 256)),
                2);
        obj2.addComponent(new SpriteRenderer(new Sprite("assets/images/blendImage2.png")));
//        obj2.addComponent(new SpriteRenderer(sprites.getSprite(10)));
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


    @Override
    public void update(float dt) {

        gameObjects.forEach(gameObject -> gameObject.update(dt));
        renderer.render();
    }


}
