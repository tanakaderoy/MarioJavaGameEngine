package physics2d.rigidbody

import components.Component
import org.joml.Vector2f

class RigidBody2D : Component() {
    var position: Vector2f = Vector2f()
    var rotation: Float = 0.0f

}