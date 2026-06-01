/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import modelo.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author ASUS ROG G14
 */


public class FacturaDAO {

    public String generarNumeroFactura() {
        String numero = "000001"; 
        String sql = "SELECT MAX(id_factura) AS id FROM facturas";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                int idMax = rs.getInt("id");
                if (idMax > 0) {
                    numero = String.format("%06d", idMax + 1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al generar número: " + e.getMessage());
        }
        return numero;
    }

    public boolean registrarVenta(modelo.Factura fac, java.util.List<modelo.DetalleFactura> listaDetalles) {
        Connection con = null;
        try {
            con = Conexion.conectar();
            con.setAutoCommit(false); 

            String sqlFac = "INSERT INTO facturas (numero_factura, id_cliente, id_usuario, subtotal, porcentaje_iva, total_iva, total) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id_factura";
            PreparedStatement psFac = con.prepareStatement(sqlFac);
            
            psFac.setString(1, fac.getNumeroFactura());

            if (fac.getIdCliente() == 0) {
                psFac.setNull(2, java.sql.Types.INTEGER);
            } else {
                psFac.setInt(2, fac.getIdCliente());
            }
            
            psFac.setInt(3, fac.getIdUsuario());
            psFac.setDouble(4, fac.getSubtotal());
            psFac.setDouble(5, fac.getPorcentajeIva());
            psFac.setDouble(6, fac.getTotalIva());
            psFac.setDouble(7, fac.getTotal());

            ResultSet rs = psFac.executeQuery();
            int idFacturaGenerada = 0;
            if (rs.next()) {
                idFacturaGenerada = rs.getInt(1);
            }

            String sqlDet = "INSERT INTO detalles_factura (id_factura, id_producto, cantidad, precio_unitario, subtotal_item) VALUES (?, ?, ?, ?, ?)";
            String sqlStock = "UPDATE productos SET stock = stock - ? WHERE id_producto = ?";
            
            PreparedStatement psDet = con.prepareStatement(sqlDet);
            PreparedStatement psStock = con.prepareStatement(sqlStock);

            for (modelo.DetalleFactura det : listaDetalles) {
                psDet.setInt(1, idFacturaGenerada);
                psDet.setInt(2, det.getIdProducto());
                psDet.setInt(3, det.getCantidad());
                psDet.setDouble(4, det.getPrecioUnitario());
                psDet.setDouble(5, det.getSubtotalItem());
                psDet.executeUpdate();

                psStock.setInt(1, det.getCantidad());
                psStock.setInt(2, det.getIdProducto());
                psStock.executeUpdate();
            }

            con.commit();
            return true;
            
        } catch (SQLException e) {
            try {
                if (con != null) con.rollback(); 
            } catch (SQLException ex) {
                System.out.println("Error en rollback: " + ex.getMessage());
            }
            javax.swing.JOptionPane.showMessageDialog(null, "Error al guardar venta: " + e.getMessage());
            return false;
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException ex) {
                System.out.println("Error cerrando conexión: " + ex.getMessage());
            }
        }
    }
}
