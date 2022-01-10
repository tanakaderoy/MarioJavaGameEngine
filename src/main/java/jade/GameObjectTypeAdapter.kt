package jade

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import components.Component
import java.lang.reflect.Type

class GameObjectTypeAdapter : JsonDeserializer<GameObject> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): GameObject {
        val jsonObject = json.asJsonObject
        val name = jsonObject.get("name").asString
        val components = jsonObject.get("components").asJsonArray
        val transform = context.deserialize<Transform>(jsonObject.get("transform"), Transform::class.java)
        val zIndex = context.deserialize<Int>(jsonObject.get("zIndex"), Int::class.java)
        return GameObject(name, transform, zIndex).apply {
            components.forEach {
                val c = context.deserialize<Component>(it, Component::class.java)
                this.addComponent(c)
            }
        }

    }

}