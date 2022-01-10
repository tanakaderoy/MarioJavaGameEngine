package components

import org.joml.Vector2f
import renderer.Texture

class SpriteSheet(private val texture: Texture, spriteWidth: Int, spriteHeight: Int, numSprites: Int, spacing: Int) {
    private val sprites: MutableList<Sprite>

    init {
        sprites = ArrayList()
        var currentX = 0
        var currentY = texture.height - spriteHeight
        for (i in 0 until numSprites) {
            val topY = (currentY + spriteHeight) / texture.height.toFloat()
            val rightX = (currentX + spriteWidth) / texture.width.toFloat()
            val leftX = currentX / texture.width.toFloat()
            val bottomY = currentY / texture.height.toFloat()
            val texCoords = arrayOf(
                Vector2f(rightX, topY),
                Vector2f(rightX, bottomY),
                Vector2f(leftX, bottomY),
                Vector2f(leftX, topY)
            )
            val sprite = Sprite()
            sprite.setTexture(texture)
            sprite.setTexCoords(texCoords)
            sprite.setHeight(spriteHeight.toFloat())
            sprite.setWidth(spriteHeight.toFloat())
            sprites.add(sprite)
            currentX += spriteWidth + spacing
            if (currentX >= texture.width) {
                currentX = 0
                currentY -= spriteHeight + spacing
            }
        }
    }

    fun getSprite(index: Int): Sprite {
        return sprites[index]
    }

    fun size(): Int {
        return sprites.size
    }
}