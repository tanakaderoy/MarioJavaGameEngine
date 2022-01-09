package jade

import com.google.gson.GsonBuilder
import imgui.ImGui
import renderer.Renderer
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.function.Consumer

abstract class Scene {
    protected var levelLoaded = false
    protected lateinit var camera: Camera
    protected var activeGameObject: GameObject? = null
    private var isRunning = false
    protected var gameObjects: MutableList<GameObject> = ArrayList()
    protected var renderer: Renderer = Renderer()
    open fun init() {

    }

    fun start() {
        gameObjects.forEach(Consumer { go: GameObject ->
            go.start()
            renderer.add(go)
        })
        isRunning = true
    }

    fun addGameObjectToScene(go: GameObject) {
        if (!isRunning) {
            gameObjects.add(go)
        } else {
            gameObjects.add(go)
            go.start()
            renderer.add(go)
        }
    }

    abstract fun update(dt: Float)
    fun camera(): Camera {
        return camera
    }

    fun sceneImgui() {
        activeGameObject?.let {
            ImGui.begin("Inspector")
            it.imgui()
            ImGui.end()
        }
        imgui()
    }

    open fun imgui() {}
    val gson = GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(GameObject::class.java, GameObjectTypeAdapter())
        .registerTypeAdapter(Component::class.java, ComponentTypeAdapter())
        .create()

    fun saveExit() {
        try {
            val writer = FileWriter("level.json")
            writer.write(gson.toJson(this.gameObjects))
            writer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun load() {

        var inFile = ""
        try {
            inFile = String(Files.readAllBytes(Paths.get("level.json")))
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (inFile.isNotEmpty()) {
            val objs = gson.fromJson(inFile, Array<GameObject>::class.java).toList()
            objs.forEach { addGameObjectToScene(it) }
            this.levelLoaded = true
        }
    }
}