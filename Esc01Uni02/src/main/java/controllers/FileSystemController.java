/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import javax.swing.tree.DefaultMutableTreeNode;
import models.*;

import view.*;
/**
 *
 * @author paul-
 */
public class FileSystemController {
    private FileSystemView view;

    public FileSystemController(FileSystemView view) {
        this.view = view;
    }

    public void addComponent(String parentName, String nodeName, boolean isFile, int size) {
        DefaultMutableTreeNode parentNode = findNode(view.getRoot(), parentName);
        if (parentNode != null) {
            DefaultMutableTreeNode newNode;
            if (isFile) {
                newNode = new DefaultMutableTreeNode(new Files(nodeName, size));
            } else {
                newNode = new DefaultMutableTreeNode(new Folder(nodeName));
            }
            parentNode.add(newNode);
            view.getTreeModel().reload(); // Refrescar el árbol
        } else {
            view.showError("El nodo padre '" + parentName + "' no fue encontrado.");
        }
    }

    public void removeComponent(String parentName, String nodeName) {
        DefaultMutableTreeNode parentNode = findNode(view.getRoot(), parentName);
        if (parentNode != null) {
            DefaultMutableTreeNode nodeToRemove = findNode(parentNode, nodeName);
            if (nodeToRemove != null) {
                parentNode.remove(nodeToRemove);
                view.getTreeModel().reload(); // Refrescar el árbol
            } else {
                view.showError("El nodo '" + nodeName + "' no fue encontrado.");
            }
        } else {
            view.showError("El nodo padre '" + parentName + "' no fue encontrado.");
        }
    }

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
}