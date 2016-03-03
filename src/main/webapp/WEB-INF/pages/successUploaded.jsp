<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>Добавление книги в библиотеку</title>
    </head>
    <body>
        <br />
        <br />
        <center>
            <h3>
                <strong> ${message}</strong>
                <br /> 
                <c:if test="${failed==false}">
                    <a href = "adminIndex.htm">На главную</a>
                </c:if>
                <br />
                <c:if test="${failed==true}">
                    <a href = "addBook.htm">Добавить книгу</a>
                </c:if>
            </h3>
        </center>
    </body>
</html>
