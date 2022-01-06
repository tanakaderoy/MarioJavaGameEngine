package renderer;

import components.SpriteRenderer;
import jade.GameObject;

public interface RendererI {
    void add(GameObject obj);

    void add(SpriteRenderer sprite);

    void render();
}
