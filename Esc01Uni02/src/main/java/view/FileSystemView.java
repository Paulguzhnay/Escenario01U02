package view;

import controllers.FileSystemController;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.tree.DefaultTreeModel;
import models.Files; // Cambio de importación de la clase File a Files
import models.Folder;
import java.io.File; // Importar la clase File de java.io

public class FileSystemView extends JFrame {
    private JTree tree;
    private DefaultMutableTreeNode root;
    private DefaultTreeModel treeModel;
    private JLabel folderSizeLabel;
    private JLabel totalSizeLabel; // Nuevo JLabel para mostrar el tamaño total
    private FileSystemController controller;

    // Constructor
    public FileSystemView(FileSystemController controller) {
        // Configuración de la ventana y del árbol
        root = new DefaultMutableTreeNode("Root");
        treeModel = new DefaultTreeModel(root);
        tree = new JTree(treeModel);
        JScrollPane scrollPane = new JScrollPane(tree);

        folderSizeLabel = new JLabel("Tamaño de carpeta: ");
        totalSizeLabel = new JLabel("Tamaño total de archivos: 0 KB"); // Inicialización del tamaño total

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton addButton = new JButton("Añadir");
        JButton removeButton = new JButton("Eliminar");

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(totalSizeLabel, BorderLayout.NORTH); // Añadir el nuevo JLabel al panel superior
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH); // Añadir los botones al panel inferior

        // Añadir componentes a la ventana
        add(scrollPane, BorderLayout.CENTER);
        add(folderSizeLabel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);

        // Configuración de la ventana
        setTitle("File System Viewer");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Inicializar el árbol con la estructura de archivos del escritorio
        initializeFileSystem();

        // Acción para añadir archivo o carpeta
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addComponentDialog();
            }
        });

        // Acción para eliminar archivo o carpeta
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeComponentDialog();
            }
        });

        // Establecer el controlador
        setController(controller);
    }

    // Método para establecer el controlador
    public void setController(FileSystemController controller) {
        this.controller = controller;
    }

    // Método para obtener el nodo raíz
    public DefaultMutableTreeNode getRoot() {
        return root;
    }

    // Método para obtener el modelo del árbol
    public DefaultTreeModel getTreeModel() {
        return treeModel;
    }

    // Método para inicializar el árbol con la estructura de archivos del escritorio
    private void initializeFileSystem() {
        File desktop = new File(System.getProperty("user.home") + "/Desktop");
        addFilesAndFoldersToNode(root, desktop);
        updateTotalSize(); // Actualizar el tamaño total al inicializar
    }

    // Añadir archivos y carpetas al nodo
    private void addFilesAndFoldersToNode(DefaultMutableTreeNode parentNode, File file) {
        if (file.isDirectory()) {
            DefaultMutableTreeNode directoryNode = new DefaultMutableTreeNode(new Folder(file.getName()));
            parentNode.add(directoryNode);

            for (File childFile : file.listFiles()) {
                addFilesAndFoldersToNode(directoryNode, childFile);
            }
        } else {
            DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(new Files(file.getName(), (int) file.length() / 1024)); // Tamaño en KB
            parentNode.add(fileNode);
        }
    }

    // Método para añadir un nodo (archivo o carpeta) al árbol
    public void addNode(String parentName, String nodeName, boolean isFile, int size) {
        DefaultMutableTreeNode parentNode = findNode(root, parentName); // Buscar nodo padre
        if (parentNode != null) {
            DefaultMutableTreeNode newNode;
            if (isFile) {
                newNode = new DefaultMutableTreeNode(new Files(nodeName, size)); // Crear nuevo archivo
            } else {
                newNode = new DefaultMutableTreeNode(new Folder(nodeName)); // Crear nueva carpeta
            }
            parentNode.add(newNode); // Añadir nodo hijo al padre
            treeModel.reload(); // Refrescar el árbol
            updateTotalSize(); // Actualizar el tamaño total
        } else {
            JOptionPane.showMessageDialog(this, "El nodo padre '" + parentName + "' no fue encontrado.");
        }
    }

    // Método para eliminar un nodo (archivo o carpeta) del árbol
    public void removeNode(String parentName, String nodeName) {
        DefaultMutableTreeNode parentNode = findNode(root, parentName); // Buscar nodo padre
        if (parentNode != null) {
            DefaultMutableTreeNode nodeToRemove = findNode(parentNode, nodeName); // Buscar nodo a eliminar
            if (nodeToRemove != null) {
                parentNode.remove(nodeToRemove); // Eliminar el nodo
                treeModel.reload(); // Refrescar el árbol
                updateTotalSize(); // Actualizar el tamaño total
            } else {
                JOptionPane.showMessageDialog(this, "El nodo '" + nodeName + "' no fue encontrado.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "El nodo padre '" + parentName + "' no fue encontrado.");
        }
    }

    // Método para buscar un nodo en el árbol por su nombre
    private DefaultMutableTreeNode findNode(DefaultMutableTreeNode root, String nodeName) {
        if (root.toString().equals(nodeName)) {
            return root;
        }
        for (int i = 0; i < root.getChildCount(); i++) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) root.getChildAt(i);
            DefaultMutableTreeNode result = findNode(childNode, nodeName);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    // Diálogo para añadir un archivo o carpeta
    private void addComponentDialog() {
        String parentName = JOptionPane.showInputDialog(this, "Nombre de la carpeta padre:");
        String nodeName = JOptionPane.showInputDialog(this, "Nombre del nuevo archivo o carpeta:");
        String isFileInput = JOptionPane.showInputDialog(this, "¿Es un archivo? (sí/no):");
        boolean isFile = "sí".equalsIgnoreCase(isFileInput);

        int size = 0;
        if (isFile) {
            String sizeInput = JOptionPane.showInputDialog(this, "Tamaño del archivo en KB:");
            try {
                size = Integer.parseInt(sizeInput);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El tamaño debe ser un número entero.");
                return;
            }
        }
        controller.addComponent(parentName, nodeName, isFile, size);
    }

    // Diálogo para eliminar un archivo o carpeta
    private void removeComponentDialog() {
        String parentName = JOptionPane.showInputDialog(this, "Nombre de la carpeta padre:");
        String nodeName = JOptionPane.showInputDialog(this, "Nombre del archivo o carpeta a eliminar:");
        controller.removeComponent(parentName, nodeName);
    }

    // Método para actualizar la etiqueta de tamaño de carpeta
    public void displayFolderSize(String folderName, int size) {
        folderSizeLabel.setText("Tamaño de la carpeta '" + folderName + "': " + size + " KB");
    }

    // Método para calcular y actualizar el tamaño total de archivos
    private void updateTotalSize() {
        int totalSize = calculateTotalSize(root);
        totalSizeLabel.setText("Tamaño total de archivos: " + totalSize + " KB");
    }

    // Método recursivo para calcular el tamaño total de los archivos
    private int calculateTotalSize(DefaultMutableTreeNode node) {
        int totalSize = 0;
        Object userObject = node.getUserObject();
        if (userObject instanceof Files) { // Cambio de File a Files
            totalSize += ((Files) userObject).getSize();
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(i);
            totalSize += calculateTotalSize(childNode);
        }
        return totalSize;
    }

    // Mostrar mensajes de error
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
