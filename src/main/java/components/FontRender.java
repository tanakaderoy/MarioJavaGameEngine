package components;

import jade.Component;

public class FontRender extends Component {

    @Override
    public void start() {
        super.start();
        if (gameObject.getComponent(SpriteRenderer.class) != null) {
            System.out.println("Found Font Renderer!");
        }
    }

    @Override
    public void update(float dt) {

    }
}
