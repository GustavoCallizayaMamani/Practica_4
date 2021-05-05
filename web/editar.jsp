<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.emergentes.modelo.Tarea" %>
<%
    Tarea libro = (Tarea) request.getAttribute("libro");
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html >
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body >
        <h1>
            <c:if test="${libro.id==0}">Nueva Tarea</c:if>
            <c:if test="${libro.id!=0}">Editar Tarea</c:if>
        </h1>
        <form action="MainController" method="post">
            <table >
                <input type="hidden" name="id" value="${libro.id}">
                <tr>
                    <td>TAREA</td>
                    <td><input type="text" name="tarea" value="${libro.tarea}"></td>
                </tr>
                <tr>
                    <td>PRIORIDAD</td>
                    <td><input type="text" name="prioridad" value="${libro.prioridad}"></td>
                </tr>
                <tr>
                    <td>COMPLETADO</td>
                    <td><input type="text" name="completado" value="${libro.completado}"></td>
                </tr>
                <tr>
                    <td></td>
                    <td><input type="submit" value="Enviar" ></td>
                </tr>
            </table>
        </form>
    </body>
</html>
