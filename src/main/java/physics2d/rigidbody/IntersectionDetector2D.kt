package physics2d.rigidbody

import Box2D
import org.joml.Vector2f
import physics2d.primitives.AABB
import physics2d.primitives.Circle
import renderer.Line2D
import util.JMath

object IntersectionDetector2D {
    // ========================================
    // Point vs. Primitive Tests
    // ========================================

    fun pointOnLine(point: Vector2f, line: Line2D): Boolean {
        val dy = line.getEnd().y - line.getStart().y
        val dx = line.getEnd().x - line.getStart().x
        if (dx == 0f) {
            return JMath.compare(point.x, line.getStart().x)
        }
        val m = dy / dx
        println(m)
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

    fun pointInBox2D(point: Vector2f, box: Box2D): Boolean {
        val min = box.getMin()
        val max = box.getMax()
        val pointLocalBoxSpace = Vector2f(point)
        JMath.rotate(
            pointLocalBoxSpace, box.rigidBody!!.rotation,
            box.rigidBody!!.position
        )
        return pointLocalBoxSpace.x <= max.x && min.x <= pointLocalBoxSpace.x &&
                pointLocalBoxSpace.y <= max.y && min.y <= pointLocalBoxSpace.y
    }

    // ========================================
    // Line vs. Primitive Tests
    // ========================================

    fun lineAndCircle(line: Line2D, circle: Circle): Boolean {
        if (pointInCirlce(line.getStart(), circle) || pointInCirlce(line.getEnd(), circle)) {
            return true
        }

        val ab = Vector2f(line.getEnd()).sub(line.getStart())

        // Project point cir5cle pos onto ab line seg
        // parametized pos t
        val circleCenter = circle.getCenter()
        val centerToLineStart = Vector2f(circleCenter).sub(line.getStart())
        val t = centerToLineStart.dot(ab) / ab.dot(ab)

        if (t < 0.0f || t > 1.0f) {
            return false
        }

        // Find the closest point to the line segment
        val closestPoint = Vector2f(line.getStart()).add(ab.mul(t))
        return pointInCirlce(closestPoint, circle)
    }
}
