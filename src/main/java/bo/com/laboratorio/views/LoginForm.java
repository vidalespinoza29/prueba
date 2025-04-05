/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bo.com.laboratorio.views;

/**
 *
 * @author Windows
 */

import bo.com.laboratorio.controller.EquipoController;
import bo.com.laboratorio.exceptions.CredencialesInvalidasException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginForm extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private EquipoController controller;
    
    public LoginForm() {
        controller = new EquipoController();
        
        // Configuración de la ventana
        setTitle("Login - CRUD Equipos");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Panel principal
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Componentes
        panel.add(new JLabel("Usuario:"));
        txtUsuario = new JTextField();
        panel.add(txtUsuario);
        
        panel.add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);
        
        panel.add(new JLabel(""));  // Espacio en blanco
        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.addActionListener(this::iniciarSesion);
        panel.add(btnLogin);
        
        add(panel);
        setVisible(true);
    }
    
    private void iniciarSesion(ActionEvent e) {
        String usuario = txtUsuario.getText();
        String password = new String(txtPassword.getPassword());
        
        if (usuario.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, complete todos los campos", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            controller.validarCredenciales(usuario, password);
            JOptionPane.showMessageDialog(this, 
                "Inicio de sesión exitoso", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Cerrar el formulario de login y abrir el CRUD
            dispose();
            new EquipoForm().setVisible(true);
            
        } catch (CredencialesInvalidasException ex) {
            JOptionPane.showMessageDialog(this, 
                ex.getMessage(), 
                "Error de autenticación", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
