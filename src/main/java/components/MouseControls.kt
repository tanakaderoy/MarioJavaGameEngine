package components

import jade.GameObject
import jade.MouseListener
import jade.Window
import org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT
import util.Settings


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
            holdingObject!!.transform.position.x = MouseListener.getOrthoX()
            holdingObject!!.transform.position.y = MouseListener.getOrthoY()
            holdingObject!!.transform.position.x =
                ((holdingObject!!.transform.position.x / Settings.GRID_WIDTH).toInt() * Settings.GRID_WIDTH).toFloat()
            holdingObject!!.transform.position.y =
                ((holdingObject!!.transform.position.y / Settings.GRID_HEIGHT).toInt() * Settings.GRID_HEIGHT).toFloat()
            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                place()
            }
        }
    }
}