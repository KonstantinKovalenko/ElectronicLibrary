<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Book Page</title>
    </head>
    <body>
        <p style="text-align:right">Текущий пользователь: ${userName} <br />
            <a href ="index.htm">Выйти</a></p>
        <p><img src = "img.jpg" align="left" height="300" width="200">Название: ${bookName}</p>
        <p>Автор: ${bookAuthor}</p>
        <p>Издательство: ${bookPublishingHouse}</p>
        <p>${bookDescription}</p>
        <p><a href = "downloadBook.dl">Скачать книгу</a></p>
        <br /><br /><br /><br />
        <center>
            <p>Комментарии:</p>
            <table>
                <c:forEach var="usersAndComments" items="${usersAndComments}" >
                    <tr><td>${usersAndComments}</td></tr>
                </c:forEach>
            </table>
            <h>Добавить комментарий:</h>
            <form action ="bookPage.htm">
                <textarea name = "addComment" rows="10" cols="30"></textarea><br/>
                <input type ="submit" value="Добавить">
            </form><br />
            <c:if test="${adminRights == true}"><a href = "adminIndex.htm">На главную</a></c:if>
            <c:if test="${adminRights == false}"><a href = "userIndex.htm">На главную</a></c:if>
        </center> 

    </body>
</html>
