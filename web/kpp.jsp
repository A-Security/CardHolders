<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cardholders.*"%>
<%
    ChsCfg cfg = new ChsCfg();
    String grhost = cfg.getGRhost();
    String ashost = cfg.getAShost();
    pageContext.setAttribute("webinf", "http://" + grhost + ":9763/registry/resource/_system/governance/ssoi/web");
%>
﻿<!DOCTYPE html>
<html>
    <head>
        <title>Загорская ГАЭС</title>
        <meta content="text/html; windows-1251" http-equiv="Content-Type">
        <meta content="no-cache, no-store, must-revalidate" http-equiv="Cache-control">
        <meta content="no-cache" http-equiv="Pragma">
        <meta content="0" http-equiv="Expires">

        <link href="${webinf}/css/metro-bootstrap.css" rel="stylesheet">
        <link href="${webinf}/css/metro-bootstrap-responsive.css" rel="stylesheet">
        <link href="${webinf}/css/kpp.css" rel="stylesheet">
        <!-- Load JavaScript Libraries -->
        <script src="${webinf}/js/jquery/jquery-2.1.1.min.js"></script>
        <script src="${webinf}/js/jquery/jquery.widget.min.js"></script>
        <script src="${webinf}/js/jquery/jquery.mousewheel.js"></script>
        <script src="${webinf}/js/soapclient.js"></script>
        <script src="${webinf}/js/config.js" type="text/javascript"></script>
        <script type="text/javascript">
            initParam('<%=ashost%>', '<%=grhost%>');
        </script>
        <script src="${webinf}/js/kpp.js"></script>
    </head>
    <body class="skud">
        <div id="p0" class="first-photo br-white">
            <div class="container">
                <div id="p2" class="image">
                    <!--img src="empty.png" -->
                </div>
            </div>	
            <div id="p1" class="content"></div>
            <div id="p3" class="brand"></div>
            
        </div>
        <script>
            document.body.style.background = '#' + getParameterByName('bg');
        </script>
    </body>
</html>
