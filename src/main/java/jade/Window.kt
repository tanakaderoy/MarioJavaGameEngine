package jade

import org.lwjgl.Version
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWWindowSizeCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.system.MemoryUtil
import util.Color

class Window private constructor() {
    private val title = "Mario"
    private var width = 1920
    private var height = 1080
    private var glfwWindow: Long = 0
    var backgroundColor: Color = Color.Companion.white
    private lateinit var imguiLayer: ImGuiLayer
    fun run() {
        println("Hello LWJGL " + Version.getVersion() + "!")
        init()
        loop()

        //Free the memory
        Callbacks.glfwFreeCallbacks(glfwWindow)
        GLFW.glfwDestroyWindow(glfwWindow)

        //Terminate GLFW and free callbacks
        GLFW.glfwTerminate()
        GLFW.glfwSetErrorCallback(null)?.free()
    }

    fun init() {
        //Setup Error Callback
        GLFWErrorCallback.createPrint(System.err).set()

        // Init GLFW
        check(GLFW.glfwInit()) { "Unable to initialize GLFW" }
        // Configure GLFW
        GLFW.glfwDefaultWindowHints() // optional, the current window hints are already the default
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE) // the window will stay hidden after creation
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE) // the window will be resizable

        //Create the window
        glfwWindow = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL)

//        // Get the thread stack and push a new frame
//        try (MemoryStack stack = stackPush()) {
//            IntBuffer pWidth = stack.mallocInt(1); // int*
//            IntBuffer pHeight = stack.mallocInt(1); // int*
//
//            // Get the window size passed to glfwCreateWindow
//            glfwGetWindowSize(glfwWindow, pWidth, pHeight);
//
//            // Get the resolution of the primary monitor
//            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
//
//            // Center the window
//            glfwSetWindowPos(
//                    glfwWindow,
//                    (vidmode.width() - pWidth.get(0)) / 2,
//                    (vidmode.height() - pHeight.get(0)) / 2
//            );
//        } // the stack frame is popped automatically

        //Set events callbacks
        GLFW.glfwSetCursorPosCallback(glfwWindow, MouseListener.Companion::mousePosCallback)
        GLFW.glfwSetMouseButtonCallback(glfwWindow, MouseListener.Companion::mouseButtonCallback)
        GLFW.glfwSetScrollCallback(glfwWindow, MouseListener.Companion::mouseScrollCallback)
        GLFW.glfwSetKeyCallback(glfwWindow, KeyListener.Companion::keyCallBack)
        GLFW.glfwSetWindowSizeCallback(glfwWindow, object : GLFWWindowSizeCallback() {
            override fun invoke(window: Long, width: Int, height: Int) {
                setWidth(width)
                setHeight(height)
            }

        })
        check(glfwWindow != MemoryUtil.NULL) { "Failed to create glfw Window" }

        //Make OpenGL Context current
        GLFW.glfwMakeContextCurrent(glfwWindow)
        //Enable V-Sync
        GLFW.glfwSwapInterval(1)

        //Make the window visible
        GLFW.glfwShowWindow(glfwWindow)

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities()
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        this.imguiLayer = ImGuiLayer(glfwWindow)
        imguiLayer.initImGui()
        changeScene(SceneType.LEVELEDITORSCENE)
    }

    fun loop() {
        var beginTime = GLFW.glfwGetTime().toFloat()
        var endTime: Float
        var dt = -1.0f
        while (!GLFW.glfwWindowShouldClose(glfwWindow)) {
            //Poll events
            GLFW.glfwPollEvents()
            GL11.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a)
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT)
            if (dt >= 0) scene.update(dt)
            imguiLayer.update(dt, scene)
            GLFW.glfwSwapBuffers(glfwWindow)
            endTime = GLFW.glfwGetTime().toFloat()
            dt = endTime - beginTime
            beginTime = endTime
        }
    }

    companion object {
        private var instance: Window? = null
        lateinit var scene: Scene
            private set

        fun get(): Window {
            if (instance == null) {
                instance = Window()
            }
            return instance!!
        }

        @JvmStatic
        fun getWidth(): Int {
            return get().width
        }

        @JvmStatic
        fun getHeight(): Int {
            return get().height
        }

        fun changeScene(newScene: SceneType) {
            when (newScene) {
                SceneType.LEVELSCENE -> {
                    scene = LevelScene()
                    (scene as LevelScene).init()
                    (scene as LevelScene).start()
                }
                SceneType.LEVELEDITORSCENE -> {
                    scene = LevelEditorScene()
                    (scene as LevelEditorScene).init()
                    (scene as LevelEditorScene).start()
                }
                else -> {
                    assert(false) { "Unkown Scene '$newScene'" }
                }
            }
        }

        @JvmStatic
        fun setWidth(width: Int) {
            get().width = width
        }

        @JvmStatic
        fun setHeight(height: Int) {
            get().height = height
        }
    }
}