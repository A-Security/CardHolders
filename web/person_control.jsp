<%@page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="personcontrol.*"%>
<%@page import="java.util.ArrayList"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<% 
    PcWSOAdapters adp = new PcWSOAdapters();
    if (adp.fillCHsSets()){
        pageContext.setAttribute("notVipCHs", adp.getNotVipCHs()); 
        pageContext.setAttribute("vipCHs", adp.getVipCHs());
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Configuration</title>
        <link rel="stylesheet" href="config/css/person_control.css" />
        <script src="config/js/person_control.js" type="text/javascript"></script>
    </head>
    <body bgcolor="#1e1e1e">
        <div class="topdiv">ssh
            <hr class="hrclass">
            <form name="main" action="TransAction" method="POST">
                <div class="title divclass" >
                    Source list
                </div>
                <select name="notvipch" size="20"  multiple="true" class="selectclass">
                    <c:forEach items="${notVipCHs}" var="ch">
                        <c:if test="${ch != null}">
                            <option value="${ch.getId()}">${ch.getName()}</option>
                        </c:if>
                    </c:forEach>
                </select>
                <input type="button" value=" Выбрать " class="baton inputclass" onclick="addVip()" />
                <input type="button" value=" Удалить " class="baton inputclass2" onclick="remVip()" />
                <div class="title divclass2" >
                    Destination list
                </div>
                <select name="vipch" multiple="true" size="10" class="selectclass2">
                    <c:forEach items="${vipCHs}" var="ch">
                        <c:if test="${ch != null}">
                            <option value="${ch.getId()}">${ch.getName()}</option>
                        </c:if>
                    </c:forEach>
                </select>
                <input type="submit" name="ok" value=" OK " class="baton inputclass3" onclick="selAllSelect()"/>
                <input type="submit" name="cancel" value=" Отменить " class="baton inputclass4"/>
            </form>
        </div>
    </body>
</html>