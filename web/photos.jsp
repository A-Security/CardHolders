<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cardholders.*"%>
<%
    ChsCfg cfg = new ChsCfg();
    String grhost = cfg.getGRhost();
    String ashost = cfg.getAShost();
    pageContext.setAttribute("webinf", "http://" + grhost + ":9763/registry/resource/_system/governance/ssoi/web");
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Загорская ГАЭС</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta content="no-cache, no-store, must-revalidate" http-equiv="Cache-control">
        <meta content="no-cache" http-equiv="Pragma">
        <meta content="0" http-equiv="Expires">

        <link href="${webinf}/css/metro-bootstrap.css" rel="stylesheet">
        <link href="${webinf}/css/metro-bootstrap-responsive.css" rel="stylesheet">
        <link href="${webinf}/css/skud.css" rel="stylesheet">
        <!-- Load JavaScript Libraries -->
        <script src="${webinf}/js/jquery/jquery-2.1.1.min.js"></script>
        <script src="${webinf}/js/jquery/jquery.widget.min.js"></script>
        <script src="${webinf}/js/jquery/jquery.mousewheel.js"></script>
        <script src="${webinf}/js/soapclient.js"></script>
        <script src="${webinf}/js/config.js" type="text/javascript"></script>
        <script type="text/javascript">
            initParam('<%=ashost%>', '<%=grhost%>');
        </script>
        <script src="${webinf}/js/photos.js"></script>
    </head>
    <body class="skud">
        <div style="border: 2px #993A00 solid ; width:100%; height: 1px; position:absolute; left:0px; top: 0px;"> </div>
        <div style="border: 3px #993A00 solid ; height:100%; width: 3px; position:absolute; right:0px; top: 0px;"> </div>
        <div class="title" id="title"><div class="inner" id="innertitle">НЕИЗВЕСТНО</div></div>
        <div id="p0" class="first-photo br-white">
            <div class="container">
                <div id="p2" class="image">
                    <!--img src="config/image/empty.png" class="transimg"-->
                </div>
            </div>	
            <div id="p1" class="content"></div>
            <div id="p3" class="brand"></div>
        </div>
        <div id="d01" class="second-photo br-white">
            <div class="container">
                <div id="d21" class="image">
                    <img src="${webinf}/image/empty.png" class="transimg">
                </div>
            </div>	
            <div id="d11" class="content"></div>
            <div id="d31" class="brand"></div>
        </div>	
        <div id="d02" class="second-photo br-white" style="top:190px;">
            <div class="container">
                <div id="d22" class="image">
                    <img src="${webinf}/image/empty.png" class="transimg">
                </div>
            </div>	
            <div id="d12" class="content"></div>
            <div id="d32" class="brand"></div>
        </div>
        <div id="playaudio"></div>
        <script>
            document.getElementById('innertitle').innerHTML = getParameterByName('name').replace("_", " ");
            document.body.style.background = '#' + getParameterByName('bg');
            var shortview = getParameterByName('shortview');
            if (shortview == 'yes') {
                document.getElementById('p0').style.left = '20px';
                document.getElementById('d01').style.left = '310px';
                document.getElementById('d02').style.left = '310px';
                document.getElementById('innertitle').left = ''
            }
        </script>	
    </body>
</html>

