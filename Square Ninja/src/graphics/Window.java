package graphics;

import gameplay.Click;
import gameplay.Input;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;

public class Window {
    private long window ; // Window handle

    private int width, height; // Height and Width of Window
    private boolean fullscreen;

    private Input input;

    public Window() {
        setSize(640, 480);
        setFullscreen(false);
    }

    public void closeWindow() {
        glfwSetWindowShouldClose(window, true);
    }

    public void createWindow(String title) {
        window = glfwCreateWindow(width, height, title, fullscreen ? glfwGetPrimaryMonitor() : 0, 0); // If fullscreen is true then pass Primary Monitor, if false then pass 0

        if (window == 0) {
            throw new IllegalStateException("Failed to create window");
        }


        if (!fullscreen) {
            GLFWVidMode vid = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(window, (vid.width() - width) / 2, (vid.height() - height) / 2);
            glfwShowWindow(window);
        }

        glfwMakeContextCurrent(window);

        input = new Input(window);

    }

    public static void setCallbacks() {
        glfwSetErrorCallback(new GLFWErrorCallback() {
            @Override
            public void invoke(int error_code, long description) {
                throw new IllegalStateException(GLFWErrorCallback.getDescription(description));
            }
        });
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window) != false;
    }

    public void swapBuffers() {
        glfwSwapBuffers(window);
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setSizeWhileRunning(int width, int height) {
        glfwSetWindowSize(window, width, height);
    }

    public void setSizeOnCMDF() { // Goes in update method
        if (this.getInput().isKeyDown(GLFW_KEY_LEFT_SUPER) && this.getInput().isKeyPressed(GLFW_KEY_F)) {
            GLFWVidMode vid = glfwGetVideoMode(glfwGetPrimaryMonitor());
            System.out.println("THIS");
            if (width == vid.width()) {
                setSizeWhileRunning(640, 480);
                setSize(640, 480);
            } else {
                setSizeWhileRunning(vid.width(), vid.height() - 48);
                setSize(vid.width(), vid.height() - 48);
            }

            glfwSetWindowPos(window, (vid.width() - width) / 2, (vid.height() - height) / 2);
        }
    }

    public void update() { // Updates keys pressed and checks if an event occurs
        setSizeOnCMDF();
        input.update();
        Click.update(window);
        glfwPollEvents();
    }

    public int getWidth() { return width; } // Returns window width
    public int getHeight() { return height; } // Returns window height
    public boolean isFullscreen() { return fullscreen; } // returns if fullscreen is set to true or false
    public long getWindow() { return window; } // returns window handle
    public Input getInput() { return input; } // returns the current input (Mouse and Keyboard)
}
