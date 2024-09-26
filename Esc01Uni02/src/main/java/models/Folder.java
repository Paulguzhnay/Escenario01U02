package models;

import java.util.ArrayList;
import java.util.List;

public class Folder implements FileSystemComponent {
    private String name;
    private List<FileSystemComponent> components = new ArrayList<>();

    public Folder(String name) {
        this.name = name;
    }

    public void add(FileSystemComponent component) {
        components.add(component);
    }

    public void remove(FileSystemComponent component) {
        components.remove(component);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getSize() {
        return components.stream().mapToInt(FileSystemComponent::getSize).sum();
    }

    @Override
    public String toString() {
        return name; // Muestra solo el nombre de la carpeta
    }
}
