<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="com.asecurity.esbdb.ApacseventsCha"%>
<%@page import="java.util.List"%>
<%@page import="cardholders.*"%>
<%
    EventsHundler eh = new EventsHundler();
    eh.fillFaultsPerHolders();
    pageContext.setAttribute("photoRootPath", eh.getPhotoPath());
    pageContext.setAttribute("faultsPerHolders", eh.getFaultsPerHolders());
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
        <script src="config/js/faults.js"></script>
        <script src="config/js/jquery/jquery.widget.min.js"></script>
        <script src="config/js/jquery/jquery.mousewheel.js"></script>
    </head>
    <body class="skud">
        <div class="topdiv">
            <div class="title">Нарушения объектового режима за сутки</div>
            <hr id="hrid"/>
            <!-- server generated place --> 
            <c:if test="${faultsPerHolders != null}">
                <c:forEach items="${faultsPerHolders.entrySet()}" var="entrySet">
                    <c:if test="${entrySet != null}">
                        <c:set var="holder" value="${entrySet.getKey()}" />
                        <div>
                            <div class="small-photo br-red">
                                <div>
                                    <div>
                                        <c:if test='${holder.getKey() != null && !holder.getKey().equals("")}'>
                                            <c:set var="photoPath" value="${photoRootPath}${holder.getKey()}.jpg" />
                                            <img src="${photoPath}"/>
                                        </c:if>
                                    </div>
                                </div>	
                                <div class="content">${holder.getValue()}</div>
                            </div>	
                            <select size="10" style="width: 75%">
                                <c:forEach items="${entrySet.getValue()}" var="event">
                                    <c:if test="${event != null}">
                                        <option>${fn:substringAfter(event.getEventtime(), "T")}&nbsp;${event.getEventtypedesc()}&nbsp;[${event.getSourcename()}]</option>
                                    </c:if>
                                </c:forEach>
                            </select>	
                        </div>
                    </c:if>
                    &nbsp;
                </c:forEach>
            </c:if>
            <!-- end of server gererated place -->
        </div>
    </body>
</html>
