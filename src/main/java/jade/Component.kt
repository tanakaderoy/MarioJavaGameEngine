package jade

abstract class Component {
    @Transient
    lateinit var gameObject: GameObject
    open fun update(dt: Float) {}
    open fun start() {}
    open fun imgui() {}
}