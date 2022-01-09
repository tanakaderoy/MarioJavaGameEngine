package components


import imgui.ImGui
import jade.Component
import jade.Transform
import org.joml.Vector2f
import org.joml.Vector4f
import renderer.Texture

class SpriteRenderer : Component {
    var color: Vector4f
        set(color) {
            if (this.color != color) {
                field = color
                isDirty = true
            }

        }
    private var sprite: Sprite? = null
    private lateinit var lastTransform: Transform
    var isDirty = false

    constructor(color: Vector4f) {
        this.color = color
//        sprite = Sprite((null as Texture?)!!)
        isDirty = true
    }

    fun setClean() {
        isDirty = false
    }

    constructor(sprite: Sprite) {
        this.sprite = sprite
        color = Vector4f(1F, 1F, 1F, 1F)
        isDirty = true
    }

    override fun start() {
        super.start()
        lastTransform = gameObject.transform.copy()
    }

    override fun update(dt: Float) {
        if (lastTransform != gameObject.transform) {
            gameObject.transform.copy(lastTransform)
            isDirty = true
        }
    }

    override fun imgui() {
        val imColor = floatArrayOf(color.x, color.y, color.z, color.w)
        if (ImGui.colorPicker4("Color Picker: ", imColor)) {
            color.set(imColor[0], imColor[1], imColor[2], imColor[3])
            isDirty = true
        }

    }


    val texCoords: Array<Vector2f>?
        get() = sprite?.texCoords
    val texture: Texture?
        get() = sprite?.texture


    fun setSprite(sprite: Sprite) {
        this.sprite = sprite
        isDirty = true
    }
}