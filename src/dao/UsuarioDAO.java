/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import modelo.Conexion;
import modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
/**
 *
 * @author ASUS ROG G14
 */
public class UsuarioDAO {
    public boolean insertar(Usuario usr) {
        String sql = "INSERT INTO usuarios (username, password_hash, id_rol, activo) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, usr.getUsername());
            ps.setString(2, usr.getPassword());
            ps.setInt(3, usr.getIdRol());
            ps.setBoolean(4, usr.isActivo());
            
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar usuario: " + e.getMessage());
            return false;
        }
    }

    public List<Usuario> listar() {
        List<Usuario> listaUsr = new ArrayList<>();
        String sql = "SELECT u.id_usuario, u.username, u.password_hash, u.id_rol, r.nombre as nombre_rol, u.activo " +
                     "FROM usuarios u INNER JOIN roles r ON u.id_rol = r.id_rol ORDER BY u.id_usuario ASC";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Usuario usr = new Usuario();
                usr.setIdUsuario(rs.getInt("id_usuario"));
                usr.setUsername(rs.getString("username"));
                usr.setPassword(rs.getString("password_hash"));
                usr.setIdRol(rs.getInt("id_rol"));
                usr.setNombreRol(rs.getString("nombre_rol"));
                usr.setActivo(rs.getBoolean("activo"));
                listaUsr.add(usr);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar usuarios: " + e.getMessage());
        }
        return listaUsr;
    }

    public boolean actualizarPorUsername(Usuario usr, String usernameAnterior) {
        String sql = "UPDATE usuarios SET username = ?, password_hash = ?, id_rol = ?, activo = ? WHERE username = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, usr.getUsername());
            ps.setString(2, usr.getPassword());
            ps.setInt(3, usr.getIdRol());
            ps.setBoolean(4, usr.isActivo());
            ps.setString(5, usernameAnterior);
            
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarPorUsername(String username) {
        String sql = "DELETE FROM usuarios WHERE username = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    public List<String> consultarRoles() {
        List<String> listaRoles = new ArrayList<>();
        String sql = "SELECT nombre FROM roles ORDER BY id_rol ASC";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                listaRoles.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al consultar roles: " + e.getMessage());
        }
        return listaRoles;
    }
    
    public Usuario validarLogin(String username, String password) {
        Usuario usr = null;
        String sql = "SELECT id_usuario, username, id_rol FROM usuarios WHERE username = ? AND password_hash = ? AND activo = true";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                usr = new Usuario();
                usr.setIdUsuario(rs.getInt("id_usuario"));
                usr.setUsername(rs.getString("username"));
                usr.setIdRol(rs.getInt("id_rol"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al conectar en login: " + e.getMessage());
        }
        return usr;
    }
}