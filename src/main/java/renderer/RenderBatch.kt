package renderer

import components.SpriteRenderer
import jade.Window
import org.lwjgl.opengl.*
import util.AssetPool
import util.Constants
import java.util.function.Consumer

class RenderBatch(maxBatchSize: Int, private val zIndex: Int) : IRenderBatch, Comparable<RenderBatch> {
    /*
     Vertex
     =======
     Pos               Color                     tex coords      tex id
     float, float,     float,float,float,float,  float, float,   float

     */
    private val POS_SIZE = 2
    private val COLOR_SIZE = 4
    private val POS_OFFSET = 0
    private val COLOR_OFFSET = POS_OFFSET + POS_SIZE * java.lang.Float.BYTES
    private val VERTEX_SIZE = 9
    private val VERTEX_SIZE_BYTES = VERTEX_SIZE * java.lang.Float.BYTES
    private val TEX_COORDS_SIZE = 2
    private val TEX_ID_SIZE = 1
    private val TEX_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * java.lang.Float.BYTES
    private val TEX_ID_OFFSET = TEX_COORDS_OFFSET + TEX_COORDS_SIZE * java.lang.Float.BYTES
    private val sprites: Array<SpriteRenderer?>
    private var numSprites: Int
    private var hasRoom: Boolean
    private val vertices: FloatArray
    var texSlots = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7)
    private val textures: MutableList<Texture?>
    private var vaoID = 0
    private var vboID = 0
    private val maxBatchSize: Int
    private val shader: Shader = AssetPool.getShader(Constants.SHADERS_DEFAULT_GLSL)

    init {
        sprites = arrayOfNulls(maxBatchSize)
        this.maxBatchSize = maxBatchSize

        // 4 vertices quads
        vertices = FloatArray(maxBatchSize * 4 * VERTEX_SIZE)
        numSprites = 0
        hasRoom = true
        textures = ArrayList()
    }

    override fun start() {

        // Generate and bind a vertex Array Object
        vaoID = GL30.glGenVertexArrays()
        GL30.glBindVertexArray(vaoID)

        // Allocate space for vertices
        vboID = GL15.glGenBuffers()
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID)
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices.size.toLong() * java.lang.Float.BYTES, GL15.GL_DYNAMIC_DRAW)


        // Create and Upload indices buffer
        val eboID = GL15.glGenBuffers()
        val indices = generateIndices()
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, eboID)
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW)

        // Enable the buffer attribute pointer
        GL20.glVertexAttribPointer(0, POS_SIZE, GL11.GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET.toLong())
        GL20.glEnableVertexAttribArray(0)
        GL20.glVertexAttribPointer(1, COLOR_SIZE, GL11.GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET.toLong())
        GL20.glEnableVertexAttribArray(1)
        GL20.glVertexAttribPointer(
            2, TEX_COORDS_SIZE, GL11.GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET.toLong()
        )
        GL20.glEnableVertexAttribArray(2)
        GL20.glVertexAttribPointer(3, TEX_ID_SIZE, GL11.GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET.toLong())
        GL20.glEnableVertexAttribArray(3)
    }

    override fun addSprite(spr: SpriteRenderer) {
        // Get index and add renderObject
        val index = numSprites
        // [0,1,2,3,4,5]
        sprites[index] = spr
        numSprites++
        if (spr.texture != null) {
            if (!textures.contains(spr.texture)) {
                textures.add(spr.texture)
            }
        }

        // Add properties to local vertices array
        loadVertexProperties(index)
        if (numSprites >= maxBatchSize) {
            hasRoom = false
        }
    }

    override fun generateIndices(): IntArray {
        // 6 indices per quad (3 per triangle)
        val elements = IntArray(6 * maxBatchSize)
        for (i in 0 until maxBatchSize) {
            loadElementIndices(elements, i)
        }
        return elements
    }

    private fun loadElementIndices(elements: IntArray, index: Int) {
        val offsetArrayIndex = 6 * index
        val offset = 4 * index
        // 3,2,0,0,2,1,               7,6,4,4,6,5
        // Triangle 1
        elements[offsetArrayIndex + 0] = offset + 3
        elements[offsetArrayIndex + 1] = offset + 2
        elements[offsetArrayIndex + 2] = offset + 0

        // Triangle 2
        elements[offsetArrayIndex + 3] = offset + 0
        elements[offsetArrayIndex + 4] = offset + 2
        elements[offsetArrayIndex + 5] = offset + 1
    }

    override fun render() {
        var rebufferData = false
        for (i in 0 until numSprites) {
            val spr = sprites[i]
            if (spr!!.isDirty) {
                loadVertexProperties(i)
                spr.setClean()
                rebufferData = true
            }
        }
        if (rebufferData) {
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID)
            GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, vertices)
        }
        // Use Shader
        shader.use()
        Window.scene.camera().let {
            shader.uploadMat4f("uProjection", it.getProjectionMatrix())
            shader.uploadMat4f("uView", it.getViewMatrix())
        }
        for (i in textures.indices) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0 + i + 1)
            textures[i]!!.bind()
        }
        shader.uploadIntArray("uTextures", texSlots)
        GL30.glBindVertexArray(vaoID)
        GL20.glEnableVertexAttribArray(0)
        GL20.glEnableVertexAttribArray(1)
        GL11.glDrawElements(GL11.GL_TRIANGLES, numSprites * 6, GL11.GL_UNSIGNED_INT, 0)
        GL20.glDisableVertexAttribArray(0)
        GL20.glDisableVertexAttribArray(1)
        GL30.glBindVertexArray(0)
        textures.forEach(Consumer { obj: Texture? -> obj!!.unbind() })
        shader.detach()
    }

    override fun loadVertexProperties(index: Int) {
        val sprite = sprites[index]

        // Find offset within array (4 vertices per sprite)
        var offset = index * 4 * VERTEX_SIZE
        val color = sprite!!.color
        val texCoords = sprite.texCoords
        var texID = 0
        if (sprite.texture != null) {
            for (i in textures.indices) {
                if (textures[i] == sprite.texture) {
                    texID = i + 1
                    break
                }
            }
        }


        // Add vertices with the appropriate props;

        // *    *
        // *    *

        // Add vertices with the appropriate properties
        var xAdd = 1.0f
        var yAdd = 1.0f
        for (i in 0..3) {
            if (i == 1) {
                yAdd = 0.0f
            } else if (i == 2) {
                xAdd = 0.0f
            } else if (i == 3) {
                yAdd = 1.0f
            }

            // Load position
            vertices[offset] =
                sprite.gameObject.transform.position.x + xAdd * sprite.gameObject.transform.scale.x
            vertices[offset + 1] =
                sprite.gameObject.transform.position.y + yAdd * sprite.gameObject.transform.scale.y

            // Load color
            vertices[offset + 2] = color.x
            vertices[offset + 3] = color.y
            vertices[offset + 4] = color.z
            vertices[offset + 5] = color.w

            // Load Texture Coords
            texCoords?.get(i)?.let {
                vertices[offset + 6] = it.x
                vertices[offset + 7] = it.y
            }
            // Load text id
            vertices[offset + 8] = texID.toFloat()
            offset += VERTEX_SIZE
        }
    }

    fun hasRoom(): Boolean {
        return hasRoom
    }

    fun hasTextureRoom(): Boolean {
        return textures.size < 8
    }

    fun hasTexture(tex: Texture?): Boolean {
        return textures.contains(tex)
    }

    fun getzIndex(): Int {
        return zIndex
    }

    override fun compareTo(o: RenderBatch): Int {
        return Integer.compare(zIndex, o.zIndex)
    }
}