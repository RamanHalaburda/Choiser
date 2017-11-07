<%-- 
    Document   : Welcome
    Created on : 01.11.2017, 21:22:54
    Author     : Maria
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Welcome - Choiser</title>        
        <link rel="stylesheet" type="text/css" href="css/style.css">
        <script type="text/javascript" src="js/script.js"></script>
    </head>
    <body onload="datetime()">
        <div class="clock"> 
                <form name="form_clock">
                    <p><input id="inputdate" name="date" type="text" name="date" value="" size="14" disabled="true"></p>
                    <p><input id="inputtime" name="time" type="text" name="time" value="" size="14" disabled="true"></p>
                </form> 
        </div>
        <div class="page-wrapper">
            <center>
                <h1>Привествуем в Choiser!</h1>
                <h1>Сервис для множественного голосования.</h1><br><br><br>
                <h2>Авторизация</h2><br><br>
                <form name="Verification" action="Verification" method="POST">
                    <input placeholder="Логин" type="text" name="login" value="" />
                    <br><br>
                    <input placeholder="Пароль" type="text" name="password" value="" />
                    <br><br>
                    <input type="checkbox" value="admin" name="isadmin" /> Я администратор
                    <br><br>
                    <input type="submit" value="Авторизоваться" name="do" />
                    <br><br>
                    <span><a href="Registration">Регистрация</a></span>
                </form>
            </center>
        </div>
    </body>
</html>
