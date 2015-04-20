<%@page import="cardholders.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    AccessEvents ae = new AccessEvents();
    pageContext.setAttribute("accessFaults", ae.getAccessFaults());
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Загорская ГАЭС</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <link href="config/css/metro-bootstrap.css" rel="stylesheet">
        <link href="config/css/metro-bootstrap-responsive.css" rel="stylesheet">
        <link href="config/css/faults.css" rel="stylesheet">
        <!-- Load JavaScript Libraries -->
        <script src="config/js/jquery/jquery-2.1.1.min.js"></script>
        <script src="config/js/jquery/jquery.widget.min.js"></script>
        <script src="config/js/jquery/jquery.mousewheel.js"></script>
    </head>
    <body class="skud">
        <div class="topdiv">
            <div class="title">Нарушения объектового режима за сутки</div>
            <hr id="hrid">
            <!-- server generated place --> 
            <div>
                <div class="small-photo br-red">
                    <div>
                        <div>
                            <img src="00003818.jpg">
                        </div>
                    </div>	
                    <div class="content">Евсеев Виктор Иванович</div>
                </div>	
                <select size="10">
                    <c:forEach items="${accessFaults}" var="af">
                        <c:if test="${af != null}">
                            <option>${af.getHoldername()}</option>
                        </c:if>
                    </c:forEach>
                    <option>00:15 Сбой по причине местного нарушения</option>
                    <option>10:15 Сбой2</option>
                    <option>00:15 Сбой по причине местного нарушения</option>
                    <option>10:15 Сбой2</option>
                    <option>00:15 Сбой по причине местного нарушения</option>
                    <option>10:15 Сбой2</option>
                    <option>00:15 Сбой по причине местного нарушения</option>
                    <option>10:15 Сбой2</option>
                </select>	
            </div>
            &nbsp;
            <!-- end of server gererated place -->
        </div>
    </body>
</html>
