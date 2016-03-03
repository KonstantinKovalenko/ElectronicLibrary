<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Добавление картинки</title>
    </head>
    <body><center>
            <spring:form method="post" enctype="multipart/form-data" modelAttribute="uploadedFile" action="successUploadedPicture.htm">
                <input type="file" name="file"><br /> 
                Название: <input type = "text" name = "name" /><br/>
                <input type="submit" value="Добавить">
                Press here to upload the file!
            </spring:form>
        </center>
    </body>
</html>
