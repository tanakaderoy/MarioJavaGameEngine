package components

import jade.Window
import org.joml.Vector2f
import org.joml.Vector3f
import renderer.DebugDraw
import util.Settings
import kotlin.math.max


class GridLines : Component() {
    override fun update(dt: Float) {
        val cameraPos: Vector2f = Window.scene.camera().position
        val projectionSize: Vector2f = Window.scene.camera().getProjectionSize()
        val firstX = ((cameraPos.x / Settings.GRID_WIDTH).toInt() - 1) * Settings.GRID_HEIGHT
        val firstY = ((cameraPos.y / Settings.GRID_HEIGHT).toInt() - 1) * Settings.GRID_HEIGHT
        val numVtLines = (projectionSize.x / Settings.GRID_WIDTH).toInt() + 2
        val numHzLines = (projectionSize.y / Settings.GRID_HEIGHT).toInt() + 2
        val height = projectionSize.y.toInt() + Settings.GRID_HEIGHT * 2
        val width = projectionSize.x.toInt() + Settings.GRID_WIDTH * 2
        val maxLines = max(numVtLines, numHzLines)
        val color = Vector3f(0.2f, 0.2f, 0.2f)
        for (i in 0 until maxLines) {
            val x = firstX + Settings.GRID_WIDTH * i
            val y = firstY + Settings.GRID_HEIGHT * i
            if (i < numVtLines) {
                DebugDraw.addLine2D(fromX = x, fromY = firstY, toX = x, toY = firstY + height, color = color)
            }
            if (i < numHzLines) {
                DebugDraw.addLine2D(fromX = firstX, fromY = y, toX = firstX + width, toY = y, color = color)
            }
        }
    }

    private fun DebugDraw.addLine2D(fromX: Int, fromY: Int, toX: Int, toY: Int, color: Vector3f = Vector3f()) {
        val from = Vector2f(fromX.toFloat(), fromY.toFloat())
        val to = Vector2f(toX.toFloat(), toY.toFloat())
        this.addLine2D(from, to, color)
    }
}
