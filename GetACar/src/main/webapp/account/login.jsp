<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Get A Car Login</title>
    <link rel="SHORTCUT ICON" href="../resources/images/favicon.ico"/>
    <link rel="stylesheet" href="../resources/css/login.css"/>
</head>
<body>
<div id="loginPanel">
    <form name="loginform" action="" method="post">
        <div>
            <label for="username">Benutzername</label>
        </div>
        <div>
            <input type="text" name="username" id="username" maxlength="30">
        </div>
        <div>
            <label for="password">Passwort</label>
        </div>
        <div><input type="password" name="password" id="password" maxlength="30">
        </div>
        <%--<tr>--%>
        <%--<td colspan="2" align="left"><input type="checkbox" name="rememberMe"><font size="2">Remember Me</font>--%>
        <%--</td>--%>
        <%--</tr>--%>
        <div>
            <input type="submit" name="submit" value="Login">
        </div>
    </form>
</div>
</body>
</html>