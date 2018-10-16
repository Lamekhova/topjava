<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Users</title>
    <style>
        .normal {
            color: aquamarine
        }

        .exceed {
            color: darkred
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>MealList</h2>
<table border="1">
    <thead>
    <tr>
        <th>Date</th>
        <th>Descriptions</th>
        <th>Calories</th>
    </tr>
    </thead>
    <c:forEach items="${mealList}" var="meal">
        <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.model.MealWithExceed"/>
        <tr class="${meal.exceed ? 'exceed': 'normal'}">
            <td>
                <%= TimeUtil.toString(meal.getDateTime())%>
            </td>>
            <td> ${meal.description}</td>>
            <td> ${meal.calories}</td>>
        </tr>
    </c:forEach>
</table>

</body>
</html>