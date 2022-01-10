package renderer

import jade.Window
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL30.*
import util.AssetPool
import util.Color
import util.Constants

object DebugDraw {

    private const val MAX_LINES = 500
    private var lines: MutableList<Line2D> = ArrayList()


    // 6 floats per vertex,  2 vertices per line
    private var vertexArray = FloatArray(MAX_LINES * 6 * 2)
    private val shader = AssetPool.getShader(Constants.DEBUG_LINE_2D)
    private var vaoID: Int = 0
    private var vboID: Int = 0
    private var started = false


    fun start() {
        // generate the vao
        vaoID = glGenVertexArrays()
        glBindVertexArray(vaoID)

        // create the vbo and buffer memory
        vboID = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vboID)
        glBufferData(GL_ARRAY_BUFFER, (vertexArray.size * Float.SIZE_BYTES).toLong(), GL_DYNAMIC_DRAW)

        // Enable the vertex array attributes
        glVertexAttribPointer(0, 3, GL_FLOAT, false, (6 * Float.SIZE_BYTES), 0)
        glEnableVertexAttribArray(0)
        glVertexAttribPointer(1, 3, GL_FLOAT, false, (6 * Float.SIZE_BYTES), (3 * Float.SIZE_BYTES).toLong())
        glEnableVertexAttribArray(1)

        glLineWidth(2.0f)
    }

    fun beginFrame() {
        if (!started) {
            start()
            started = true
        }
        // remove dead lines
        var i = 0
        while (i < lines.size) {
            if (lines[i].beginFrame() < 0) {
                lines.removeAt(i)
                i--
            }
            i++
        }

    }

    fun draw() {
        if (lines.isEmpty()) {
            return
        }
        var index = 0
        lines.forEach { line ->
            for (i in 0 until 2) {
                var position = if (i == 0) line.from else line.to
                var color = line.color
                // Load position
                vertexArray[index + 0] = position.x
                vertexArray[index + 1] = position.y
                vertexArray[index + 2] = -10f

                // Load color
                vertexArray[index + 3] = color.x
                vertexArray[index + 4] = color.y
                vertexArray[index + 5] = color.z

                index += 6
            }
        }

        glBindBuffer(GL_ARRAY_BUFFER, vboID)
        GL15.glBufferSubData(GL_ARRAY_BUFFER, 0, vertexArray.copyOfRange(0, lines.size * 6 * 2))

        // Use shader
        shader.use()
        Window.scene.camera().let { camera ->
            shader.uploadMat4f("uProjection", camera.getProjectionMatrix())
            shader.uploadMat4f("uView", camera.getViewMatrix())
        }

        // Bind vao
        glBindVertexArray(vaoID)
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)

        // Draw the batch
        glDrawArrays(GL_LINES, 0, lines.size * 6 * 2)

        // Disable location
        glDisableVertexAttribArray(0)
        glDisableVertexAttribArray(1)
        glBindVertexArray(0)

        // Unbind Shader
        shader.detach()
    }

    // ======================================================
    // Add line 2d Methods
    // ======================================================

    fun addLine2D(from: Vector2f, to: Vector2f) {
        addLine2D(from, to, Color.green.toVec3f(), 1)
    }

    fun addLine2D(from: Vector2f, to: Vector2f, color: Vector3f) {
        addLine2D(from, to, color, 1)
    }

    fun addLine2D(from: Vector2f, to: Vector2f, color: Vector3f, lifetime: Int) {
        if (lines.size >= MAX_LINES) return
        lines.add(Line2D(from, to, color, lifetime))
    }


}