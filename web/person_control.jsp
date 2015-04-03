<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="personcontrol.*" import="java.util.ArrayList" %>
<% 
    PcWSOAdapters adp = new PcWSOAdapters();
    pageContext.setAttribute("notVipCHs", adp.getNotVipCHs()); 
    pageContext.setAttribute("vipCHs", adp.getVipCHs());
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Настройка</title>
        <link rel="stylesheet" href="config/main.css" />
    </head>
    <body bgcolor="#1e1e1e">
        <div class="topdiv">
            <div class="title">
                Спецконтроль
                <div style="float:right;">
                    <img src="config/persons.png" width="62" height="62">
                </div>
            </div>
            <hr class="hrclass">
            <form action="">
                <div class="title divclass" >
                    Список посетителей
                </div>
                <select name="source" size="20" multiple class="selectclass">
                    <c:forEach items="${notVipCHs}" var="ch">
                        <c:if test="${ch != null}">
                            <option value="${ch.getId()}">${ch.getName()}</option>
                        </c:if>
                    </c:forEach>
                </select>
                <input type="submit" id="select" value=" Выбрать " class="baton inputclass"  />
                <input type="submit" id="remove" value=" Удалить " class="baton inputclass2"  />
                <div class="title divclass2" >
                    Список спец. контроля
                </div>
                <select name="selected" size="10" class="selectclass2">
                    <c:forEach items="${vipCHs}" var="ch">
                        <c:if test="${ch != null}">
                            <option value="${ch.getId()}">${ch.getName()}</option>
                        </c:if>
                    </c:forEach>
                </select>
                <input type="submit" id="ok" value=" OK " class="baton inputclass3" ></input>
                <input type="submit" id="cancel" value=" Отменить " class="baton inputclass4" ></input>
            </form>
        </div>
    </body>
</html>