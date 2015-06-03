/* global grchphotopath */

var place = getParameterByName('place');
var maxresult = getParameterByName('count');
var persons = ['', '', '', 0, '', 'none', '']; //name: '', card: '', time: '', calcTime: 0,	photo:'', alarm: 'none', alarmDesc: ''
var oldpersons = ['', '', '', 0, '', 'none', ''];
var timeUpdate = 2000000; //clear photo after this time in milliseconds // 30 seconds like in Stalt
var changed = false;

$(document).ready(function () {
    var idleInterval = setInterval(loadXMLDoc, 1500); // 2 seconds for updating
});
function refreshAt(hours, minutes, seconds) {
    var now = new Date();
    var then = new Date();

    if (now.getHours() > hours ||
            (now.getHours() == hours && now.getMinutes() > minutes) ||
            now.getHours() == hours && now.getMinutes() == minutes && now.getSeconds() >= seconds) {
        then.setDate(now.getDate() + 1);
    }
    then.setHours(hours);
    then.setMinutes(minutes);
    then.setSeconds(seconds);

    var timeout = (then.getTime() - now.getTime());
    setTimeout(function () {
        window.location.reload(true);
    }, timeout);
}
refreshAt(03, 00, 00);
function doesFileExist(urlToFile) {
    var xhr = new XMLHttpRequest();
    xhr.open('HEAD', urlToFile, true);
    xhr.send('');
    if (xhr.status == "404") {
        return false;
    } else {
        return true;
    }
}

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
            results = regex.exec(location.search);
    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}


function loadXMLDoc() {
    var sourceids;
    switch (place){
        case 'spk':{
            sourceids = '00001174,0000118C,00001180,00001198';
            break;
        }
        case 'ckppM1':{
            sourceids = '000010F6,00001102';
            break;
        }
        case 'ckppM2':{
            sourceids = '0000110E,0000111A,0000481C';
            break;
        }
        default:
            sourceids = place;
    }
    
    var pl = new SOAPClientParameters();
    pl.add('SourceIDs', sourceids);
    pl.add('MaxResult', maxresult);
    SOAPClient.invoke(wsurl, 'getAccessEvents', pl, true, getAccessEvents_callback);

}

function getAccessEvents_callback(aec_obj) {
    var aec = aec_obj.ApacseventsCha;
    for (j = 0; j < persons.length; j++) {
        oldpersons[j] = persons[j];
    }
    var nowtime = new Date().getTime();
    if (aec == null || aec == undefined)
        return;
    var alarmRE = new RegExp("ErrHolder|Denied");
    persons[0] = (aec.holdername == null || aec.holdername == undefined) ? 'НЕИЗВЕСТНЫЙ' : aec.holdername;;
    persons[1] = aec.cardno;
    persons[4] = (aec.holderid == undefined || aec.holderid == null) ? 'config/image/empty.png' : grchphotopath + aec.holderid + '.jpg';
    persons[5] = alarmRE.test(aec.eventtype);
    if (persons[5]) {
        var descarr = aec.eventtypedesc.split(', ');
        var descFirst = descarr[1];
        var eventdesc = descFirst[0].toUpperCase();
        eventdesc += descFirst.substring(1, descFirst.length);
        if (descarr.lenght > 2){
            for (var i = 2; i < descarr.length; i++){
                eventdesc += ', ' + descarr[i];
            }
        }
        persons[6] = eventdesc;
    }
    persons[3] = new Date(aec.eventtime);
    persons[2] = ('0' + persons[3].getHours()).slice(-2) + ':' + ('0' + persons[3].getMinutes()).slice(-2);
    // check is an old photo
    if (Math.abs((persons[3] - nowtime)) > timeUpdate)
        persons = ['', '', '', 0, 'config/image/empty.png', 'none', ''];
    // check if updated
    var updated = false;
    for (j = 0; j < persons.length; j++) {
        if (oldpersons[j] != persons[j]) {
            updated = true;
            break;
        }
    }
    if (updated)
        updatePhotos();
}

function updatePhotos() {
    // update big photo
    var padding = ' padding-none';
    if (persons[0].length > 0)
        padding = ' padding-big';
    document.getElementById('p1').innerHTML = '<span>' + persons[0] + '</span>';
    if (persons[5]) {
        document.getElementById('p0').className = 'first-photo br-darkRed';
        document.getElementById('p1').className = 'content bg-darkRed opacity fg-white' + padding;
        document.getElementById('p3').className = 'brand bg-darkRed fg-white';
        document.getElementById('p1').innerHTML += '<br><span>' + persons[6] + '</span>';
    } else {
        if (persons[5] == 'warn') {
            document.getElementById('p0').className = 'first-photo br-orange';
            document.getElementById('p1').className = 'content bg-dark opacity fg-white' + padding;
            document.getElementById('p3').className = 'brand bg-orange fg-white';
        } else {
            document.getElementById('p0').className = 'first-photo br-white';
            document.getElementById('p1').className = 'content bg-white fg-black' + padding;
            document.getElementById('p3').className = 'brand';
        }
    }
    var check = persons[4].length > 0;
    var new_photofile = persons[4] + '?id=' + new Date().getTime(); // because of cashing
    if (check)
        check = doesFileExist(new_photofile);
    if (check)
        document.getElementById('p2').style.background = 'url(' + new_photofile + ')';
    else
        document.getElementById('p2').style.background = 'url(' + 'config/image/empty.jpg?id=' + new Date().getTime() + ')'; // because of cashing
    document.getElementById('p2').style.backgroundRepeat = 'no-repeat';
    document.getElementById('p2').style.backgroundSize = '100% 100%';
    document.getElementById('p3').innerHTML = '<span>' + persons[2] + '</span>';
}