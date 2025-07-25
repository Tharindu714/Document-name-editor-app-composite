package com.tharindu.composite;

import java.util.ArrayList;
import java.util.List;

// Component interface
interface DocElement {
    void render();

    void move(int x, int y);

    void resize(double factor);
}

// Leaf: TextBox
class TextBox implements DocElement {
    private int x, y;
    private final String text;
    private double size;

    public TextBox(int x, int y, String text, double size) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.size = size;
    }

    public String getText() {
        return text;
    }

    @Override
    public void render() {
        System.out.println("Rendering TextBox at (" + x + ", " + y + ") with text='" + getText() + "' size= (" + size + " KB)");
    }

    @Override
    public void move(int x, int y) {
        this.x = x;
        this.y = y;
        System.out.println("Moved TextBox '" + text + "' to (" + x + ", " + y + ")");
    }

    @Override
    public void resize(double factor) {
        this.size *= factor;
        System.out.println("Resized TextBox '" + text + "' by factor " + factor + " to size= (" + size + " KB)");
    }
}

// Leaf: ImageElement
class ImageElement implements DocElement {
    private int x, y, width, height;
    private final String url;

    public ImageElement(int x, int y, int width, int height, String url) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.url = url;
    }

    @Override
    public void render() {
        System.out.println("Rendering Image at (" + x + ", " + y + ") size=" + width + "x" + height + " from " + url);
    }

    @Override
    public void move(int x, int y) {
        this.x = x;
        this.y = y;
        System.out.println("Moved Image to (" + x + ", " + y + ")");
    }

    @Override
    public void resize(double factor) {
        width = (int) (width * factor);
        height = (int) (height * factor);
        System.out.println("Resized Image by factor " + factor + " to " + width + "x" + height);
    }
}

// Composite: Group of elements
class Group implements DocElement {
    private final List<DocElement> children = new ArrayList<>();

    public void add(DocElement elem) {
        children.add(elem);
    }

    @Override
    public void render() {
        System.out.println("Rendering Group with " + children.size() + " elements");
        for (DocElement elem : children) {
            elem.render();
        }
    }

    @Override
    public void move(int x, int y) {
        System.out.println("Moving Group by (" + x + ", " + y + ")");
        for (DocElement elem : children) {
            elem.move(x, y);
        }
    }

    @Override
    public void resize(double factor) {
        System.out.println("Resizing Group by factor " + factor);
        for (DocElement elem : children) {
            elem.resize(factor);
        }
    }
}

class Chart implements DocElement {
    private int x, y, width, height;
    private final String title;

    public Chart(int x, int y, int width, int height, String title) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.title = title;
    }

    @Override
    public void render() {
        System.out.println("Rendering Chart at (" + x + ", " + y + ") size=" + width + "x" + height + " with title: " + title);
    }

    @Override
    public void move(int x, int y) {
        this.x = x;
        this.y = y;
        System.out.println("Moved Chart to (" + x + ", " + y + ")");
    }

    @Override
    public void resize(double factor) {
        width = (int) (width * factor);
        height = (int) (height * factor);
        System.out.println("Resized Chart by factor " + factor + " to " + width + "x" + height);
    }
}

// Client: Document Editor
public class DocEditor {
    public static void main(String[] args) {
        // Create individual elements
        TextBox title = new TextBox(0, 0, "Composite Pattern", 24);
        TextBox body = new TextBox(10, 50, "This is an example of the composite design pattern.", 12);
        ImageElement logo = new ImageElement(300, 20, 100, 50, "logo.png");
        Chart chart = new Chart(50, 150, 400, 300, "Sales Data");
        // Create a group
        Group page = new Group();
        page.add(title);
        page.add(body);
        page.add(logo);
        page.add(chart);

        // Render initial state
        page.render();
        System.out.println();

        // Move the entire page
        page.move(5, 10);
        System.out.println();

        // Resize the entire page
        page.resize(1.5);
        System.out.println();

        // Render final state
        page.render();
    }
}