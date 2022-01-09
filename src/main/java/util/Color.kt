package util

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
        var black = Color(0f, 0f, 0f, 0f)
    }
}