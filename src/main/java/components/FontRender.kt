package components

import components.SpriteRenderer
import jade.Component

class FontRender : Component() {
    override fun start() {
        super.start()
        if (gameObject.getComponent(SpriteRenderer::class.java) != null) {
            println("Found Font Renderer!")
        }
    }

    override fun update(dt: Float) {}
}