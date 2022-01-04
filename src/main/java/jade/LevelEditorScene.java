package jade;

import org.lwjgl.BufferUtils;
import util.Color;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {
    private String vertexShaderSrc = "    #version 330 core\n" +
            "    layout (location=0) in vec3 aPos;\n" +
            "    layout (location=1) in vec4 aColor;\n" +
            "    out vec4 fColor;\n" +
            "\n" +
            "    void main() {\n" +
            "        fColor = aColor;\n" +
            "        gl_Position = vec4(aPos,1.0);\n" +
            "    }";
    private String fragmentShaderSrc = " #version 330 core\n" +
            "\n" +
            "    in vec4 fColor;\n" +
            "\n" +
            "    out vec4 color;\n" +
            "\n" +
            "    void main() {\n" +
            "        color = fColor;\n" +
            "    }";

    private int vertexID, fragmentID, shaderProgram;
    private int vaoID, vboID, eboID;

    private float[] vertexArray = {
            // position         // color
            0.5f, -0.5f, 0f,    1f, 0f, 0f, 1f, // Bottom right 0
            -0.5f, 0.5f, 0f,    0f, 1f, 0f, 1f, // Top Left 1
            0.5f, 0.5f, 0f,     0f, 0f, 1f, 1f, // Top Right 2
            -0.5f, -0.5f, 0f,   1f, 1f, 0f, 1f, // Bottom Left 3

    };
    // Must be in counter clockwise order
    private int[] elementArray = {
            /*
                    x         x


                    x         x

             */
            2,1,0, //Top right
            0,1,3, // Bottom left
    };

    public LevelEditorScene() {

    }


    @Override
    public void init() {
        super.init();
        // ============================================
        // Compile and link shaders
        // ============================================

        //First load and compile vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // Pass shader source to GPU
        glShaderSource(vertexID, vertexShaderSrc);
        glCompileShader(vertexID);

        // Check for errors in compilation
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tVertex shader compilation failed");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }


        //First load and compile fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass shader source to GPU
        glShaderSource(fragmentID, fragmentShaderSrc);
        glCompileShader(fragmentID);

        // Check for errors in compilation
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tFragment shader compilation failed");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        // Link shader and check errors
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);

        // Check for errors
        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tLinking of shaders failed");
            System.out.println(glGetProgramInfoLog(shaderProgram, len));
            assert false : "";
        }
        // ================================================================
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
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,elementBuffer,GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int floatByteSize = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * floatByteSize;
        glVertexAttribPointer(0,positionsSize, GL_FLOAT,false,vertexSizeBytes,0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1,colorSize,GL_FLOAT,false, vertexSizeBytes, positionsSize * floatByteSize);
        glEnableVertexAttribArray(1);




    }

    @Override
    public void update(float dt) {
        // Bind the Shader Program
        glUseProgram(shaderProgram);
        // Bind the VAO we're using
        glBindVertexArray(vaoID);

        // Enable Vertex Attrib pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT,0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
        glUseProgram(0);

    }


}
