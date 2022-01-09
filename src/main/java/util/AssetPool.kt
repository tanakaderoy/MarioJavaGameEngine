package util

import components.SpriteSheet
import renderer.Shader
import renderer.Texture
import java.io.File
import java.util.function.Supplier

object AssetPool {
    private val shaders: MutableMap<String, Shader> = HashMap()
    private val textures: MutableMap<String, Texture> = HashMap()
    private val spriteSheets: MutableMap<String, SpriteSheet> = HashMap()

    /**
     * Get the shader from the given shader resourceName
     *
     * @param resourceName `String` path to the location
     * @return a Shader
     */
    fun getShader(resourceName: String): Shader {
        val file = File(resourceName)
        //        if (shaders.containsKey(file.getAbsolutePath())) {
//            return shaders.get(file.getAbsolutePath());
//        } else {
//            Shader shader = new Shader(resourceName);
//            shader.compile();
//            shaders.put(file.getAbsolutePath(), shader);
//            return shader;
//        }
        return getObjectFromMap(shaders, resourceName, Supplier {
            val shader = Shader(resourceName)
            shader.compile()
            shaders[file.absolutePath] = shader
            shader
        })!!
    }

    /**
     * Get the texture in the asset pool
     *
     * @param resourceName a String resourceName
     * @return a texture
     */
    fun getTexture(resourceName: String): Texture {
        val file = File(resourceName)
        //        if (textures.containsKey(file.getAbsolutePath())) {
//            return textures.get(file.getAbsolutePath());
//        } else {
//            Texture texture = new Texture(resourceName);
//            textures.put(file.getAbsolutePath(), texture);
//            return texture;
//        }
        return getObjectFromMap(textures, resourceName, Supplier {
            val texture = Texture()
            texture.initialize(resourceName)
            textures[file.absolutePath] = texture
            texture
        })!!
    }

    fun addSpriteSheet(resourceName: String, spriteSheet: SpriteSheet) {
        val file = File(resourceName)
        if (!spriteSheets.containsKey(file.absolutePath)) {
            spriteSheets[file.absolutePath] = spriteSheet
        }
        //
    }

    fun getSpriteSheet(resourceName: String): SpriteSheet? {
        val file = File(resourceName)
        return getObjectFromMap(spriteSheets, file.absolutePath) {
            assert(false) {
                "Error: Tried to access spritesheet '%s' and it has not been added to the asset pool".formatted(
                    resourceName
                )
            }
            null
        }
    }

    private fun <T> getObjectFromMap(map: Map<String, T>, key: String, notExistAction: Supplier<T>): T? {
        return if (map.containsKey(key)) {
            map[key]
        } else {
            notExistAction.get()
        }
    }
}