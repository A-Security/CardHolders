var wsurl;
var grholderspath;
var grchphotopath;
var graccesscontrolpath;
var pm_employee_color;
var pm_guests_color;
function initParam(ashost, grhost){
    wsurl = "http://" + ashost + ":9763/eventslogdb/AccessEvents";
    var grhostpath = "http://" + grhost + ":9763/";
    var grssoipath = "registry/resource/_system/governance/ssoi/";
    grholderspath = grhostpath + grssoipath + "cardholders/";
    grchphotopath = grholderspath + "photo/";
    graccesscontrolpath = grhostpath + grssoipath + "config/access_control/";
    setpm_employee_color();
    setpm_guests_color();
}
function setpm_employee_color() {
    var xhttp = new XMLHttpRequest();
    xhttp.open("GET", graccesscontrolpath + 'pm_employee_color?time_stamp=' + new Date().getTime(), true);
    xhttp.onreadystatechange = function () {
        if (xhttp.readyState == 4) {
           pm_employee_color = xhttp.responseText;
        }
    };
    try {
        xhttp.send();
    } catch (e) {
    }
}

function setpm_guests_color() {
    var xhttp = new XMLHttpRequest();
    xhttp.open("GET", graccesscontrolpath + 'pm_guests_color?time_stamp=' + new Date().getTime(), true);
    xhttp.onreadystatechange = function () {
        if (xhttp.readyState == 4) {
           pm_guests_color = xhttp.responseText;
        }
    };
    try {
        xhttp.send();
    } catch (e) {
    }
}