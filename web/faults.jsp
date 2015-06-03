<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="cardholders.*"%>
<%
    ChsCfg cfg = new ChsCfg();
    String grhost = cfg.getGRhost();
    String ashost = cfg.getAShost();
    EventsHundler eh = new EventsHundler(cfg);
    eh.fillFaultsPerHolders();
    pageContext.setAttribute("photoRootPath", "http://" + grhost + ":9763/registry/resource/_system/governance/ssoi/cardholders/photo/");
    pageContext.setAttribute("faultsPerHolders", eh.getFaultsPerHolders());
    pageContext.setAttribute("webinf", "http://" + grhost + ":9763/registry/resource/_system/governance/ssoi/web");
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Загорская ГАЭС</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <link href="${webinf}/css/metro-bootstrap.css" rel="stylesheet">
        <link href="${webinf}/css/metro-bootstrap-responsive.css" rel="stylesheet">
        <link href="${webinf}/css/faults.css" rel="stylesheet">
        <!-- Load JavaScript Libraries -->
        <script src="${webinf}/js/jquery/jquery-2.1.1.min.js"></script>
        <script src="${webinf}/js/jquery/jquery.widget.min.js"></script>
        <script src="${webinf}/js/jquery/jquery.mousewheel.js"></script>
        <script src="${webinf}/js/config.js" type="text/javascript"></script>
        <script type="text/javascript">
            initParam('<%=ashost%>', '<%=grhost%>');
        </script>
        <script src="${webinf}/js/faults.js"></script>
    </head>
    <body class="skud" onload="descDivById();">
        <div class="topdiv">
            <div class="title">Нарушения объектового режима за сутки</div>
            <hr id="hrid"/>
            <!-- server generated place --> 
            <div id="maindiv">
                <c:if test="${faultsPerHolders != null}">
                    <c:forEach items="${faultsPerHolders.keySet()}" var="key">
                        <c:if test="${key != null}">
                            <div id="${faultsPerHolders.get(key).get(0).getEventtime()}">
                                <div class="small-photo br-red">
                                    <div>
                                        <div>
                                            <c:if test='${key.getKey() != null && !"".equals(key.getKey())}'>
                                                <c:set var="photoPath" value="${photoRootPath}${key.getKey()}.jpg" />
                                                <img src="${photoPath}"/>
                                            </c:if>
                                        </div>
                                    </div>
                                    <c:set var="holderName" value="${key.getValue()}" />
                                    <c:if test='${key.getValue() == null || "".equals(key.getValue())}'>
                                        <c:set var="holderName" value="НЕИЗВЕСТНЫЙ" />
                                    </c:if>
                                    <div class="content">${holderName}</div>
                                </div>	
                                <select size="10" style="width: 75%">
                                    <c:forEach items="${faultsPerHolders.get(key)}" var="event">
                                        <c:if test="${event != null}">
                                            <c:set var="eventDescD" value='${fn:substringAfter(event.getEventtypedesc(), "Доступ запрещен, ")}'></c:set>
                                            <c:if test='${"".equals(eventDescD)}'>
                                                <c:set var="eventDescD" value='${fn:substringAfter(event.getEventtypedesc(), "Доступ запрещён, ")}'></c:set>
                                            </c:if>
                                            <c:if test='${"".equals(eventDescD)}'>
                                                <c:set var="eventDescD" value='${fn:substringAfter(event.getEventtypedesc(), "Доступ посетителю запрещён, ")}'></c:set>
                                            </c:if>
                                            <c:if test='${"".equals(eventDescD)}'>
                                                <c:set var="eventDescD" value='${fn:substringAfter(event.getEventtypedesc(), "Доступ посетителю запрещен, ")}'></c:set>
                                            </c:if>
                                            <c:choose>
                                                <c:when test='${!"".equals(eventDescD)}'>
                                                    <c:set var="eventDesc" value="${fn:toUpperCase(fn:substring(eventDescD, 0, 1))}${fn:substring(eventDescD, 1, fn:length(eventDescD))}"></c:set>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:set var="eventDesc" value="${event.getEventtypedesc()}"></c:set>
                                                </c:otherwise>
                                            </c:choose>
                                            <option>${fn:substringBefore(fn:substringAfter(event.getEventtime(), "T"), "+")}&nbsp;${eventDesc}&nbsp;[${event.getSourcename()}]</option>
                                        </c:if>
                                    </c:forEach>
                                </select>	
                                &nbsp;
                                <br></br>
                            </div>
                        </c:if>
                    </c:forEach>
                </c:if>

            </div>
            <!-- end of server gererated place -->
        </div>

    </body>
</html>
