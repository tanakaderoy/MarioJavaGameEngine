package renderer

import components.SpriteRenderer
import jade.GameObject

interface RendererI {
    fun add(obj: GameObject)
    fun add(sprite: SpriteRenderer)
    fun render()
}