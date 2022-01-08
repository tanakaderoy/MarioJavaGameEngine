package util;

import renderer.Shader;
import renderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class AssetPool {
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();

    /**
     * Get the shader from the given shader resourceName
     *
     * @param resourceName `String` path to the location
     * @return a Shader
     */
    public static Shader getShader(String resourceName) {
        File file = new File(resourceName);
//        if (shaders.containsKey(file.getAbsolutePath())) {
//            return shaders.get(file.getAbsolutePath());
//        } else {
//            Shader shader = new Shader(resourceName);
//            shader.compile();
//            shaders.put(file.getAbsolutePath(), shader);
//            return shader;
//        }
        return getObjectFromMap(shaders, resourceName, () -> {
            Shader shader = new Shader(resourceName);
            shader.compile();
            shaders.put(file.getAbsolutePath(), shader);
            return shader;
        });

    }

    /**
     * Get the texture in the asset pool
     *
     * @param resourceName a String resourceName
     * @return a texture
     */
    public static Texture getTexture(String resourceName) {
        File file = new File(resourceName);
//        if (textures.containsKey(file.getAbsolutePath())) {
//            return textures.get(file.getAbsolutePath());
//        } else {
//            Texture texture = new Texture(resourceName);
//            textures.put(file.getAbsolutePath(), texture);
//            return texture;
//        }
        return getObjectFromMap(textures, resourceName, () -> {
            Texture texture = new Texture(resourceName);
            textures.put(file.getAbsolutePath(), texture);
            return texture;
        });
    }

    private static <T> T getObjectFromMap(Map<String, T> map, String key, Supplier<T> notExistAction) {
        if (map.containsKey(key)) {
            return map.get(key);
        } else {
            return notExistAction.get();
        }
    }

}
