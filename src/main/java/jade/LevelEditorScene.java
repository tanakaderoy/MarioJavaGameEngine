package jade;

import util.Color;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene {
    private boolean changingScene = false;
    private float timeToChangeScene = 2f;

    public LevelEditorScene() {
        System.out.println("Level Editor Scene");
    }

    @Override
    public void update(float dt) {
//        System.out.println((1.0f/dt)+" fps");
        if (!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            changingScene = true;
        }
        if (changingScene && timeToChangeScene > 0) {
            timeToChangeScene -= dt;
            Window.get().backgroundColor.r -= dt * 5f;
            Window.get().backgroundColor.g -= dt * 5f;
            Window.get().backgroundColor.b -= dt * 5f;
        } else if (changingScene) {
            Window.changeScene(SceneType.LEVELSCENE);
        }

    }


}
