package jade

import imgui.ImFontConfig
import imgui.ImGui
import imgui.ImGuiFreeType
import imgui.callback.ImStrConsumer
import imgui.callback.ImStrSupplier
import imgui.flag.*
import imgui.gl3.ImGuiImplGl3
import imgui.type.ImBoolean
import jade.KeyListener.Companion.keyCallBack
import jade.MouseListener.Companion.mouseButtonCallback
import jade.Window.Companion.getHeight
import jade.Window.Companion.getWidth
import org.lwjgl.glfw.GLFW
import scenes.Scene


class ImGuiLayer(private val glfwWindow: Long) {
    // Mouse cursors provided by GLFW
    private val mouseCursors = LongArray(ImGuiMouseCursor.COUNT)

    // LWJGL3 renderer (SHOULD be initialized)
    private val imGuiGl3 = ImGuiImplGl3()

    // Initialize Dear ImGui.
    fun initImGui() {
        // IMPORTANT!!
        // This line is critical for Dear ImGui to work.
        ImGui.createContext()

        // ------------------------------------------------------------
        // Initialize ImGuiIO config
        val io = ImGui.getIO()
        io.iniFilename = "imgui.ini" // We don't want to save .ini file
        io.configFlags = ImGuiConfigFlags.NavEnableKeyboard // Navigation with keyboard
        io.configFlags = ImGuiConfigFlags.DockingEnable //  Enable Docking
        io.backendFlags = ImGuiBackendFlags.HasMouseCursors // Mouse cursors to display while resizing windows etc.
        io.backendPlatformName = "imgui_java_impl_glfw"

        // ------------------------------------------------------------
        // Keyboard mapping. ImGui will use those indices to peek into the io.KeysDown[] array.
        val keyMap = IntArray(ImGuiKey.COUNT)
        keyMap[ImGuiKey.Tab] = GLFW.GLFW_KEY_TAB
        keyMap[ImGuiKey.LeftArrow] = GLFW.GLFW_KEY_LEFT
        keyMap[ImGuiKey.RightArrow] = GLFW.GLFW_KEY_RIGHT
        keyMap[ImGuiKey.UpArrow] = GLFW.GLFW_KEY_UP
        keyMap[ImGuiKey.DownArrow] = GLFW.GLFW_KEY_DOWN
        keyMap[ImGuiKey.PageUp] = GLFW.GLFW_KEY_PAGE_UP
        keyMap[ImGuiKey.PageDown] = GLFW.GLFW_KEY_PAGE_DOWN
        keyMap[ImGuiKey.Home] = GLFW.GLFW_KEY_HOME
        keyMap[ImGuiKey.End] = GLFW.GLFW_KEY_END
        keyMap[ImGuiKey.Insert] = GLFW.GLFW_KEY_INSERT
        keyMap[ImGuiKey.Delete] = GLFW.GLFW_KEY_DELETE
        keyMap[ImGuiKey.Backspace] = GLFW.GLFW_KEY_BACKSPACE
        keyMap[ImGuiKey.Space] = GLFW.GLFW_KEY_SPACE
        keyMap[ImGuiKey.Enter] = GLFW.GLFW_KEY_ENTER
        keyMap[ImGuiKey.Escape] = GLFW.GLFW_KEY_ESCAPE
        keyMap[ImGuiKey.KeyPadEnter] = GLFW.GLFW_KEY_KP_ENTER
        keyMap[ImGuiKey.A] = GLFW.GLFW_KEY_A
        keyMap[ImGuiKey.C] = GLFW.GLFW_KEY_C
        keyMap[ImGuiKey.V] = GLFW.GLFW_KEY_V
        keyMap[ImGuiKey.X] = GLFW.GLFW_KEY_X
        keyMap[ImGuiKey.Y] = GLFW.GLFW_KEY_Y
        keyMap[ImGuiKey.Z] = GLFW.GLFW_KEY_Z
        io.setKeyMap(keyMap)

        // ------------------------------------------------------------
        // Mouse cursors mapping
        mouseCursors[ImGuiMouseCursor.Arrow] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR)
        mouseCursors[ImGuiMouseCursor.TextInput] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_IBEAM_CURSOR)
        mouseCursors[ImGuiMouseCursor.ResizeAll] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR)
        mouseCursors[ImGuiMouseCursor.ResizeNS] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_VRESIZE_CURSOR)
        mouseCursors[ImGuiMouseCursor.ResizeEW] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HRESIZE_CURSOR)
        mouseCursors[ImGuiMouseCursor.ResizeNESW] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR)
        mouseCursors[ImGuiMouseCursor.ResizeNWSE] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR)
        mouseCursors[ImGuiMouseCursor.Hand] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR)
        mouseCursors[ImGuiMouseCursor.NotAllowed] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR)

        // ------------------------------------------------------------
        // GLFW callbacks to handle user input
        GLFW.glfwSetKeyCallback(glfwWindow) { w: Long, key: Int, scancode: Int, action: Int, mods: Int ->
            if (action == GLFW.GLFW_PRESS) {
                io.setKeysDown(key, true)
            } else if (action == GLFW.GLFW_RELEASE) {
                io.setKeysDown(key, false)
            }
            io.keyCtrl = io.getKeysDown(GLFW.GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW.GLFW_KEY_RIGHT_CONTROL)
            io.keyShift = io.getKeysDown(GLFW.GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW.GLFW_KEY_RIGHT_SHIFT)
            io.keyAlt = io.getKeysDown(GLFW.GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW.GLFW_KEY_RIGHT_ALT)
            io.keySuper = io.getKeysDown(GLFW.GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW.GLFW_KEY_RIGHT_SUPER)
            if (!io.wantCaptureKeyboard) {
                keyCallBack(w, key, scancode, action, mods)
            }
        }
        GLFW.glfwSetCharCallback(glfwWindow) { w: Long, c: Int ->
            if (c != GLFW.GLFW_KEY_DELETE) {
                io.addInputCharacter(c)
            }
        }
        GLFW.glfwSetMouseButtonCallback(glfwWindow) { w: Long, button: Int, action: Int, mods: Int ->
            val mouseDown = BooleanArray(5)
            mouseDown[0] = button == GLFW.GLFW_MOUSE_BUTTON_1 && action != GLFW.GLFW_RELEASE
            mouseDown[1] = button == GLFW.GLFW_MOUSE_BUTTON_2 && action != GLFW.GLFW_RELEASE
            mouseDown[2] = button == GLFW.GLFW_MOUSE_BUTTON_3 && action != GLFW.GLFW_RELEASE
            mouseDown[3] = button == GLFW.GLFW_MOUSE_BUTTON_4 && action != GLFW.GLFW_RELEASE
            mouseDown[4] = button == GLFW.GLFW_MOUSE_BUTTON_5 && action != GLFW.GLFW_RELEASE
            io.setMouseDown(mouseDown)
            if (!io.wantCaptureMouse && mouseDown[1]) {
                ImGui.setWindowFocus(null)
            }
            if (!io.wantCaptureMouse) {
                mouseButtonCallback(w, button, action, mods)
            }
        }
        GLFW.glfwSetScrollCallback(glfwWindow) { w: Long, xOffset: Double, yOffset: Double ->
            io.mouseWheelH = io.mouseWheelH + xOffset.toFloat()
            io.mouseWheel = io.mouseWheel + yOffset.toFloat()
        }
        io.setSetClipboardTextFn(object : ImStrConsumer() {
            override fun accept(s: String) {
                GLFW.glfwSetClipboardString(glfwWindow, s)
            }
        })
        io.setGetClipboardTextFn(object : ImStrSupplier() {
            override fun get(): String {
                val clipboardString = GLFW.glfwGetClipboardString(glfwWindow)
                return clipboardString ?: ""
            }
        })

        // ------------------------------------------------------------
        // Fonts configuration
        // Read: https://raw.githubusercontent.com/ocornut/imgui/master/docs/FONTS.txt
        val fontAtlas = io.fonts
        val fontConfig = ImFontConfig() // Natively allocated object, should be explicitly destroyed

        // Glyphs could be added per-font as well as per config used globally like here
        fontConfig.glyphRanges = fontAtlas.glyphRangesDefault


        // Fonts merge example
        fontConfig.pixelSnapH = true
        fontAtlas.addFontFromFileTTF("assets/fonts/segoeui.ttf", 32f, fontConfig)
        fontConfig.destroy() // After all fonts were added we don't need this config more

        // ------------------------------------------------------------
        // Use freetype instead of stb_truetype to build a fonts texture
        ImGuiFreeType.buildFontAtlas(fontAtlas, ImGuiFreeType.RasterizerFlags.LightHinting)

        // Method initializes LWJGL3 renderer.
        // This method SHOULD be called after you've initialized your ImGui configuration (fonts and so on).
        // ImGui context should be created as well.
        imGuiGl3.init("#version 330 core")
    }

    fun update(dt: Float, currentScene: Scene) {
        startFrame(dt)
        // Any Dear ImGui code SHOULD go between ImGui.newFrame()/ImGui.render() methods
        ImGui.newFrame()
        setupDockspace()
        currentScene.sceneImgui()
        ImGui.showDemoWindow()
        ImGui.end()
        ImGui.render()
        endFrame()
    }

    private fun startFrame(deltaTime: Float) {
        // Get window properties and mouse position
        val winWidth = floatArrayOf(getWidth().toFloat())
        val winHeight = floatArrayOf(getHeight().toFloat())
        val mousePosX = doubleArrayOf(0.0)
        val mousePosY = doubleArrayOf(0.0)
        GLFW.glfwGetCursorPos(glfwWindow, mousePosX, mousePosY)

        // We SHOULD call those methods to update Dear ImGui state for the current frame
        val io = ImGui.getIO()
        io.setDisplaySize(winWidth[0], winHeight[0])
        io.setDisplayFramebufferScale(1f, 1f)
        io.setMousePos(mousePosX[0].toFloat(), mousePosY[0].toFloat())
        io.deltaTime = deltaTime

        // Update the mouse cursor
        val imguiCursor = ImGui.getMouseCursor()
        GLFW.glfwSetCursor(glfwWindow, mouseCursors[imguiCursor])
        GLFW.glfwSetInputMode(glfwWindow, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL)
    }

    private fun endFrame() {
        // After Dear ImGui prepared a draw data, we use it in the LWJGL3 renderer.
        // At that moment ImGui will be rendered to the current OpenGL context.
        imGuiGl3.render(ImGui.getDrawData())
    }

    // If you want to clean a room after yourself - do it by yourself
    private fun destroyImGui() {
        imGuiGl3.dispose()
        ImGui.destroyContext()
    }

    private fun setupDockspace() {
        var windowFlags = ImGuiWindowFlags.MenuBar or ImGuiWindowFlags.NoDocking
        ImGui.setNextWindowPos(0.0f, 0.0f, ImGuiCond.Always)
        ImGui.setNextWindowSize(getWidth().toFloat(), getHeight().toFloat())
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f)
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f)
        windowFlags = windowFlags or (ImGuiWindowFlags.NoTitleBar or ImGuiWindowFlags.NoCollapse or
                ImGuiWindowFlags.NoResize or ImGuiWindowFlags.NoMove or
                ImGuiWindowFlags.NoBringToFrontOnFocus or ImGuiWindowFlags.NoNavFocus)
        ImGui.begin("Dockspace Demo", ImBoolean(true), windowFlags)
        ImGui.popStyleVar(2)

        // Dockspace
        ImGui.dockSpace(ImGui.getID("Dockspace"))
    }
}