/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package grupo05.esc01uni02;

import controllers.FileSystemController;
import models.Files;
import models.Folder;
import view.FileSystemView;

/**
 *
 * @author paul-
 */
public class Esc01Uni02 {

    public static void main(String[] args) {
        // Crear instancia de la vista
        FileSystemView view = new FileSystemView(null); // Inicialmente, el controlador es null
        
        // Crear instancia del controlador y asociarlo con la vista
        FileSystemController controller = new FileSystemController(view);
        
        // Actualizar la vista con el controlador
        view.setController(controller);
        
        // Mostrar la vista
        view.setVisible(true);
    }
}