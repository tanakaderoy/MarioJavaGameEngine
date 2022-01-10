package scenes

import components.*
import imgui.ImGui
import imgui.ImVec2
import jade.*
import org.joml.Math.cos
import org.joml.Vector2f
import org.joml.Vector4f
import renderer.DebugDraw
import util.AssetPool
import util.Color
import util.Constants
import kotlin.math.sin

class LevelEditorScene : Scene() {
    private var obj1: GameObject? = null
    private var obj2: GameObject? = null
    private var sprites: SpriteSheet? = null
    private val mouseControls = MouseControls()
    override fun init() {
        loadResources()
        camera = Camera(Vector2f(-250f, 0f))
        sprites = AssetPool.getSpriteSheet(Constants.DECORATIONS_AND_BLOCKS)
        DebugDraw.addLine2D(Vector2f(0f, 0f), Vector2f(800f, 800f), Color.red.toVec3f(), 120)
        if (levelLoaded) {
            this.activeGameObject = gameObjects[0]
            return
        }
        //        obj1 = new GameObject("Obj 1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        obj1 = GameObject(
            "Obj 1", Transform(
                Vector2f(200f, 100f), Vector2f(256f, 256f)
            ), 2
        )
        val obj1Sprite = SpriteRenderer()
        obj1Sprite.color = Vector4f(0f, 1f, 0f, 1f)
        obj1!!.addComponent(obj1Sprite)
        obj1!!.addComponent(RigidBody())
//        obj1!!.addComponent(SpriteRenderer(Sprite("assets/images/blendImage1.png")))
        addGameObjectToScene(obj1!!)
        obj2 = GameObject(
            "Obj 2", Transform(
                Vector2f(400f, 100f), Vector2f(256f, 256f)
            ), 3
        )
        val obj2SpriteRenderer = SpriteRenderer()
        val obj2Sprite = Sprite()
        obj2Sprite.setTexture(AssetPool.getTexture("assets/images/blendImage2.png"))
        obj2SpriteRenderer.setSprite(obj2Sprite)
        obj2!!.addComponent(obj2SpriteRenderer)
        //        obj2.addComponent(new SpriteRenderer(sprites.getSprite(10)));
        addGameObjectToScene(obj2!!)
        this.activeGameObject = obj1

        load()

    }


    private fun loadResources() {
        AssetPool.getShader(Constants.SHADERS_DEFAULT_GLSL)
        AssetPool.addSpriteSheet(
            Constants.DECORATIONS_AND_BLOCKS, SpriteSheet(
                AssetPool.getTexture(Constants.DECORATIONS_AND_BLOCKS), 16, 16, 81, 0
            )
        )
        AssetPool.getTexture("assets/images/blendImage2.png")
    }

    override fun imgui() {
        ImGui.begin("Test Window")
        val windowPos = ImVec2()
        ImGui.getWindowPos(windowPos)
        val windowSize = ImVec2()
        ImGui.getWindowSize(windowSize)
        val itemSpacing = ImVec2()
        ImGui.getStyle().getItemSpacing(itemSpacing)

        val windowX2 = windowPos.x + windowSize.x
        sprites?.let { sprites ->
            for (i in 0 until sprites.size()) {
                val sprite = sprites.getSprite(i)
                val spriteWidth = sprite.getWidth() * 4f
                val spriteHeight = sprite.getHeight() * 4f
                val id = sprite.getTexID()
                val texCoords = sprite.texCoords
                ImGui.pushID(i)
                if (ImGui.imageButton(
                        id, spriteWidth, spriteHeight, texCoords[0].x, texCoords[0].y, texCoords[2].x, texCoords[2].y
                    )
                ) {
                    val `object` = Prefabs.generateSpriteObject(sprite, spriteWidth, spriteHeight)
                    // Attach this to the mouse cursor
                    mouseControls.pickupObject(`object`)
                }
                ImGui.popID()

                val lastButtonPos = ImVec2()

                ImGui.getItemRectMax(lastButtonPos)
                val lastButtonX2 = lastButtonPos.x
                val nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth
                if (i + 1 < sprites.size() && nextButtonX2 < windowX2) {
                    ImGui.sameLine()
                }
            }

        }
        ImGui.end()
    }

    var t = 0f
    override fun update(dt: Float) {
        mouseControls.update(dt)
        var x = ((sin(t) * 200f) + 600f).toFloat()
        var y: Float = (cos(t) * 200f) + 400
        t += 0.05f
        DebugDraw.addLine2D(Vector2f(600f, 400f), Vector2f(x, y), Color.blue.toVec3f(), 10)
        MouseListener.getOrthoX()
        gameObjects.forEach { gameObject -> gameObject.update(dt) }
        renderer.render()
    }
}