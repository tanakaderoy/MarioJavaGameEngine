package jade;

import util.Color;

public class LevelScene extends Scene {

    public LevelScene() {
        System.out.println("Level Scene");
        Window.get().backgroundColor.r = 1f;
        Window.get().backgroundColor.g = 1f;
        Window.get().backgroundColor.b = 1f;
        System.out.println(Window.get().backgroundColor);
    }

    @Override
    public void update(float dt) {

    }
}
