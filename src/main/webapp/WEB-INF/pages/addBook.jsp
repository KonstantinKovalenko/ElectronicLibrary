<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Добавление книги</title>
    </head>
    <body><center>
            <spring:form method="post" enctype="multipart/form-data" modelAttribute="uploadedFile" action="successUploaded.htm">
                Выберите файл: <input type="file" name="file"><br /> <br/>

                Название: <input type = "text" name = "name" /><br/><br/>
                Автор: <input type = "text" name = "author" /><br/><br/>
                Издательство: <input type = "text" name = "publishingHouse" /><br/><br/>
                Описание: <input type = "text" name = "description" /><br/><br/>

                <input type="submit" value="Добавить книгу">
            </spring:form>
        </center>
    </body>
</html>
