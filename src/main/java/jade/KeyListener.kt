package jade

import org.lwjgl.glfw.GLFW

class KeyListener private constructor() {
    private val keyPressed = BooleanArray(350)

    companion object {
        private var instance: KeyListener? = null
        fun get(): KeyListener? {
            if (instance == null) instance = KeyListener()
            return instance
        }

        fun keyCallBack(window: Long, key: Int, scancode: Int, action: Int, modifier: Int) {
            if (action == GLFW.GLFW_PRESS) {
                get()!!.keyPressed[key] = true
            } else if (action == GLFW.GLFW_RELEASE) {
                get()!!.keyPressed[key] = false
            }
        }

        fun isKeyPressed(keyCode: Int): Boolean {
            return get()!!.keyPressed[keyCode]
        }
    }
}