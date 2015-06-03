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
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="${webinf}/css/metro-bootstrap.css" rel="stylesheet">
        <link href="${webinf}/css/position.css" rel="stylesheet">
        <!-- Load JavaScript Libraries -->
        <script src="${webinf}/js/jquery/jquery-2.1.1.min.js"></script>
        <script src="${webinf}/js/soapclient.js"></script>
        <script src="${webinf}/js/config.js" type="text/javascript"></script>
        <script type="text/javascript">
            initParam('<%=ashost%>', '<%=grhost%>');
        </script>
        <script src="${webinf}/js/position.js"></script>
    </head>
    <body class="skud" style="padding-top:0px;">
        <div>
            <div class="datagrid">
                <table>
                    <thead>
                        <tr>
                            <th width="330px"></th>
                            <th width="110px"></th>
                            <th width="55px"></th>
                            <th width="90px"></th>
                            <th width="55px"></th>
                            <th width="60px"></th>
                            <th width="155px"></th>
                            <th width="200px"></th>
                            <th width="*"></th>
                        </tr>
                    </thead>
                    <tfoot>
                        <tr>
                            <td colspan="9">
                                <div id="no-paging">
                                    <b>ЗАГРУЗКА...</b>
                                </div>
                            </td>
                        </tr>
                    </tfoot>
                    <tbody id="persongrid">
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>