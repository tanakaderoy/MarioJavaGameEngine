package components;

import jade.Component;

public class SpriteRenderer extends Component {


    private boolean ft = false;

    @Override
    public void start() {
        super.start();
        System.out.println("Starting");
    }

    @Override
    public void update(float dt) {
        if (!ft) {
            System.out.println("I am updating");
            ft = true;
        }
    }
}