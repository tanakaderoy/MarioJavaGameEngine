package physics2d.primitives

import org.joml.Vector2f
import physics2d.rigidbody.RigidBody2D

class Circle {
    var body: RigidBody2D? = null

    var radius: Float = 1.0f
        private set

    fun getCenter(): Vector2f {
        return body?.position ?: Vector2f()

    }

}