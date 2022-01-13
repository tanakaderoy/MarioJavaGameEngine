package util

import org.joml.Vector2f
import java.lang.Math.toRadians
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

object JMath {
    fun rotate(vec: Vector2f, angleDeg: Float, origin: Vector2f) {
        val x = vec.x - origin.x
        val y = vec.y - origin.y
        val cos = cos(toRadians(angleDeg.toDouble())).toFloat()
        val sin = sin(toRadians(angleDeg.toDouble())).toFloat()
        var xPrime = x * cos - y * sin
        var yPrime = x * sin + y * cos
        xPrime += origin.x
        yPrime += origin.y
        vec.x = xPrime
        vec.y = yPrime
    }

    fun compare(x: Float, y: Float, epsilon: Float): Boolean {
        return abs(x - y) <= epsilon * max(1.0f, max(abs(x), abs(y)))
    }

    fun compare(vec1: Vector2f, vec2: Vector2f, epsilon: Float): Boolean {
        return compare(vec1.x, vec2.x, epsilon) && compare(vec1.y, vec2.y, epsilon)
    }

    fun compare(x: Float, y: Float): Boolean {
        return abs(x - y) <= Float.MIN_VALUE * max(1.0f, max(abs(x), abs(y)))
    }

    fun compare(vec1: Vector2f, vec2: Vector2f): Boolean {
        return compare(vec1.x, vec2.x) && compare(vec1.y, vec2.y)
    }
}