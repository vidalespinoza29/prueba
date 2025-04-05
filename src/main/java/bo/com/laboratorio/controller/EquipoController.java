/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bo.com.laboratorio.controller;

/**
 *
 * @author Windows
 */

import bo.com.laboratorio.database.ConexionBD;
import bo.com.laboratorio.exceptions.CredencialesInvalidasException;
import bo.com.laboratorio.model.Equipo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipoController {
    
    // Método para validar credenciales
    public boolean validarCredenciales(String usuario, String password) throws CredencialesInvalidasException {
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND password = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return true;
                } else {
                    throw new CredencialesInvalidasException("Usuario o contraseña incorrectos");
                }
            }
        } catch (SQLException e) {
            throw new CredencialesInvalidasException("Error de conexión: " + e.getMessage());
        }
    }
    
    // Método para insertar un equipo
    public void insertar(Equipo equipo) throws SQLException {
        String sql = "INSERT INTO equipos (nombre, marca, modelo, precio) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, equipo.getNombre());
            stmt.setString(2, equipo.getMarca());
            stmt.setString(3, equipo.getModelo());
            stmt.setDouble(4, equipo.getPrecio());
            stmt.executeUpdate();
        }
    }
    
    // Método para listar todos los equipos
    public List<Equipo> listar() throws SQLException {
        List<Equipo> equipos = new ArrayList<>();
        String sql = "SELECT * FROM equipos";
        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                equipos.add(new Equipo(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    rs.getDouble("precio")
                ));
            }
        }
        return equipos;
    }
    
    // Método para actualizar un equipo
    public void actualizar(Equipo equipo) throws SQLException {
        String sql = "UPDATE equipos SET nombre = ?, marca = ?, modelo = ?, precio = ? WHERE id = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, equipo.getNombre());
            stmt.setString(2, equipo.getMarca());
            stmt.setString(3, equipo.getModelo());
            stmt.setDouble(4, equipo.getPrecio());
            stmt.setInt(5, equipo.getId());
            stmt.executeUpdate();
        }
    }
    
    // Método para eliminar un equipo
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM equipos WHERE id = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
