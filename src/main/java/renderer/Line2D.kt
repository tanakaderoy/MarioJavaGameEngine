package renderer

import org.joml.Vector2f
import org.joml.Vector3f

class Line2D(from: Vector2f, to: Vector2f, color: Vector3f, private var lifetime: Int) {
    var from: Vector2f = from
        private set
    var to: Vector2f = to
        private set
    var color: Vector3f = color
        private set

    fun beginFrame(): Int {
        this.lifetime--
        return lifetime
    }

}