package components

import jade.GameObject
import jade.MouseListener
import jade.Window
import org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT

class MouseControls : Component() {
    var holdingObject: GameObject? = null
    fun pickupObject(go: GameObject) {
        holdingObject = go
        Window.scene.addGameObjectToScene(go)
    }

    fun place() {
        holdingObject = null
    }

    override fun update(dt: Float) {
        if (holdingObject != null) {
            holdingObject!!.transform.position.x = MouseListener.getOrthoX() - 16f
            holdingObject!!.transform.position.y = MouseListener.getOrthoY() - 16f
            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                place()
            }
        }
    }
}