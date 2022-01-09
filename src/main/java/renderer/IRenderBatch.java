package renderer;

import components.SpriteRenderer;

public interface IRenderBatch {
    void start();

    void addSprite(SpriteRenderer spr);

    int[] generateIndices();

    void render();

    void loadVertexProperties(int index);
}
