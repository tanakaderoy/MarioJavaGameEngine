package physics2d

import org.joml.Vector2f
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test


import physics2d.rigidbody.IntersectionDetector2D
import renderer.Line2D

class CollisionDetectorTests {
    companion object {
        const val EPSILON = 0.000001f
        
    }

    @Test
    fun `start point on line should return true test`() {
        val line = Line2D(Vector2f(0f, 0f), Vector2f(12f, 4f))
        val point = Vector2f(0f, 0f)

        assertTrue(IntersectionDetector2D.pointOnLine(point, line))
    }

    @Test
    fun `end point on line should return true test`() {
        val line = Line2D(Vector2f(0f, 0f), Vector2f(12f, 4f))
        val point = Vector2f(12f, 4f)

        assertTrue(IntersectionDetector2D.pointOnLine(point, line))
    }

    @Test
    fun `point on vertical line should return true test`() {
        val line = Line2D(Vector2f(0f, 0f), Vector2f(0f, 10f))
        val point = Vector2f(0f, 5f)

        assertTrue(IntersectionDetector2D.pointOnLine(point, line))
    }
}