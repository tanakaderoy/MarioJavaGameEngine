package jade;

import org.joml.Vector2f;

public class Transform {
    public Vector2f position, scale;

    public Transform() {
        init(new Vector2f(), new Vector2f());
    }

    public void init(Vector2f position, Vector2f scale) {
        this.position = position;
        this.scale = scale;
    }

    public Transform copy() {
        return new Transform(new Vector2f(this.position), new Vector2f(this.scale));
    }

    public void copy(Transform to) {
        to.position.set(this.position);
        to.scale.set(this.scale);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transform transform = (Transform) o;

        if (!position.equals(transform.position)) return false;
        return scale.equals(transform.scale);
    }

    @Override
    public int hashCode() {
        int result = position.hashCode();
        result = 31 * result + scale.hashCode();
        return result;
    }

    public Transform(Vector2f position) {
        init(position, new Vector2f());
    }

    public Transform(Vector2f position, Vector2f scale) {
        init(position, scale);
    }
}
