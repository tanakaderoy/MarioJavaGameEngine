package jade;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import renderer.Shader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {

    private int vertexID, fragmentID, shaderProgram;
    private int vaoID, vboID, eboID;
    private Shader defaultShader;

    private final float[] vertexArray = {
            // position         // color
            100f, 0f, 0f, 1f, 0f, 0f, 1f, // Bottom right 0
            0f, 100f, 0f, 0f, 1f, 0f, 1f, // Top Left 1
            100f, 100f, 0f, 0f, 0f, 1f, 1f, // Top Right 2
            0f, 0f, 0f, 1f, 1f, 0f, 1f, // Bottom Left 3

    };
    // Must be in counter clockwise order
    private final int[] elementArray = {
            /*
                    x         x


                    x         x

             */
            2, 1, 0, //Top right
            0, 1, 3, // Bottom left
    };

    public LevelEditorScene() {
    }


    @Override
    public void init() {
        super.init();
        this.camera = new Camera(new Vector2f());
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();


        // ===================================================================
        // Generate VAO, VBO, and EBO buffer objects and send to the GPU
        // ===================================================================
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
        // create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO and upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create the indeces and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int floatByteSize = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * floatByteSize;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * floatByteSize);
        glEnableVertexAttribArray(1);


    }

    @Override
    public void update(float dt) {
        camera.position.x -= dt * 50;
        // Bind the VAO we're using
        glBindVertexArray(vaoID);
        defaultShader.use();
        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        // Enable Vertex Attrib pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
        defaultShader.detatch();

    }


}
