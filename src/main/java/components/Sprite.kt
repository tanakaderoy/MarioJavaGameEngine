package components

import org.joml.Vector2f
import renderer.Texture
import util.AssetPool

class Sprite {
    var texture: Texture? = null
        private set
    var texCoords: Array<Vector2f>
        private set

    constructor(texture: Texture) {
        init(texture)
        this.texCoords = arrayOf(
            Vector2f(1f, 1f),
            Vector2f(1f, 0f),
            Vector2f(0f, 0f),
            Vector2f(0f, 1f)
        )
    }

    private fun init(texture: Texture) {
        this.texture = texture

    }

    constructor(assetPoolKey: String) {
        init(AssetPool.getTexture(assetPoolKey))
        this.texCoords = arrayOf(
            Vector2f(1f, 1f),
            Vector2f(1f, 0f),
            Vector2f(0f, 0f),
            Vector2f(0f, 1f)
        )
    }

    constructor(texture: Texture?, texCoords: Array<Vector2f>) {
        this.texture = texture
        this.texCoords = texCoords
    }
}