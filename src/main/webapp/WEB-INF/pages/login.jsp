<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Вход</title>
    </head>
    <body>
        <center>
            <h2>Вход на сайт</h2><br/>
            <form method = "post" action="loginProcess.htm">
                Логин: <input name = "userName" type ="text"  /><br/>
                Пароль: <input name = "password" type ="password" /><br/>
                <input type ="submit" value = "Вход" />
            </form>
        </center>
    </body>
</html>
