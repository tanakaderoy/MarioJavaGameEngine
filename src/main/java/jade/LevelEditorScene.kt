package jade

import components.Sprite
import components.SpriteRenderer
import components.SpriteSheet
import imgui.ImGui
import org.joml.Vector2f
import org.joml.Vector4f
import util.AssetPool
import util.Constants
import java.util.function.Consumer

class LevelEditorScene : Scene() {
    private var obj1: GameObject? = null
    private var obj2: GameObject? = null
    private var sprites: SpriteSheet? = null
    override fun init() {
        loadResources()
        super.init()
        sprites = AssetPool.getSpriteSheet(Constants.SPRITE_SHEET)
        camera = Camera(Vector2f(0f, 0f))
        //        obj1 = new GameObject("Obj 1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        obj1 = GameObject(
            "Obj 1", Transform(
                Vector2f(200f, 100f),
                Vector2f(256f, 256f)
            ),
            2
        )
        obj1!!.addComponent(SpriteRenderer(Vector4f(1f, 0f, 0f, 1f)))
//        obj1!!.addComponent(SpriteRenderer(Sprite("assets/images/blendImage1.png")))
        addGameObjectToScene(obj1!!)
        obj2 = GameObject(
            "Obj 2", Transform(
                Vector2f(400f, 100f),
                Vector2f(256f, 256f)
            ),
            2
        )
        obj2!!.addComponent(SpriteRenderer(Sprite("assets/images/blendImage2.png")))
        //        obj2.addComponent(new SpriteRenderer(sprites.getSprite(10)));
        addGameObjectToScene(obj2!!)
        this.activeGameObject = obj1

    }

    private fun loadResources() {
        AssetPool.getShader(Constants.SHADERS_DEFAULT_GLSL)
        AssetPool.addSpriteSheet(
            Constants.SPRITE_SHEET,
            SpriteSheet(
                AssetPool.getTexture(Constants.SPRITE_SHEET),
                16,
                16,
                26,
                0
            )
        )
    }

    override fun imgui() {
        ImGui.begin("Test Window")
        ImGui.text("Some random text")
        ImGui.end()
    }

    override fun update(dt: Float) {
        gameObjects.forEach(Consumer { gameObject: GameObject -> gameObject.update(dt) })
        renderer.render()
    }
}