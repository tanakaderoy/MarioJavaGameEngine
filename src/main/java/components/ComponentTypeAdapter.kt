package components

import com.google.gson.*
import java.lang.reflect.Type

class ComponentTypeAdapter : JsonDeserializer<Component>, JsonSerializer<Component> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Component {
        val jsonObject = json.asJsonObject
        val type = jsonObject.get("type").asString
        val element = jsonObject.get("properties")
        try {
            return context.deserialize(element, Class.forName(type))
        } catch (e: ClassNotFoundException) {
            throw JsonParseException("Unkown elemnt type: $type", e)
        }
    }

    override fun serialize(src: Component, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val result = JsonObject()
        result.add("type", JsonPrimitive(src::class.java.canonicalName))
        result.add("properties", context.serialize(src, src::class.java))
        return result
    }
}