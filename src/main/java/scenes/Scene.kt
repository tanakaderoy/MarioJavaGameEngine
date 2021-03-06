package scenes

import com.google.gson.GsonBuilder
import components.Component
import components.ComponentTypeAdapter
import imgui.ImGui
import jade.Camera
import jade.GameObject
import jade.GameObjectTypeAdapter
import renderer.Renderer
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.function.Consumer
import kotlin.math.max

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
        var maxGoID = -1
        var maxCompID = -1
        try {
            val file = File(Paths.get("level.json").toUri())
            if (!file.exists()) {
                file.createNewFile()
            }
            inFile = String(Files.readAllBytes(file.toPath()))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        var objs: List<GameObject> = ArrayList()
        if (inFile.isNotEmpty()) {
            objs = gson.fromJson(inFile, Array<GameObject>::class.java).toList()
            objs.forEach {
                addGameObjectToScene(it)
                it.getAllComponents().forEach { c ->
//                    maxCompID = if (c.getUid() > maxCompID) c.getUid() else maxCompID
                    maxCompID = max(c.getUid(), maxCompID)
                }
//                maxGoID = if (it.getUid() > maxGoID) it.getUid() else maxGoID
                maxGoID = max(it.getUid(), maxGoID)
            }

        }
        maxGoID++
        maxCompID++
        GameObject.init(maxGoID)
        Component.init(maxCompID)
        this.levelLoaded = objs.isNotEmpty()
    }
}
