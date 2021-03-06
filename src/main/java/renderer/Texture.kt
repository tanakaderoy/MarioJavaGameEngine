package renderer

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.*
import org.lwjgl.stb.STBImage

class Texture {
    @Transient
    private var texID: Int = 0;
    private lateinit var filepath: String
    var width: Int = 0
    var height: Int = -1

    constructor(width: Int, height: Int) {
        this.width = width
        this.height = height

        this.filepath = "Generated"
        // Generate texture on GPU
        texID = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, texID)

        glTexImage2D(
            GL_TEXTURE_2D, 0, GL_RGBA, width, height,
            0, GL_RGB, GL_UNSIGNED_BYTE, 0
        )

    }

    constructor() {
        texID = -1
        width = -1
        height = -1
    }

    fun initialize(filePath: String) {
        this.filepath = filePath
        // Generate texture on GPU
        texID = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, texID)

        // Set texture parameters
        // Repeat image in both directions
        GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT)
        GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT)
        // When stretching the image, pixelate
        GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST)
        // When shrinking an image, pixelate
        GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)
        val width = BufferUtils.createIntBuffer(1)
        val height = BufferUtils.createIntBuffer(1)
        val channels = BufferUtils.createIntBuffer(1)
        STBImage.stbi_set_flip_vertically_on_load(true)
        val image = STBImage.stbi_load(filepath, width, height, channels, 0)
        if (image != null) {
            this.width = width[0]
            this.height = height[0]
            if (channels[0] == 3) {
                glTexImage2D(
                    GL_TEXTURE_2D, 0, GL_RGB, width[0], height[0],
                    0, GL_RGB, GL_UNSIGNED_BYTE, image
                )
            } else if (channels[0] == 4) {
                glTexImage2D(
                    GL_TEXTURE_2D, 0, GL_RGBA, width[0], height[0],
                    0, GL_RGBA, GL_UNSIGNED_BYTE, image
                )
            } else {
                assert(false) { "Error: (Texture) Unknown number of channels '%s'".formatted(channels[0]) }
            }
        } else {
            assert(false) { "Error: (Texture) Could not load image '%s'".formatted(filepath) }
        }
        STBImage.stbi_image_free(image)
    }

    fun bind() {
        glBindTexture(GL_TEXTURE_2D, texID)
    }

    fun unbind() {
        glBindTexture(GL_TEXTURE_2D, 0)
    }

    fun getID(): Int {
        return texID
    }

    fun getFilePath(): String {
        return filepath
    }

    override fun equals(o: Any?): Boolean {
        if (o == null) return false
        if (o !is Texture) return false
        return o.width == this.width &&
                o.height == this.height &&
                o.getID() == this.texID &&
                o.getFilePath() == this.filepath
    }

}