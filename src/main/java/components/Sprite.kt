package components

import org.joml.Vector2f
import renderer.Texture

class Sprite {
    private var width = 0f
    private var height = 0f
    var texture: Texture? = null
        private set
    var texCoords: Array<Vector2f> = arrayOf(
        Vector2f(1f, 1f),
        Vector2f(1f, 0f),
        Vector2f(0f, 0f),
        Vector2f(0f, 1f)
    )
        private set

    fun setTexCoords(texCoords: Array<Vector2f>) {
        this.texCoords = texCoords
    }

    fun setTexture(texture: Texture) {
        this.texture = texture
    }

    fun getWidth(): Float {
        return width
    }

    fun getHeight(): Float {
        return height
    }

    fun getTexID(): Int {
        return texture?.getID() ?: -1
    }

    fun setWidth(width: Float) {
        this.width = width
    }

    fun setHeight(height: Float) {
        this.height = height
    }

}