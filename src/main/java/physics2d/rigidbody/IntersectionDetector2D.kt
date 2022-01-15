package physics2d.rigidbody

import Box2D
import org.joml.Vector2f
import physics2d.primitives.AABB
import physics2d.primitives.Circle
import renderer.Line2D

class IntersectionDetector2D {
    // ========================================
    // Point vs. Primitive Tests
    // ========================================
    companion object {
        fun pointOnLine(point: Vector2f, line: Line2D): Boolean {
            val dy = line.getEnd().y - line.getStart().y
            val dx = line.getEnd().x - line.getStart().x
            val m = dy / dx

            val b = line.getEnd().y - (m * line.getEnd().x)
            // y = mx + b
            return point.y == m * point.x + b
        }

        fun pointInCirlce(point: Vector2f, circle: Circle): Boolean {
            val circleCenter = circle.getCenter()
            val centerToPoint = Vector2f(point).sub(circleCenter)
            return centerToPoint.lengthSquared() <= circle.radius * circle.radius
        }

        fun pointInAABB(point: Vector2f, box: AABB): Boolean {
            val min = box.getMin()
            val max = box.getMax()
            return point.x >= min.x &&
                    point.x <= max.x &&
                    point.y <= max.y &&
                    point.y >= min.y
        }

        fun pointInBox2D(point: Vector2f, box: Box2D) {

        }
    }

    // ========================================
    // Line vs. Primitive Tests
    // ========================================
}
