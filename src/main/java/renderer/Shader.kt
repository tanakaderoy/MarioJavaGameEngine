package renderer

import org.joml.*
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

class Shader(private val filePath: String) {
    private var shaderProgramID = 0
    private var vertexSource: String? = null
    private var fragmentSource: String? = null
    private var beingUsed = false

    init {
        try {
            val source = String(Files.readAllBytes(Paths.get(filePath)))
            val splitString = source.split("(#type)( )+([a-zA-Z]+)".toRegex())

            // Find the first pattern after #type 'pattern'

            // Find the first pattern after #type 'pattern'
            var index = source.indexOf("#type") + 6
            var eol = source.indexOf("\r\n", index)
            val firstPattern = source.substring(index, eol).trim()

            // Find the second pattern after #type 'pattern'

            // Find the second pattern after #type 'pattern'
            index = source.indexOf("#type", eol) + 6
            eol = source.indexOf("\r\n", index)
            val secondPattern = source.substring(index, eol).trim()
            when (firstPattern) {
                "vertex" -> {
                    vertexSource = splitString[1]
                }
                "fragment" -> {
                    fragmentSource = splitString[1]
                }
                else -> {
                    throw IOException("Unexexpected token: '%s'".formatted(firstPattern))
                }
            }
            when (secondPattern) {
                "vertex" -> {
                    vertexSource = splitString[2]
                }
                "fragment" -> {
                    fragmentSource = splitString[2]
                }
                else -> {
                    throw IOException("Unexexpected token: '%s'".formatted(secondPattern))
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            assert(false) { "Error: Could not open file for shader: '$filePath'" }
        }
    }

    fun compile() {
        val fragmentID: Int
        val vertexID: Int
        // ============================================
        // Compile and link shaders
        // ============================================

        //First load and compile vertex shader
        vertexID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER)
        // Pass shader source to GPU
        GL20.glShaderSource(vertexID, vertexSource)
        GL20.glCompileShader(vertexID)

        // Check for errors in compilation
        var success = GL20.glGetShaderi(vertexID, GL20.GL_COMPILE_STATUS)
        if (success == GL11.GL_FALSE) {
            val len = GL20.glGetShaderi(vertexID, GL20.GL_INFO_LOG_LENGTH)
            System.out.printf("ERROR: '%s'\n\tVertex shader compilation failed", filePath)
            println(GL20.glGetShaderInfoLog(vertexID, len))
            assert(false) { "" }
        }


        //First load and compile fragment shader
        fragmentID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER)
        // Pass shader source to GPU
        GL20.glShaderSource(fragmentID, fragmentSource)
        GL20.glCompileShader(fragmentID)

        // Check for errors in compilation
        success = GL20.glGetShaderi(fragmentID, GL20.GL_COMPILE_STATUS)
        if (success == GL11.GL_FALSE) {
            val len = GL20.glGetShaderi(fragmentID, GL20.GL_INFO_LOG_LENGTH)
            System.out.printf("ERROR: '%s'\n\tFragment shader compilation failed", filePath)
            println(GL20.glGetShaderInfoLog(fragmentID, len))
            assert(false) { "" }
        }

        // Link shader and check errors
        shaderProgramID = GL20.glCreateProgram()
        GL20.glAttachShader(shaderProgramID, vertexID)
        GL20.glAttachShader(shaderProgramID, fragmentID)
        GL20.glLinkProgram(shaderProgramID)

        // Check for errors
        success = GL20.glGetProgrami(shaderProgramID, GL20.GL_LINK_STATUS)
        if (success == GL11.GL_FALSE) {
            val len = GL20.glGetProgrami(shaderProgramID, GL20.GL_INFO_LOG_LENGTH)
            System.out.printf("ERROR: '%s'\n\tLinking of shaders failed%n", filePath)
            println(GL20.glGetProgramInfoLog(shaderProgramID, len))
            assert(false) { "" }
        }
    }

    fun use() {
        if (!beingUsed) {
            // Bind the Shader Program
            GL20.glUseProgram(shaderProgramID)
            beingUsed = true
        }
    }

    fun detach() {
        GL20.glUseProgram(0)
        beingUsed = false
    }

    fun uploadMat4f(varName: String, mat4: Matrix4f) {
        val varLocation = getLocation(varName)
        val matBuffer = BufferUtils.createFloatBuffer(16)
        mat4[matBuffer]
        GL20.glUniformMatrix4fv(varLocation, false, matBuffer)
    }

    fun uploadMat3f(varName: String, mat3: Matrix3f) {
        val varLocation = getLocation(varName)
        val matBuffer = BufferUtils.createFloatBuffer(9)
        mat3[matBuffer]
        GL20.glUniformMatrix3fv(varLocation, false, matBuffer)
    }

    fun uploadVec4f(varName: String, vec: Vector4f) {
        val varLocation = getLocation(varName)
        use()
        GL20.glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w)
    }

    fun uploadVec3f(varName: String, vec: Vector3f) {
        val varLocation = getLocation(varName)
        use()
        GL20.glUniform3f(varLocation, vec.x, vec.y, vec.z)
    }

    fun uploadVec2f(varName: String, vec: Vector2f) {
        val varLocation = getLocation(varName)
        use()
        GL20.glUniform2f(varLocation, vec.x, vec.y)
    }

    fun uploadFloat(varName: String, `val`: Float) {
        val varLocation = getLocation(varName)
        use()
        GL20.glUniform1f(varLocation, `val`)
    }

    fun uploadInt(varName: String, `val`: Int) {
        val varLocation = getLocation(varName)
        use()
        GL20.glUniform1i(varLocation, `val`)
    }

    fun uploadTexture(varName: String, slot: Int) {
        val varLocation = getLocation(varName)
        use()
        GL20.glUniform1i(varLocation, slot)
    }

    private fun getLocation(varName: String): Int {
        return GL20.glGetUniformLocation(shaderProgramID, varName)
    }

    fun uploadIntArray(varName: String, slots: IntArray?) {
        val varLocation = getLocation(varName)
        use()
        GL20.glUniform1iv(varLocation, slots)
    }
}