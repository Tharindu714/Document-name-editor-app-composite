package com.tharindu.composite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

// Component interface with hit-test
interface DocumentElement {
    void move(int dx, int dy);

    void resize(double factor);

    void draw(Graphics2D g);

    boolean contains(Point p);
}

// Leaf: EditableTextBox
class EditableTextBox implements DocumentElement {
    private int x, y;
    private String text;
    private double size;

    public EditableTextBox(int x, int y, String text, double size) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.size = size;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void move(int dx, int dy) {
        x += dx;
        y += dy;
        System.out.println("Moved TextBox to (" + x + ", " + y + ") with the following text ='" + text + "'");
    }

    @Override
    public void resize(double factor) {
        size *= factor;
        System.out.println("Resized TextBox to size= (" + size + " KB) by the factor " + factor + " with the following text ='" + text + "'");
    }

    @Override
    public void draw(Graphics2D g) {
        Font font = g.getFont().deriveFont((float) size);
        g.setFont(font);
        g.drawString(text, x, y);
        System.out.println("Drawing TextBox at (" + x + ", " + y + ") with text='" + text + "' and size= (" + size + " KB)");
    }

    @Override
    public boolean contains(Point p) {
        Font font = new JLabel().getFont().deriveFont((float) size);
        System.out.println("Checking if point " + p + " is within TextBox bounds at (" + x + ", " + y + ") with text='" + text + "' and size= (" + size + " KB)");
        FontMetrics fm = new JLabel().getFontMetrics(font);
        int width = fm.stringWidth(text);
        int height = fm.getHeight();
        Rectangle bounds = new Rectangle(x, y - fm.getAscent(), width, height);
        System.out.println("Point " + p + " is " + (bounds.contains(p) ? "inside" : "outside") + " the TextBox bounds.");
        return bounds.contains(p);
    }
}

// Composite: EditableGroup
class EditableGroup implements DocumentElement {
    private final List<DocumentElement> children = new ArrayList<>();

    public void add(DocumentElement elem) {
        children.add(elem);
        System.out.println("Added element: " + elem.getClass().getSimpleName());
    }

    public void remove(DocumentElement elem) {
        children.remove(elem);
        System.out.println("Removed element: " + elem.getClass().getSimpleName());
    }

    @Override
    public void move(int dx, int dy) {
        for (DocumentElement e : children) e.move(dx, dy);
        System.out.println("Moved group by (" + dx + ", " + dy + ")");
    }

    @Override
    public void resize(double f) {
        for (DocumentElement e : children) e.resize(f);
        System.out.println("Resized group by factor " + f);
    }

    @Override
    public void draw(Graphics2D g) {
        for (DocumentElement e : children) e.draw(g);
        System.out.println("Drawing group with " + children.size() + " elements.");
    }

    @Override
    public boolean contains(Point p) {
        for (DocumentElement e : children) {
            if (e.contains(p)) return true;
        }
        System.out.println("Point " + p + " is outside the group bounds.");
        return false;
    }

    public DocumentElement findElementAt(Point p) {
        for (DocumentElement e : children) {
            if (e.contains(p)) return e;
        }
        System.out.println("No element found at point " + p);
        return null;
    }
}

// Main editor with UI controls
public class ActualDocEditor {
    private final EditableGroup page = new EditableGroup();
    private DocumentElement selected;
    private Point lastClickPoint;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ActualDocEditor().createAndShowGUI());
    }

    private void createAndShowGUI() {
        // Initial elements
        page.add(new EditableTextBox(50, 50, "Click to edit", 24));
        page.add(new EditableTextBox(50, 100, "Composite Pattern Demo", 18));

        JFrame frame = new JFrame("Editable Document Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000);

        // Toolbar setup
        JPanel toolbar = new JPanel();
        JTextField editField = new JTextField(15);
        JTextField newTextField = new JTextField(15);
        JButton applyBtn = new JButton("Apply Text");
        JButton removeBtn = new JButton("Remove");
        JButton addTextBtn = new JButton("Add Text");

        toolbar.add(new JLabel("New Text:"));
        toolbar.add(newTextField);
        toolbar.add(addTextBtn);
        toolbar.add(new JLabel("Selected Text:"));
        toolbar.add(editField);
        toolbar.add(applyBtn);
        toolbar.add(removeBtn);

        // Canvas
        DrawPanel canvas = new DrawPanel();
        canvas.setBackground(Color.WHITE);
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                lastClickPoint = e.getPoint();
                selected = page.findElementAt(e.getPoint());
                System.out.println("Clicked at " + e.getPoint() + ", selected=" +
                        (selected == null ? "none" : selected.getClass().getSimpleName()));
                if (selected instanceof EditableTextBox) {
                    editField.setText(((EditableTextBox) selected).getText());
                } else {
                    editField.setText("");
                }
                canvas.repaint();
            }
        });

        // Apply text to selected
        applyBtn.addActionListener(e -> {
            if (selected instanceof EditableTextBox) {
                ((EditableTextBox) selected).setText(editField.getText());
                canvas.repaint();
            }
        });

        // Remove selected
        removeBtn.addActionListener(e -> {
            if (selected != null) {
                page.remove(selected);
                selected = null;
                editField.setText("");
                canvas.repaint();
            }
        });

        // Add new text box at last click location
        addTextBtn.addActionListener(e -> {
            String txt = newTextField.getText().trim();
            if (txt.isEmpty()) return;
            Point p = lastClickPoint != null ? lastClickPoint : new Point(50, 150);
            EditableTextBox newBox = new EditableTextBox(p.x, p.y, txt, 18);
            page.add(newBox);
            selected = newBox;
            editField.setText(txt);
            newTextField.setText("");
            System.out.println("[Client] Added new TextBox at " + p + " with text='" + txt + "'");
            canvas.repaint();
        });

        frame.setLayout(new BorderLayout());
        frame.add(toolbar, BorderLayout.NORTH);
        frame.add(canvas, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // Custom drawing panel
    private class DrawPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            page.draw(g2);
            // optional highlight selected
            if (selected != null) {
                System.out.println("Highlighting " + selected.getClass().getSimpleName());
            }
        }
    }
}