package renderer

import components.SpriteRenderer

interface IRenderBatch {
    fun start()
    fun addSprite(spr: SpriteRenderer)
    fun generateIndices(): IntArray
    fun render()
    fun loadVertexProperties(index: Int)
}