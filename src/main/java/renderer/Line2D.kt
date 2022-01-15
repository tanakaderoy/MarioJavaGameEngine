package renderer

import org.joml.Vector2f
import org.joml.Vector3f

class Line2D {
    var from: Vector2f = Vector2f()
        private set
    var to: Vector2f = Vector2f()
        private set
    var color: Vector3f = Vector3f()
        private set
    private var lifetime: Int = 1


    fun beginFrame(): Int {
        this.lifetime--
        return lifetime
    }

    constructor(from: Vector2f, to: Vector2f) {
        this.from = from
        this.to = to
    }

    constructor(from: Vector2f, to: Vector2f, color: Vector3f, lifetime: Int) {
        this.from = from
        this.to = to
        this.color = color
        this.lifetime = lifetime
    }

    fun getStart(): Vector2f {
        return from
    }

    fun getEnd(): Vector2f {
        return to
    }

}