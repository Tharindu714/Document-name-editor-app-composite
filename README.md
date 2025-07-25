# Editable Document Editor

A Java Swing application demonstrating the **Composite Design Pattern** in a simple interactive document editor.

---

## Table of Contents

1. [Overview](#overview)
2. [Features](#features)
3. [Design Pattern: Composite](#design-pattern-composite)
4. [Getting Started](#getting-started)

   * [Prerequisites](#prerequisites)
   * [Project Structure](#project-structure)
   * [Running the Application](#running-the-application)
5. [How It Works](#how-it-works)
6. [Extending the App](#extending-the-app)
7. [License](#license)

---

## Overview

`EditableDocEditor` is a desktop Java application built with **Swing** that lets users:

* Click to select text boxes.
* Edit existing text in those boxes.
* Remove text boxes.
* Add new text boxes dynamically at clicked locations.

All document elements (text boxes) are managed through the **Composite Pattern**, enabling uniform treatment of both individual elements and groups of elements.

---

## Features

* **Interactive Editing**: Click any text box to select it, change its content via the toolbar.
* **Dynamic Addition**: Type new text, click “Add Text,” and place new boxes at the last-clicked canvas location.
* **Removal**: Remove the currently selected box with a single button click.
* **Visual Feedback**: Console logs illustrate leaf vs. composite operations.

---

## Design Pattern: Composite

The **Composite Design Pattern** lets you treat both single objects (leaves) and compositions of objects (composites) uniformly.

### Key Components

1. **Component Interface (`DocumentElement`)**

   * Declares common operations: `move()`, `resize()`, `draw()`, `contains()`.

2. **Leaf (`EditableTextBox`)**

   * Implements `DocumentElement` for individual text boxes.
   * Handles its own drawing, hit-testing, and state (position, text, font size).

3. **Composite (`EditableGroup`)**

   * Implements `DocumentElement`, stores child `DocumentElement`s in a list.
   * Forwards `move()`, `resize()`, and `draw()` calls to all children.

4. **Client (`ActualDocEditor`)**

   * Builds and manages the UI.
   * Operates on `DocumentElement` references without knowing whether they are leaves or composites.

### Why Use Composite?

* **Scalability**: New element types (e.g., images, shapes) can be added easily.
* **Uniformity**: Client code treats everything as `DocumentElement`, simplifying operations like rendering and hit-testing.
* **Maintainability**: Clear separation between object behaviors (leaf) and object aggregates (composite).

---

## Getting Started

### Prerequisites

* Java JDK 11 or higher
* IntelliJ IDEA (or any Java IDE)
* Maven or Gradle (if using a build tool)

### Project Structure

```
project-root/
├─ src/
│  ├─ main/
│  │  ├─ java/
│  │  │   └─ com/tharindu/composite/
│  │  │       ├─ ActualDocEditor.java
│  │  │       └─ DocEditor.java
│  │  └─ resources/
│  │       └─ logo.png
├─ README.md
└─ pom.xml (or build.gradle)
```

### Running the Application

1. **Import** the project into IntelliJ IDEA.
2. Right‑click on `ActualDocEditor.java` → **Run**.
3. Use the toolbar to add, select, edit, and remove text boxes.

---

## How It Works
<img width="983" height="726" alt="image" src="https://github.com/user-attachments/assets/b6cf256c-fdfe-4762-beb7-f63a20c3610e" />

1. **Initialization**
   The client (`ActualDocEditor`) creates an `EditableGroup` and adds a couple of `EditableTextBox` leaves.

2. **UI Setup**
   A `JToolBar` hosts text fields and buttons; a custom `JPanel` (`DrawPanel`) overrides `paintComponent()` to draw all `DocumentElement`s.

3. **User Interaction**

   * **Click** on a text box → `page.findElementAt()` checks each leaf’s `contains()`.
   * **Apply Text** → `setText()` on the selected leaf, followed by `repaint()`.
   * **Add Text** → Reads input, creates a new leaf at the last click point, and adds it to the composite.
   * **Remove** → Calls `remove()` on the composite to drop the selected leaf.

4. **Console Logging**
   Print statements tagged `[Leaf]`, `[Composite]`, and `[Client]` illustrate pattern operations in real time.

---

## Extending the App

* **Image Support**: Implement an `ImageElement` leaf that loads and draws an image.
* **Drag & Drop**: Allow users to drag text boxes around the canvas.
* **Grouping**: Let users group multiple elements and treat them as a single composite.
* **Export**: Add functionality to export the canvas to PDF or PNG.

---

## License

Licensed under the MIT License. Feel free to reuse and adapt for your learning projects.
