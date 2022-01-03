package jade;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double xPos, yPos, lastY, lastX;
    private boolean mouseButtonPressed[] = new boolean[3];
    private boolean isDragging;

    private MouseListener() {
        this.scrollX = 0.0;
        this.scrollY = 0;
        this.xPos = 0;
        this.yPos = 0;
        this.lastX = 0;
        this.lastY = 0;
    }


    public static MouseListener get() {
        if (instance == null)
            instance = new MouseListener();

        return instance;
    }

    public static void mousePosCallback(long window, double xPos, double yPos) {
        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().xPos = xPos;
        get().yPos = yPos;
        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];

    }

    public static void mouseButtonCallback(long window, int button, int action, int modifier) {
        MouseListener mouse = get();
        if (action == GLFW_PRESS) {
            if (button < mouse.mouseButtonPressed.length)
                mouse.mouseButtonPressed[button] = true;
        } else if (action == GLFW_RELEASE) {
            if (button < mouse.mouseButtonPressed.length) {
                mouse.mouseButtonPressed[button] = false;
                mouse.isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        MouseListener mouse = get();
        mouse.scrollY = yOffset;
        mouse.scrollX = xOffset;
    }

    public static void endFrame() {
        MouseListener mouse = get();
        mouse.scrollY = 0;
        mouse.scrollX = 0;
        mouse.lastY = mouse.yPos;
        mouse.lastX = mouse.xPos;
    }

    public static float getDx() {
        return (float) (get().lastX - get().xPos);
    }

    public static float getDy() {
        return (float) (get().lastY - get().yPos);
    }

    public static float getX() {
        return (float) get().xPos;
    }

    public static float getY() {
        return (float) get().yPos;
    }

    public static float getScrollX() {
        return (float) get().scrollX;
    }

    public static float getScrollY() {
        return (float) get().scrollY;
    }

    public static boolean isDragging() {
        return get().isDragging;
    }

    public static boolean mouseButtonDown(int button) {
        if (button < get().mouseButtonPressed.length) {
            return get().mouseButtonPressed[button];
        } else {
            return false;
        }
    }


}
