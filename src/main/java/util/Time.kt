package util

import org.lwjgl.glfw.GLFW

object Time {
    val time: Float
        get() = GLFW.glfwGetTime().toFloat()
}