/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;
import java.sql.*;
import javax.swing.JOptionPane;
/**
 *
 * @author ASUS ROG G14
 */
public class Conexion {
private static final String URL = "jdbc:postgresql://localhost:5432/visual"; 
    private static final String USUARIO = "postgres";
    private static final String PASSWORD = "root"; 

    
    public static Connection conectar() {
        Connection conexion = null;
        try {
            
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("Conectadardo");
            
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error: No se encontró el driver de PostgreSQL.\n" + e.getMessage(), "Error de Driver", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de conexión a la base de datos.\n" + e.getMessage(), "Error de Conexión", JOptionPane.ERROR_MESSAGE);
        }
        return conexion;
    }
    
}
