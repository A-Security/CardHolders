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
    </head>
    <body class="skud" style="padding-bottom:0px;">
        <div>
            <div class="top">
                <div class="topb">
                    <form onsubmit="window.parent.bottomFrame.findString(document.getElementById('searchtext').value);
                            return false;">
                        <input id="searchtext" type="text" size="50" >
                        <input type="submit" value="Поиск">
                    </form>
                </div>
                <div style="margin: 10px; top: 0px; right: 0px; color: white; float: right; position: absolute;" id="chCount">Сотрудников на станции:</div>
            </div>
            <div class="datagrid">
                <table>
                    <thead>
                        <tr>
                            <th width="330px">Фамилия Имя Отчество</th>
                            <th width="110px">Территория</th>
                            <th width="55px">СПК</th>
                            <th width="90px">МАШЗАЛ</th>
                            <th width="55px">ГЩУ</th>
                            <th width="60px">КРУЭ</th>
                            <th width="155px">ВОДОПРИЕМНИК</th>
                            <th width="200px">Компания</th>
                            <th width="*">Должность</th>
                        </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>