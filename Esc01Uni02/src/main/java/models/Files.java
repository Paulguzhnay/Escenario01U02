package models;

public class Files implements FileSystemComponent {
    private String name;
    private int size; // Tamaño en KB

    public Files(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public Files(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return name + " (" + size + " KB)"; // Muestra el nombre y el tamaño del archivo
    }
}
