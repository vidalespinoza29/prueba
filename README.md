import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Registro extends JFrame {
    private JTextField usernameField;
    private JTextField passwordField;
    private JTextField telefonoField;

    public Registro(){
        setTitle("Registro");
        setSize(300,200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4,2));

        add(new JLabel("Usuario"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Contraseña"));
        passwordField = new JTextField();
        add(passwordField);

        add(new JLabel("Telefono"));
        telefonoField = new JTextField();
        add(telefonoField);


        JButton registerButton = new JButton("Registrar");
        registerButton.addActionListener(new RegisterAccion());
        add(registerButton);

        JButton loginButton = new JButton("Iniciar Secion ");
        loginButton.addActionListener( e -> {
            dispose();
            new Login().setVisible(true);

        });
        add(loginButton);

    }

    private class RegisterAccion implements ActionListener{
        public void actionPerformed(ActionEvent e){
            String username = usernameField.getText();
            String password = new String(passwordField.getPassw());
            String telefono = telefonoField.getText();
            String role = "usuario";

            if (username.isEmpty() || password.isEmpty() || telefono.isEmpty()){
                JOptionPane.showMessageDialog(:null, "todos los campos son ");
                return;

            }

            try (Connection conn = Coneccion.getConeccion()) {
                if (conn != null) {
                    String query = "INSERT INTO usuarios(nombre,contraseña,telefono,role) VALUES (?,?,?,?)";
                    try (PreparedStatement stmt = conn.prepareStatement(query)){
                        stmt.setString(1,username);
                        stmt.setString(2,password);
                        stmt.setString(3,telefono);
                        stmt.setString(4,role);

                        int rowInserted = stmt.executeUpdate();
                        if(rowInserted>0){
                            JOptionPane.showMessageDialog(null,"Registro Exitoso");
                            dispose();
                            new Login().setVisible(true);
                        }else{
                            JOptionPane.showMessageDialog(null,"error al registrar el usuario");

                        }
                    }
                }else {
                    JOptionPane.showMessageDialog(null, "No se pudo conectar a la base de datos");

                }
            }catch(SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null,"error al registrarse"+ex.getMessage());
            }

        }

    }
    
}
