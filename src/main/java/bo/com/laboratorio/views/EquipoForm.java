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
import bo.com.laboratorio.model.Equipo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;

public class EquipoForm extends JFrame {
    private JTable tablaEquipos;
    private DefaultTableModel modelo;
    private EquipoController controller;
    private JTextField txtNombre, txtMarca, txtModelo, txtPrecio;
    private JButton btnAgregar, btnActualizar, btnEliminar, btnLimpiar;
    private int idSeleccionado = -1;
    
    public EquipoForm() {
        controller = new EquipoController();
        setTitle("CRUD de Equipos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Panel de formulario
        JPanel panelForm = new JPanel(new GridLayout(5, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        panelForm.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelForm.add(txtNombre);
        
        panelForm.add(new JLabel("Marca:"));
        txtMarca = new JTextField();
        panelForm.add(txtMarca);
        
        panelForm.add(new JLabel("Modelo:"));
        txtModelo = new JTextField();
        panelForm.add(txtModelo);
        
        panelForm.add(new JLabel("Precio:"));
        txtPrecio = new JTextField();
        panelForm.add(txtPrecio);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnAgregar = new JButton("Agregar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");
        
        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        
        // Tabla de equipos
        modelo = new DefaultTableModel(new String[]{"ID", "Nombre", "Marca", "Modelo", "Precio"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Para evitar la edición directa en la tabla
            }
        };
        
        tablaEquipos = new JTable(modelo);
        tablaEquipos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Listeners
        btnAgregar.addActionListener(this::agregarEquipo);
        btnActualizar.addActionListener(this::actualizarEquipo);
        btnEliminar.addActionListener(this::eliminarEquipo);
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        tablaEquipos.getSelectionModel().addListSelectionListener(e -> cargarDatosSeleccionados());
        
        // Diseño principal
        add(panelForm, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);
        
        // Agregar la tabla con un JScrollPane para permitir el desplazamiento
        JScrollPane scrollPane = new JScrollPane(tablaEquipos);
        scrollPane.setPreferredSize(new Dimension(780, 300));
        add(scrollPane, BorderLayout.SOUTH);
        
        // Cargar datos iniciales
        cargarDatos();
    }
    
    private void cargarDatos() {
        try {
            modelo.setRowCount(0);  // Limpiar la tabla
            List<Equipo> equipos = controller.listar();
            for (Equipo e : equipos) {
                modelo.addRow(new Object[]{
                    e.getId(), 
                    e.getNombre(), 
                    e.getMarca(), 
                    e.getModelo(), 
                    e.getPrecio()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                this, 
                "Error al cargar datos: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void cargarDatosSeleccionados() {
        int fila = tablaEquipos.getSelectedRow();
        if (fila >= 0) {
            idSeleccionado = (int) modelo.getValueAt(fila, 0);
            txtNombre.setText(modelo.getValueAt(fila, 1).toString());
            txtMarca.setText(modelo.getValueAt(fila, 2).toString());
            txtModelo.setText(modelo.getValueAt(fila, 3).toString());
            txtPrecio.setText(modelo.getValueAt(fila, 4).toString());
        }
    }
    
    private void agregarEquipo(ActionEvent e) {
        try {
            validarCampos();
            Equipo equipo = new Equipo(
                0,  // El ID será asignado por la base de datos
                txtNombre.getText(),
                txtMarca.getText(),
                txtModelo.getText(),
                Double.parseDouble(txtPrecio.getText())
            );
            
            controller.insertar(equipo);
            JOptionPane.showMessageDialog(
                this, 
                "Equipo agregado con éxito", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE
            );
            
            cargarDatos();
            limpiarFormulario();
        } catch (Exception ex) {
            mostrarError(ex);
        }
    }
    
    private void actualizarEquipo(ActionEvent e) {
        try {
            if (idSeleccionado == -1) {
                throw new IllegalArgumentException("Seleccione un equipo para actualizar");
            }
            
            validarCampos();
            Equipo equipo = new Equipo(
                idSeleccionado,
                txtNombre.getText(),
                txtMarca.getText(),
                txtModelo.getText(),
                Double.parseDouble(txtPrecio.getText())
            );
            
            controller.actualizar(equipo);
            JOptionPane.showMessageDialog(
                this, 
                "Equipo actualizado con éxito", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE
            );
            
            cargarDatos();
            limpiarFormulario();
        } catch (Exception ex) {
            mostrarError(ex);
        }
    }
    
    private void eliminarEquipo(ActionEvent e) {
        try {
            if (idSeleccionado == -1) {
                throw new IllegalArgumentException("Seleccione un equipo para eliminar");
            }
            
            int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar este equipo?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                controller.eliminar(idSeleccionado);
                JOptionPane.showMessageDialog(
                    this, 
                    "Equipo eliminado con éxito", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE
                );
                
                cargarDatos();
                limpiarFormulario();
            }
        } catch (Exception ex) {
            mostrarError(ex);
        }
    }
    
    private void validarCampos() throws IllegalArgumentException {
        if (txtNombre.getText().isEmpty() || txtPrecio.getText().isEmpty()) {
            throw new IllegalArgumentException("Nombre y precio son obligatorios");
        }
        
        try {
            double precio = Double.parseDouble(txtPrecio.getText());
            if (precio <= 0) {
                throw new IllegalArgumentException("El precio debe ser mayor a cero");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Precio debe ser un número válido");
        }
    }
    
    private void limpiarFormulario() {
        idSeleccionado = -1;
        txtNombre.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtPrecio.setText("");
        tablaEquipos.clearSelection();
    }
    
    private void mostrarError(Exception ex) {
        JOptionPane.showMessageDialog(
            this, 
            "Error: " + ex.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE
        );
    }
}