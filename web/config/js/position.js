/* global graccesscontrolpath */

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
            results = regex.exec(location.search);
    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

$(document).ready(function () {
    var idleInterval = setInterval(loadXMLDoc, 4000); // 4 seconds for updating
});

// ЗДЕСЬ НУЖНО ЗАМЕНИТЬ ВМЕСТО ЗАПРОСА ИЗ ФАЙЛА ЗАПРОС НА WEB-СЕРВИС!
function loadXMLDoc() {
    var vdprVerhOut = "000000FC";
    var vdprNizhOut = "00000114";
    var ckppOutT1 = "00001102";
    var ckppOutT2 = "0000111A";
    var ckppOutCB = "0000481C";
    var ckppAutoOut = "0000493E";
    var exitids = vdprVerhOut + ',' + vdprNizhOut + ',' + ckppOutT1
            + ',' + ckppOutT2 + ',' + ckppOutCB + ',' + ckppAutoOut;
    var pl = new SOAPClientParameters();
    pl.add('ExitIDs', exitids);
    SOAPClient.invoke(wsurl, 'getCHsPositions', pl, true, getCHsPositions_callback);
}

var TRange = null;

function findString(str) {
    var strFound;
    if (window.find) {
        // CODE FOR BROWSERS THAT SUPPORT window.find
        strFound = self.find(str);
        if (!strFound) {
            strFound = self.find(str, 0, 1);
            while (self.find(str, 0, 1))
                continue;
        }
    }
    else {
        // EXPLORER-SPECIFIC CODE
        if (TRange != null) {
            TRange.collapse(false);
            strFound = TRange.findText(str);
            if (strFound)
                TRange.select();
        }
        if (TRange == null || strFound == 0) {
            TRange = self.document.body.createTextRange();
            strFound = TRange.findText(str);
            if (strFound)
                TRange.select();
        }
    }
    return;
}
function choiceZone(id) {
    var spkInT1 = "00001174";
    var spkInT2 = "0000118C";
    var spkOutT1 = "00001180";
    var spkOutT2 = "00001198";
    var serv620In = "00004A8C";
    var serv620Out = "00004A98";
    var serv217In = "000041B4";
    var serv217Out = "000041C0";
    var serv116In = "00005C0F";
    var serv116Out = "00005C1B";
    var ckppInT1 = "000010F6";
    var ckppInT2 = "0000110E";
    var ckppAutoIn = "00004932";
    var pcoIn = "0000435B";
    var pcoOut = "00004367";
    var vdprVerhIn = "000000F0";
    var vdprNizhIn = "00000108";
    var gsuIn = "000011CD";
    var gsuOut = "000011D9";
    var krueIn = "0000599D";
    var krueOut = "000059A9";
    var krue500In = "00005A7D";
    var krue500Out = "00005A89";
    var secRoomIn = "00005991";
    var secRoomOut = "00005985";
    var mzIn = "000011E9";
    var mzOut = "000011F5";
    var result;
    switch (id)
    {
        case ckppInT1: case ckppInT2: case ckppAutoIn: case spkOutT1: 
            case spkOutT2: case mzOut: case krueOut: case pcoOut: 
                case serv217Out: case pcoIn: case serv217In:{
                    result = "";
                    break;
        }
        case spkInT1: case spkInT2: case gsuOut: 
            case serv620In: case serv620Out: {
                result = "spk";
                break;
        }
        case mzIn: {
            result = "mz";
            break;
        }
        case krueIn: case secRoomIn: case secRoomOut: case serv116In:
            case serv116Out: case krue500In: case krue500Out: {
                result = "krue";
                break;
        }
        case gsuIn: {
            result = "gsu";
            break;
        }
        case vdprVerhIn: case vdprNizhIn: {
            result = "vdpr";
            break;
        }
        default: {
            result = "";
            break;
        }
    }
    return result;
}
function getCHsPositions_callback(chp_list) { //xmlDoc
    if (chp_list == null || chp_list == undefined || chp_list.length == 0) {
        return;
    }
    var new_tbody = document.createElement('tbody');
    new_tbody.id = 'persongrid';
    var k = 0;
    for (var i = 0; i < chp_list.length; i++) {
        var chp = chp_list[i];
        new_tr = document.createElement('tr');
        if (i % 2 != 0)
            new_tr.className = 'alt';
        var holdercolor;
        switch (chp.holdercategory){
            case 'false': {
                holdercolor = pm_employee_color;
                break;
            }
            case 'true': {
                holdercolor = pm_guests_color;
                break;
            }
        }
        if (holdercolor){
            new_tr.style.color = holdercolor;
        } 
        else{
            new_tr.style.color = '#000000';
        }
        ned_td1 = document.createElement('td');
        ned_td1.innerHTML = chp.holdername;
        new_tr.appendChild(ned_td1);
        var zones = choiceZone(chp.sourceid);
        ned_td2 = document.createElement('td');
        if (zones == null)
            ned_td2.innerHTML = '<div class="imgdiv green">&nbsp;</div>';
        else
            ned_td2.innerHTML = '<div class="imgdiv grey">&nbsp;</div>';
        new_tr.appendChild(ned_td2);

        ned_td3 = document.createElement('td');
        if (zones == 'spk')
            ned_td3.innerHTML = '<div class="imgdiv green">&nbsp;</div>';
        else if (zones == 'gsu')
            ned_td3.innerHTML = '<div class="imgdiv grey">&nbsp;</div>';
        new_tr.appendChild(ned_td3);

        ned_td4 = document.createElement('td');
        if (zones == 'mz')
            ned_td4.innerHTML = '<div class="imgdiv green">&nbsp;</div>';
        new_tr.appendChild(ned_td4);

        ned_td5 = document.createElement('td');
        if (zones == 'gsu')
            ned_td5.innerHTML = '<div class="imgdiv green">&nbsp;</div>';
        new_tr.appendChild(ned_td5);

        ned_td6 = document.createElement('td');
        if (zones == 'krue')
            ned_td6.innerHTML = '<div class="imgdiv green">&nbsp;</div>';
        new_tr.appendChild(ned_td6);

        ned_td7 = document.createElement('td');
        if (zones == 'vdpr')
            ned_td7.innerHTML = '<div class="imgdiv green">&nbsp;</div>';
        new_tr.appendChild(ned_td7);

        ned_td8 = document.createElement('td');
        ned_td8.innerHTML = chp.holdercompany;
        new_tr.appendChild(ned_td8);

        ned_td9 = document.createElement('td');
        if (chp.holderoccup == null || chp.holderoccup == undefined || chp.holderoccup.length == 0) {
            ned_td9.innerHTML = chp.holderoccup;
        }
        else {
            ned_td9.innerHTML = chp.holderoccup.length > 34 ? chp.holderoccup.substring(0, 31) + '...' : chp.holderoccup;
        }
        new_tr.appendChild(ned_td9);
        new_tbody.appendChild(new_tr);
    }
    var persongrid = document.getElementById('persongrid');
    persongrid.parentNode.replaceChild(new_tbody, persongrid);
    document.getElementById('no-paging').innerHTML = 'Сотрудников на станции: <b>' + chp_list.length + '</b>';
    window.parent.topFrame.document.getElementById('chCount').innerHTML = 'Сотрудников на станции: <b>' + chp_list.length + '</b>';
}