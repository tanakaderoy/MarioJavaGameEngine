package jade

import org.lwjgl.glfw.GLFW

class MouseListener private constructor() {
    private var scrollX = 0.0
    private var scrollY = 0.0
    private var xPos = 0.0
    private var yPos = 0.0
    private var lastY = 0.0
    private var lastX = 0.0
    private val mouseButtonPressed = BooleanArray(3)
    private var isDragging = false

    companion object {
        private var instance: MouseListener? = null
        fun get(): MouseListener? {
            if (instance == null) instance = MouseListener()
            return instance
        }

        fun mousePosCallback(window: Long, xPos: Double, yPos: Double) {
            get()!!.lastX = get()!!.xPos
            get()!!.lastY = get()!!.yPos
            get()!!.xPos = xPos
            get()!!.yPos = yPos
            get()!!.isDragging =
                get()!!.mouseButtonPressed[0] || get()!!.mouseButtonPressed[1] || get()!!.mouseButtonPressed[2]
        }

        fun mouseButtonCallback(window: Long, button: Int, action: Int, modifier: Int) {
            val mouse = get()
            if (action == GLFW.GLFW_PRESS) {
                if (button < mouse!!.mouseButtonPressed.size) mouse.mouseButtonPressed[button] = true
            } else if (action == GLFW.GLFW_RELEASE) {
                if (button < mouse!!.mouseButtonPressed.size) {
                    mouse.mouseButtonPressed[button] = false
                    mouse.isDragging = false
                }
            }
        }

        fun mouseScrollCallback(window: Long, xOffset: Double, yOffset: Double) {
            val mouse = get()
            mouse!!.scrollY = yOffset
            mouse.scrollX = xOffset
        }

        fun endFrame() {
            val mouse = get()
            mouse!!.scrollY = 0.0
            mouse.scrollX = 0.0
            mouse.lastY = mouse.yPos
            mouse.lastX = mouse.xPos
        }

        val dx: Float
            get() = (get()!!.lastX - get()!!.xPos).toFloat()
        val dy: Float
            get() = (get()!!.lastY - get()!!.yPos).toFloat()
        val x: Float
            get() = get()!!.xPos.toFloat()
        val y: Float
            get() = get()!!.yPos.toFloat()

        fun getScrollX(): Float {
            return get()!!.scrollX.toFloat()
        }

        fun getScrollY(): Float {
            return get()!!.scrollY.toFloat()
        }

        fun isDragging(): Boolean {
            return get()!!.isDragging
        }

        fun mouseButtonDown(button: Int): Boolean {
            return if (button < get()!!.mouseButtonPressed.size) {
                get()!!.mouseButtonPressed[button]
            } else {
                false
            }
        }
    }
}