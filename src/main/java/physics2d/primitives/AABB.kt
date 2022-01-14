package physics2d.primitives

import org.joml.Vector2f
import physics2d.rigidbody.RigidBody2D

/**
 * Axis Aligned Bounding Box
 */
class AABB() {
    var size = Vector2f()
    var rigidBody: RigidBody2D? = null
    private val _rigidBody: RigidBody2D
        get() = rigidBody!!
    var halfSize: Vector2f

    init {
        this.halfSize = Vector2f(size).mul(0.5f)
    }

    constructor(min: Vector2f, max: Vector2f) : this() {
        size = Vector2f(max).sub(min)
        this.halfSize = Vector2f(size).mul(0.5f)

    }

    fun getMin(): Vector2f {
        return Vector2f(_rigidBody.position.sub(halfSize))
    }

    fun getMax(): Vector2f {
        return Vector2f(_rigidBody.position).add(halfSize)
    }

}