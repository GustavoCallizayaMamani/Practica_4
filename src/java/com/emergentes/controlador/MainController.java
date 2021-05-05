
package com.emergentes.controlador;

import com.emergentes.ConexionDB;
import com.emergentes.modelo.Tarea;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
public class MainController extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String op;
            op = (request.getParameter("op") != null) ? request.getParameter("op") : "list";
            ArrayList<Tarea> lista = new ArrayList<Tarea>();
            ConexionDB canal = new ConexionDB();
            Connection conn = canal.conectar();
            PreparedStatement ps;
            ResultSet rs;
            if (op.equals("list")){
                //Para listar los datos
                String sql = "select * from tareas";
                //Consulta de seleccion y almacenarlo en una coleccion
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();
                while   (rs.next()){
                    Tarea lib = new Tarea();
                    lib.setId(rs.getInt("id"));
                    lib.setTarea(rs.getString("tarea"));
                    lib.setPrioridad (rs.getInt("prioridad"));
                    lib.setCompletado(rs.getInt("completado"));
                    lista.add(lib);
                }
                request.setAttribute ("lista", lista);
                //Enviar al index.jsp para mostrat la informacion
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }
            if (op.equals("nuevo")) {
                //instanciar un objeto de la clase Tarea
                Tarea li = new Tarea();            
                //El objeto se pose como atributo de request
                request.setAttribute("libro", li);
                //Redireccionar  a editar.jsp
                request.getRequestDispatcher("editar.jsp").forward (request, response);    
            }
            if (op.equals("editar")) {
                int id = Integer.parseInt(request.getParameter("id"));   
                String sql = "select * from tareas where id = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                rs = ps.executeQuery();
                Tarea li = new Tarea(); 
                while   (rs.next()){
                    li.setId(rs.getInt("id"));
                    li.setTarea(rs.getString("tarea"));
                    li.setPrioridad (rs.getInt("prioridad"));
                    li.setCompletado(rs.getInt("completado"));
                }
                request.setAttribute("libro", li);
                request.getRequestDispatcher("editar.jsp").forward(request, response);
            }
            if (op.equals("eliminar")){
                //obtener el id
                int id = Integer.parseInt(request.getParameter("id"));
                //Realizar la eliminacion en la base de datos
                String sql = "delete from tareas where id = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                ps.executeUpdate();
                //Redireccionar a MainController
                response.sendRedirect("MainController");
            }
        } catch (SQLException ex) {
            System.out.println("ERROR AL CONECTAR  "+ ex.getMessage());
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String tarea = request.getParameter("tarea");
            int prioridad = Integer.parseInt(request.getParameter("prioridad"));
            int completado = Integer.parseInt(request.getParameter("completado"));
            
            Tarea lib = new Tarea();
            lib.setId(id);
            lib.setTarea(tarea);
            lib.setPrioridad(prioridad);
            lib.setCompletado(completado);
            
            ConexionDB canal = new ConexionDB();
            Connection conn = canal.conectar();
            PreparedStatement ps;
            ResultSet rs;
            if (id == 0){
                //Nuevo registro
                String sql = "insert into tareas (tarea,prioridad,completado) values (?,?,?)";
                ps = conn.prepareStatement(sql);
                ps.setString (1, lib.getTarea());
                ps.setInt (2, lib.getPrioridad());
                ps.setInt (3, lib.getCompletado());
                ps.executeUpdate();
                
                
            } 
                    
            //si el registro ya existe
            else{
                String sql = "update tareas set tarea=?,prioridad=?,campletado=? where id = ?";
                ps = conn.prepareStatement(sql);
                ps.setString (1, lib.getTarea());
                ps.setInt (2, lib.getPrioridad());
                ps.setInt (3, lib.getCompletado());
                ps.setInt(4, lib.getId());
                ps.executeUpdate();
            }
            response.sendRedirect("MainController");
        } catch (SQLException ex) {
            System.out.println("Error en SQL " + ex.getMessage());
        }
    }

}
