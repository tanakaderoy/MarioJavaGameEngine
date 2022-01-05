package jade;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GameObject {
    private String name;
    private List<Component> components;

    public GameObject(String name) {
        this.name = name;
        this.components = new ArrayList<>();
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components) {

            if (componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    assert false : "Error: Casting component. ";
                }
            }
        }
        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (int i = 0; i < components.size(); i++) {
            Component c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass())) {
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component component) {
        components.add(component);
        component.gameObject = this;
    }

    public void update(float dt) {
//        for (int i = 0; i < components.size(); i++) {
//            components.get(i).update(dt);
//        }
        runOnAllComponents(c -> c.update(dt));
    }

    public void start() {
        runOnAllComponents(Component::start);
//    components.forEach(Component::start);
    }

    private void runOnAllComponents(Consumer<Component> action) {
        for (int i = 0; i < components.size(); i++) {
            action.accept(components.get(i));
        }
    }

}
