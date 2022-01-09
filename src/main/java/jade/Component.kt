package jade

import imgui.ImGui
import org.joml.Vector3f
import org.joml.Vector4f
import java.lang.reflect.Modifier

abstract class Component {
    @Transient
    lateinit var gameObject: GameObject
    open fun start() {}
    open fun update(dt: Float) {}
    open fun imgui() {
        try {
            val fields = this.javaClass.declaredFields
            for (field in fields) {
                val isTransient = Modifier.isTransient(field.modifiers)
                if (isTransient) {
                    continue
                }
                val isPrivate = Modifier.isPrivate(field.modifiers)
                if (isPrivate) {
                    field.isAccessible = true
                }
                val type = field.type
                val value = field[this]
                val name = field.name
                if (type == Int::class.javaPrimitiveType) {
                    val `val` = value as Int
                    val imInt = intArrayOf(`val`)
                    if (ImGui.dragInt("$name: ", imInt)) {
                        field[this] = imInt[0]
                    }
                } else if (type == Float::class.javaPrimitiveType) {
                    val `val` = value as Float
                    val imFloat = floatArrayOf(`val`)
                    if (ImGui.dragFloat("$name: ", imFloat)) {
                        field[this] = imFloat[0]
                    }
                } else if (type == Boolean::class.javaPrimitiveType) {
                    val `val` = value as Boolean
                    if (ImGui.checkbox("$name: ", `val`)) {
                        field[this] = !`val`
                    }
                } else if (type == Vector3f::class.java) {
                    val `val` = value as Vector3f
                    val imVec = floatArrayOf(`val`.x, `val`.y, `val`.z)
                    if (ImGui.dragFloat3("$name: ", imVec)) {
                        `val`[imVec[0], imVec[1]] = imVec[2]
                    }
                } else if (type == Vector4f::class.java) {
                    val `val` = value as Vector4f
                    val imVec = floatArrayOf(`val`.x, `val`.y, `val`.z, `val`.w)
                    if (ImGui.dragFloat4("$name: ", imVec)) {
                        `val`[imVec[0], imVec[1], imVec[2]] = imVec[3]
                    }
                }
                if (isPrivate) {
                    field.isAccessible = false
                }
            }
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }
}