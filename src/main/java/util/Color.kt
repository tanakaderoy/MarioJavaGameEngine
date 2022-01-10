package util

import org.joml.Vector3f

class Color(var r: Float, var g: Float, var b: Float, var a: Float) {
    override fun toString(): String {
        return "Color{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                ", a=" + a +
                '}'
    }

    companion object {
        var white = Color(1f, 1f, 1f, 1f)
        var black = Color(0f, 0f, 0f, 1f)
        var red = Color(1f, 0f, 0f, 1f)
        var green = Color(0f, 1f, 0f, 1f)
        var blue = Color(0f, 0f, 1f, 1f)
    }

    fun toVec3f(): Vector3f {
        return Vector3f(this.r, this.g, this.b)
    }
}