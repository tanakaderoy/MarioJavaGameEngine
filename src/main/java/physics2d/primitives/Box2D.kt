import org.joml.Vector2f
import physics2d.rigidbody.RigidBody2D
import util.JMath

class Box2D() {
    var size: Vector2f = Vector2f()
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

    fun getVertices(): List<Vector2f> {
        val min = getMin();
        val max = getMax()

        val vertices = listOf(
            Vector2f(min.x, min.y),
            Vector2f(min.x, max.y),
            Vector2f(max.x, min.y),
            Vector2f(max.x, max.y)
        )
        if (_rigidBody.rotation != 0.0f) {
            for (vert in vertices) {
                JMath.rotate(vert, _rigidBody.rotation, _rigidBody.position)
            }
        }

        return vertices

    }
}