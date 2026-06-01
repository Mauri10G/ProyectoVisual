/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import modelo.Conexion;
import modelo.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author ASUS ROG G14
 */
public class ClienteDAO {

    public Cliente buscarPorCedula(String cedula) {
        Cliente cli = null;
        String sql = "SELECT id_cliente, identificacion, nombre FROM clientes WHERE identificacion = ?";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, cedula);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                cli = new Cliente();
                cli.setIdCliente(rs.getInt("id_cliente"));
                cli.setIdentificacion(rs.getString("identificacion"));
                cli.setNombre(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar cliente: " + e.getMessage());
        }
        return cli;
    }
    
    public boolean insertar(Cliente cli) {
        String sql = "INSERT INTO clientes (identificacion, nombre, correo, telefono) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cli.getIdentificacion());
            ps.setString(2, cli.getNombre());
            ps.setString(3, cli.getCorreo());
            ps.setString(4, cli.getTelefono());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Error al guardar cliente: " + e.getMessage());
            return false;
        }
    }

    public java.util.List<Cliente> listar() {
        java.util.List<Cliente> listaCli = new java.util.ArrayList<>();
        String sql = "SELECT * FROM clientes ORDER BY id_cliente ASC";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Cliente cli = new Cliente();
                cli.setIdCliente(rs.getInt("id_cliente"));
                cli.setIdentificacion(rs.getString("identificacion"));
                cli.setNombre(rs.getString("nombre"));
                cli.setCorreo(rs.getString("correo"));
                cli.setTelefono(rs.getString("telefono"));
                listaCli.add(cli);
            }
        } catch (SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Error al listar clientes: " + e.getMessage());
        }
        return listaCli;
    }

    public boolean actualizarPorCedula(Cliente cli, String cedulaAnterior) {
        String sql = "UPDATE clientes SET identificacion = ?, nombre = ?, correo = ?, telefono = ? WHERE identificacion = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cli.getIdentificacion());
            ps.setString(2, cli.getNombre());
            ps.setString(3, cli.getCorreo());
            ps.setString(4, cli.getTelefono());
            ps.setString(5, cedulaAnterior);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Error al actualizar cliente: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarPorCedula(String cedula) {
        String sql = "DELETE FROM clientes WHERE identificacion = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cedula);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Error al eliminar cliente: " + e.getMessage());
            return false;
        }
    }
}