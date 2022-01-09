package jade

import org.joml.Vector2f

class Transform {
    lateinit var position: Vector2f
    lateinit var scale: Vector2f

    constructor() {
        init(Vector2f(), Vector2f())
    }

    fun init(position: Vector2f, scale: Vector2f) {
        this.position = position
        this.scale = scale
    }

    fun copy(): Transform {
        return Transform(Vector2f(position), Vector2f(scale))
    }

    fun copy(to: Transform) {
        to.position.set(position)
        to.scale.set(scale)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val transform = other as Transform
        return if (position != transform.position) false else scale == transform.scale
    }

    override fun hashCode(): Int {
        var result = position.hashCode()
        result = 31 * result + scale.hashCode()
        return result
    }

    constructor(position: Vector2f) {
        init(position, Vector2f())
    }

    constructor(position: Vector2f, scale: Vector2f) {
        init(position, scale)
    }
}