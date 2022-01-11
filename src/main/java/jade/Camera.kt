package jade

import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f

class Camera(var position: Vector2f) {
    private val projectionMatrix: Matrix4f = Matrix4f()
    private val inverseProjection: Matrix4f = Matrix4f()
    private val viewMatrix: Matrix4f = Matrix4f()
    private val inverseView: Matrix4f = Matrix4f()
    private var projectionSize = Vector2f(32f * 40f, 32f * 21f)

    fun getProjectionMatrix(): Matrix4f {
        return projectionMatrix
    }

    init {
        adjustProjection()
    }

    fun adjustProjection() {
        projectionMatrix.identity()
        projectionMatrix.ortho(0.0f, projectionSize.x, 0.0f, projectionSize.y, 0.0f, 100.0f)
        projectionMatrix.invert(inverseProjection)
    }

    fun getViewMatrix(): Matrix4f {
        val cameraFront = Vector3f(0.0f, 0.0f, -1.0f)
        val cameraUp = Vector3f(0.0f, 1.0f, 0.0f)
        viewMatrix.identity()
        viewMatrix.lookAt(
            Vector3f(position.x, position.y, 20.0f),
            cameraFront.add(position.x, position.y, 0.0f),
            cameraUp
        )
        viewMatrix.invert(inverseView)
        return viewMatrix
    }

    fun getInverseProjection(): Matrix4f {
        return inverseProjection
    }

    fun getInverseView(): Matrix4f {
        return inverseView
    }

    fun getProjectionSize(): Vector2f {
        return projectionSize
    }
}