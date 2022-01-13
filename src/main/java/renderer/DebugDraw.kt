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
import util.JMath

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

    // ======================================================
    // Add Box2d Methods
    // ======================================================

//    fun addBox2D(center: Vector2f, dimensions: Vector2f, rotation: Float = 0.0f) {
//        addBox2D(center = center, dimensions = dimensions, rotation = rotation)
//    }
//
//    fun addBox2D(center: Vector2f, dimensions: Vector2f, rotation: Float = 0.0f, color: Vector3f) {
//        addBox2D(center = center, dimensions = dimensions, rotation = rotation, color = color)
//    }

    fun addBox2D(
        center: Vector2f,
        dimensions: Vector2f,
        rotation: Float = 0.0f,
        color: Vector3f = Color.green.toVec3f(),
        lifetime: Int = 1
    ) {
        var min = Vector2f(center).sub(Vector2f(dimensions).mul(0.5f))
        var max = Vector2f(center).add(Vector2f(dimensions).mul(0.5f))

        var vertices = arrayOf(
            Vector2f(min.x, min.y),
            Vector2f(min.x, max.y),
            Vector2f(max.x, max.y),
            Vector2f(max.x, min.y),
        )

        if (rotation != 0.0f) {
            for (vert in vertices) {
                JMath.rotate(vert, rotation, center)
            }
        }
        addLine2D(vertices[0], vertices[1], color, lifetime)
        addLine2D(vertices[0], vertices[3], color, lifetime)
        addLine2D(vertices[1], vertices[2], color, lifetime)
        addLine2D(vertices[2], vertices[3], color, lifetime)


    }

    // ======================================================
    // Add Circle Methods
    // ======================================================

    fun addCircle(center: Vector2f, radius: Float, color: Vector3f = Color.green.toVec3f(), lifetime: Int = 1) {
        var points = MutableList(20) { Vector2f() }
        val increment = 360 / points.size
        var currentAngle = 0f
        for (i in 0 until points.size) {
            val tmp = Vector2f(radius, 0f)
            JMath.rotate(tmp, currentAngle, Vector2f())
            points[i] = Vector2f(tmp).add(center)
            if (i > 0) {
                addLine2D(points[i - 1], points[i], color, lifetime)
            }
            currentAngle += increment
        }
        addLine2D(points.last(), points.first())
    }

}