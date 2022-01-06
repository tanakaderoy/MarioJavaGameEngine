package renderer;

import components.SpriteRenderer;

public interface RenderBatchI {
    void start();

    void addSprite(SpriteRenderer spr);

    int[] generateIndices();

    void render();

    void loadVertexProperties(int index);
}
