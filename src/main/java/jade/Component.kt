package jade

abstract class Component {
    lateinit var gameObject: GameObject
    open fun update(dt: Float) {}
    open fun start() {}
    open fun imgui() {}
}