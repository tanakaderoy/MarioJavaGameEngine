package jade

import components.RigidBody
import components.Sprite
import components.SpriteRenderer
import components.SpriteSheet
import imgui.ImGui
import imgui.ImVec2
import org.joml.Vector2f
import org.joml.Vector4f
import util.AssetPool
import util.Constants

class LevelEditorScene : Scene() {
    private var obj1: GameObject? = null
    private var obj2: GameObject? = null
    private var sprites: SpriteSheet? = null
    override fun init() {
        loadResources()
        super.init()
        camera = Camera(Vector2f(-250f, 0f))
        sprites = AssetPool.getSpriteSheet(Constants.DECORATIONS_AND_BLOCKS)
        if (levelLoaded) {
            this.activeGameObject = gameObjects[0]
            return
        }
        //        obj1 = new GameObject("Obj 1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        obj1 = GameObject(
            "Obj 1", Transform(
                Vector2f(200f, 100f),
                Vector2f(256f, 256f)
            ),
            2
        )
        val obj1Sprite = SpriteRenderer()
        obj1Sprite.color = Vector4f(0f, 1f, 0f, 1f)
        obj1!!.addComponent(obj1Sprite)
        obj1!!.addComponent(RigidBody())
//        obj1!!.addComponent(SpriteRenderer(Sprite("assets/images/blendImage1.png")))
        addGameObjectToScene(obj1!!)
        obj2 = GameObject(
            "Obj 2", Transform(
                Vector2f(400f, 100f),
                Vector2f(256f, 256f)
            ),
            3
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
            Constants.DECORATIONS_AND_BLOCKS,
            SpriteSheet(
                AssetPool.getTexture(Constants.DECORATIONS_AND_BLOCKS),
                16,
                16,
                81,
                0
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
                        id,
                        spriteWidth,
                        spriteHeight,
                        texCoords[0].x,
                        texCoords[0].y,
                        texCoords[2].x,
                        texCoords[2].y
                    )
                ) {
                    println("Button $i clicked")
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

    override fun update(dt: Float) {
        MouseListener.getOrthoX()
        gameObjects.forEach { gameObject -> gameObject.update(dt) }
        renderer.render()
    }
}