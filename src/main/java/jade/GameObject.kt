package jade

import java.util.function.Consumer

class GameObject {
    private lateinit var name: String
    private lateinit var components: MutableList<Component>
    lateinit var transform: Transform
    private var zIndex = 0

    constructor(name: String) {
        init(name)
    }

    fun init(name: String) {
        this.name = name
        components = ArrayList()
        transform = Transform()
        zIndex = 0
    }

    fun init(name: String, transform: Transform, zIndex: Int) {
        this.init(name)
        this.transform = transform
        this.zIndex = zIndex
    }

    constructor(name: String, transform: Transform, zIndex: Int) {
        init(name, transform, zIndex)
    }

    fun <T : Component?> getComponent(componentClass: Class<T>): T? {
        for (c in components) {
            if (componentClass.isAssignableFrom(c.javaClass)) {
                try {
                    return componentClass.cast(c)
                } catch (e: ClassCastException) {
                    e.printStackTrace()
                    assert(false) { "Error: Casting component. " }
                }
            }
        }
        return null
    }

    fun <T : Component?> removeComponent(componentClass: Class<T>) {
        for (i in components.indices) {
            val c = components[i]
            if (componentClass.isAssignableFrom(c.javaClass)) {
                components.removeAt(i)
                return
            }
        }
    }

    fun addComponent(component: Component) {
        components.add(component)
        component.gameObject = this
    }

    fun update(dt: Float) {
//        for (int i = 0; i < components.size(); i++) {
//            components.get(i).update(dt);
//        }
        runOnAllComponents { c: Component -> c.update(dt) }
    }

    fun start() {
        runOnAllComponents { obj: Component -> obj.start() }
        //    components.forEach(Component::start);
    }

    private fun runOnAllComponents(action: Consumer<Component>) {
        for (i in components.indices) {
            action.accept(components[i])
        }
    }

    fun getzIndex(): Int {
        return zIndex
    }
}