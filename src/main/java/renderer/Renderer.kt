package renderer

import components.SpriteRenderer
import jade.GameObject
import java.util.function.Consumer

class Renderer : RendererI {
    private val MAX_BATCH_SIZE = 1000
    private val batches: MutableList<RenderBatch>

    init {
        batches = ArrayList()
    }

    override fun add(obj: GameObject) {
        val spr = obj.getComponent(SpriteRenderer::class.java)
        spr?.let { add(it) }
    }

    override fun add(sprite: SpriteRenderer) {
        var added = false
        for (batch in batches) {
            if (batch.hasRoom() && batch.getzIndex() == sprite.gameObject.getzIndex()) {
                val tex = sprite.texture
                if (tex == null || batch.hasTexture(tex) || batch.hasTextureRoom()) {
                    batch.addSprite(sprite)
                    added = true
                    break
                }
            }
        }
        if (!added) {
            val newBatch = RenderBatch(MAX_BATCH_SIZE, sprite.gameObject.getzIndex())
            newBatch.start()
            batches.add(newBatch)
            newBatch.addSprite(sprite)
            batches.sort()
        }
    }

    override fun render() {
        batches.forEach(Consumer { obj: RenderBatch -> obj.render() })
    }
}