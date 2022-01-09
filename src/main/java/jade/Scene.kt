package jade

import renderer.Renderer
import java.util.function.Consumer

abstract class Scene {
    protected lateinit var camera: Camera
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
}