<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Главная</title>
    </head>
    <body>
        <p style="text-align:right">Текущий пользователь: ${userName} <br />
            <a href ="index.htm">Выйти</a></p>
        <center>
            <form action="adminIndex.htm" method="post">
                <input type="search" placeholder="Введите название книги или описание" size="100" name = "search"/>
                <input type="submit" value="Поиск"/>    
                <a href ="addBook.htm">Добавить книгу</a>    
                <a href ="addPicture.htm">Добавить изображение к книге</a>
            </form><br />
            <h>${headerMessage}</h><br /><br />
            <h>${book}</h>
            <table>
                <c:forEach var="books" items="${books}" >
                    <tr> <td>${books}</td> </tr>
                </c:forEach>
            </table>
        </center>
    </body>
</html>
