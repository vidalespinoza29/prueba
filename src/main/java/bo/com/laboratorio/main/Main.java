/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package bo.com.laboratorio.main;

/**
 *
 * @author Windows
 */
import bo.com.laboratorio.views.LoginForm;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Establecer look and feel nativo del sistema
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Iniciar la aplicación con el formulario de login
            new LoginForm();
        });
    }
}
