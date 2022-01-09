package jade

abstract class Component {
    lateinit var gameObject: GameObject
    abstract fun update(dt: Float)
    open fun start() {}
}