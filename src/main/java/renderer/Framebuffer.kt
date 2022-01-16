package renderer

import org.lwjgl.opengl.GL30.*

class Framebuffer {
    private var width = 0
    private var height = 0
    private var fboID = 0
    private var texture: Texture? = null

    constructor(width: Int, height: Int) {
        this.width = width
        this.height = height
        // Generate Framebuffer
        fboID = glGenFramebuffers()
        glBindFramebuffer(GL_FRAMEBUFFER, fboID)

        // Create the texture to render the data to, and attach it to our framebuffer
        texture = Texture(width, height)

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.texture!!.getID(), 0)

        // Create renderbuffer store depth info
        val rboID = glGenRenderbuffers()
        glBindRenderbuffer(GL_RENDERBUFFER, rboID)
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32, width, height)
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboID)

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            assert(false) { "Error: Framebuffer is not complete" }
        }
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
    }

    fun getTextureID(): Int = texture?.getID() ?: -1

    fun getFboID(): Int = this.fboID

    fun bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, fboID)
    }

    fun unBind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0)

    }

}