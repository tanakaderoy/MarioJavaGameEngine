package scenes

import components.GridLines
import components.MouseControls
import components.SpriteSheet
import imgui.ImGui
import imgui.ImVec2
import jade.*
import org.joml.Vector2f
import renderer.DebugDraw
import util.AssetPool
import util.Color
import util.Constants

class LevelEditorScene : Scene() {
    //    private var obj1: GameObject? = null
//    private var obj2: GameObject? = null
    private var sprites: SpriteSheet? = null
    private val levelEditorStuff = GameObject("Level Editor", Transform(), 0)
    override fun init() {
        levelEditorStuff.addComponent(MouseControls())
        levelEditorStuff.addComponent(GridLines())
        loadResources()
        camera = Camera(Vector2f(-250f, 0f))
        sprites = AssetPool.getSpriteSheet(Constants.DECORATIONS_AND_BLOCKS)
        if (levelLoaded) {
            this.activeGameObject = gameObjects[0]
            return
        }
        //        obj1 = new GameObject("Obj 1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
//        obj1 = GameObject(
//            "Obj 1", Transform(
//                Vector2f(200f, 100f), Vector2f(256f, 256f)
//            ), 2
//        )
//        val obj1Sprite = SpriteRenderer()
//        obj1Sprite.color = Vector4f(0f, 1f, 0f, 1f)
//        obj1!!.addComponent(obj1Sprite)
//        obj1!!.addComponent(RigidBody())
////        obj1!!.addComponent(SpriteRenderer(Sprite("assets/images/blendImage1.png")))
//        addGameObjectToScene(obj1!!)
//        obj2 = GameObject(
//            "Obj 2", Transform(
//                Vector2f(400f, 100f), Vector2f(256f, 256f)
//            ), 3
//        )
//        val obj2SpriteRenderer = SpriteRenderer()
//        val obj2Sprite = Sprite()
//        obj2Sprite.setTexture(AssetPool.getTexture("assets/images/blendImage2.png"))
//        obj2SpriteRenderer.setSprite(obj2Sprite)
//        obj2!!.addComponent(obj2SpriteRenderer)
//        //        obj2.addComponent(new SpriteRenderer(sprites.getSprite(10)));
//        addGameObjectToScene(obj2!!)
//        this.activeGameObject = obj1

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
                        id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y
                    )
                ) {
                    val `object` = Prefabs.generateSpriteObject(sprite, 32f, 32f)
                    // Attach this to the mouse cursor
                    levelEditorStuff.getComponent(MouseControls::class.java)?.pickupObject(`object`)
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

    var x = 0f
    var y = 0f
    override fun update(dt: Float) {
        DebugDraw.addCircle(Vector2f(x, y), 64f)
        DebugDraw.addBox2D(Vector2f(200, 200), Vector2f(64, 32), 20f, Color.green.toVec3f(), 1)
        x += 50f * dt
        y += 50f * dt
        levelEditorStuff.update(dt)
        MouseListener.getOrthoX()
        gameObjects.forEach { gameObject -> gameObject.update(dt) }
        renderer.render()
    }

    fun Vector2f(x: Int, y: Int): Vector2f = Vector2f(x.toFloat(), y.toFloat())
}

