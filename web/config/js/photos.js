var persons = [
    ['', '', '', 0, '', 'none', 'none'], //name: '', card: '', time: '', calcTime: 0, photo:'', alarm: 'none', vip: 'none' ('true' if it is a vip)
    ['', '', '', 0, '', 'none', 'none'],
    ['', '', '', 0, '', 'none', 'none']
];

var oldpersons = [
    ['', '', '', 0, '', 'none', 'none'],
    ['', '', '', 0, '', 'none', 'none'],
    ['', '', '', 0, '', 'none', 'none']
];
var place = getParameterByName('place');
var maxresult = getParameterByName('count');
var showvip = getParameterByName('show');

var timeUpdate = 360000000000; //clear photo after this time in miliseconds // 1 hour
var timeSound = 200000; // time for playing sound
var changed = false;
var vip_sound = 'config/sounds/234564.wav';
var alarm_sound = 'config/sounds/alarm4.wav';

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
            results = regex.exec(location.search);
    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

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
    xhr.open('HEAD', urlToFile, false);
    xhr.send();
    if (xhr.status == "404") {
        return false;
    } else {
        return true;
    }
}

$(document).ready(function () {
    //if (xmlfile.length==0) return;
    var idleInterval = setInterval(loadXMLDoc, 2000); // 2 seconds for updating
});

// ЗДЕСЬ НУЖНО ЗАМЕНИТЬ ВМЕСТО ЗАПРОСА ИЗ ФАЙЛА ЗАПРОС НА WEB-СЕРВИС!
function loadXMLDoc() {
    var sourceids;
    switch (place){
        case 'spkIn':{
            sourceids = '00001174,0000118C';
            break;
        }
        case 'spkOut':{
            sourceids = '00001180,00001198';
            break;
        }
        case 'ckppIn':{
            sourceids = '000010F6,0000110E';
            break;
        }
        case 'ckppOut':{
            sourceids = '00001102,0000111A,0000481C';
            break;
        }
        case 'ckppAutoIn':{
            sourceids = '00004932';
            break;
        }
        case 'ckppAutoOut':{
            sourceids = '0000493E';
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

function getAccessEvents_callback(aec_list) {
    for (i = 0; i < persons.length; i++) {
        for (j = 0; j < persons[i].length; j++) {
            oldpersons[i][j] = persons[i][j];
        }
    }
    //
    var nowtime = new Date().getTime();

    var k = persons.length - 1;
    for (var i = 0; i < aec_list.length; i++) {
        var aec = aec_list[i];
        var photoVipVal = getPhotoVip(aec.holderid);
        var alarmRE = new RegExp("ErrHolder|Denied");
        persons[k][0] = (aec.holdershortname == null || aec.holdershortname == undefined) ? 'НЕИЗВЕСТНЫЙ' : aec.holdershortname;
        persons[k][1] = aec.cardno;
        persons[k][4] = photoVipVal == undefined ? 'empty.png' : photoVipVal.photo;
        persons[k][5] = alarmRE.test(aec.eventtype);
        persons[k][6] = photoVipVal == undefined || showvip == 'true' ? photoVipVal.vip : 'none';
        persons[k][3] = new Date(aec.eventtime);
        persons[k][2] = ('0' + persons[k][3].getHours()).slice(-2) + ':' + ('0' + persons[k][3].getMinutes()).slice(-2);
        //persons[k][3] = new Date(tarr[0],tarr[1]-1,tarr[2],tarr[3],tarr[4],tarr[5]);
        //persons[k][2] = tarr[3]+':'+tarr[4];
        //
        k--;
    }
    // sort by datetime
    persons.sort(function (a, b) {
        return b[3] - a[3]
    });

    if (persons[0][3] != 0)
        if (Math.abs((persons[0][3].getTime() - nowtime)) > timeUpdate) {
            persons = [
                ['', '', '', 0, '', 'none', 'none'],
                ['', '', '', 0, '', 'none', 'none'],
                ['', '', '', 0, '', 'none']
            ];
        }

    // check if photos was updated
    for (i = 0; i < persons.length; i++) {
        for (j = 0; j < persons[i].length; j++) {
            if (j == 3)
                continue; //some problem with two time strings difference
            if (oldpersons[i][j] != persons[i][j]) {
                changed = true;
                break;
            }
        }
    }
    // update photos at the screen
    if (changed) {
        updatePhotos();
        changed = false;
    }
}
function getPhotoVip(holderid) {
    if (holderid == undefined || holderid == null) {
        return;
    }
    var result = {
        photo: '',
        vip: ''
    };
    var holderPath = grholderspath + holderid + '.xml?time_stamp=' + new Date().getTime();
    xhttp = new XMLHttpRequest();
    xhttp.open("GET", holderPath, false);
    xhttp.onreadystatechange = function () {
        if (xhttp.readyState == 4) {
            try {
                var x = xhttp.responseXML.getElementsByTagName('metadata')[0].getElementsByTagName('holder')[0];
            } catch (e) {
                return;
            }
            result.photo = x.getElementsByTagName('photoLink')[0].textContent;
            result.vip = x.getElementsByTagName('vip')[0].textContent;
        }
    }
    try {
        xhttp.send("");
    } catch (e) {
    }
    return result;
}

function updatePhotos() {
    var padding = ' padding-none';
    var new_photofile = '';
    // update big photo
    if (persons[0][0].length > 0)
        padding = ' padding-big';
    if (persons[0][5]) {
        document.getElementById('p0').className = 'first-photo br-darkRed';
        document.getElementById('p1').className = 'content bg-darkRed opacity fg-white' + padding;
        document.getElementById('p3').className = 'brand bg-darkRed fg-white';
        document.getElementById('playaudio').innerHTML = '<embed src="' + alarm_sound + '" hidden="true" autostart="true" loop="false">';

    } else {
        if (persons[0][5] == 'warn') {
            document.getElementById('p0').className = 'first-photo br-orange';
            document.getElementById('p1').className = 'content bg-dark opacity fg-white' + padding;
            document.getElementById('p3').className = 'brand bg-orange fg-white';
        } else {
            document.getElementById('p0').className = 'first-photo br-white';
            document.getElementById('p1').className = 'content bg-dark opacity fg-white' + padding;
            document.getElementById('p3').className = 'brand';
        }
    }
    document.getElementById('p1').innerHTML = '<span>' + persons[0][0] + '</span>';
    var check = persons[0][4].length > 0;
    if (check) {
        new_photofile = persons[0][4] + '?id=' + new Date().getTime(); // because of cashing
        check = doesFileExist(new_photofile);
    }
    if (!check) {
        if (persons[0][0].length > 0)
            new_photofile = 'config/image/empty.png?id=' + new Date().getTime();
        else
            new_photofile = 'config/image/empty.png';
    }
    // replace image with transition
    var img = document.getElementById('p2');
    var style = img.currentStyle || window.getComputedStyle(img, false);
    document.getElementById('p2').style.transition = 'none';
    document.getElementById('p2').style.backgroundPosition = '0px -300px, 0px 0px';
    document.getElementById('p2').style.background = 'url(' + new_photofile + '), ' + style.backgroundImage.split(',')[0];
    document.getElementById('p2').style.backgroundRepeat = 'no-repeat';
    document.getElementById('p2').style.backgroundSize = '100% 100%';
    document.getElementById('p2').style.transition = 'all 1s ease-in-out';
    document.getElementById('p2').style.backgroundPosition = '0px 0px, 0px 0px';
    // replace all another first person data
    if (persons[0][2].length > 0) {
        document.getElementById('p3').innerHTML = '<span>' + persons[0][2] + '</span>';
    } else {
        document.getElementById('p3').innerHTML = '';
    }
    // update next photos
    for (i = 1; i < 3; i++) {
        if (persons[i][0].length > 0)
            padding = ' padding-big';
        if (persons[i][5]) {
            document.getElementById('d0' + i).className = 'second-photo br-darkRed';
            document.getElementById('d1' + i).className = 'content bg-darkRed opacity fg-white' + padding;
        } else {
            if (persons[i][5] == 'warn') {
                document.getElementById('d0' + i).className = 'second-photo br-orange';
                document.getElementById('d1' + i).className = 'content bg-dark opacity fg-white' + padding;
            } else {
                document.getElementById('d0' + i).className = 'second-photo br-white';
                document.getElementById('d1' + i).className = 'content bg-dark opacity fg-white' + padding;
            }
        }
        document.getElementById('d1' + i).innerHTML = '<span>' + persons[i][0] + '</span>';
        check = persons[i][4].length > 0;
        if (check) {
            new_photofile = persons[i][4] + '?id=' + new Date().getTime(); // because of cashing
            check = doesFileExist(new_photofile);
        }
        if (!check) {
            if (persons[i][0].length > 0)
                new_photofile = 'config/image/empty.png?id=' + new Date().getTime();
            else
                new_photofile = 'config/image/empty.png';
        }

        document.getElementById('d2' + i).innerHTML = '<img src="' + new_photofile + '">';
        document.getElementById('d3' + i).innerHTML = '<span>' + persons[i][2] + '</span>';
    }
    // vips
    if (persons[0][3] != 0)
        if (persons[0][6] == 'true') {
            document.getElementById('playaudio').innerHTML = '<embed src="' + vip_sound + '" hidden="true" autostart="true" loop="false">';
            document.getElementById('p0').className = 'first-photo br-green';
            document.getElementById('p1').className = 'content bg-green opacity fg-white' + padding;
        }
    /*
     if (persons[0][3]!=0) {
     if (Math.abs((persons[0][3].getTime()-new Date().getTime()))<timeSound) {
     for (j=0; j<vips.length; j++)
     if (persons[0][1]==vips[j]) {
     document.getElementById('playaudio').innerHTML='<embed src="'+'config/sounds/234564.wav" hidden="true" autostart="true" loop="false">';
     document.getElementById('p0').className = 'first-photo br-green';
     document.getElementById('p1').className = 'content bg-green opacity fg-white'+padding;
     break;
     }
     }
     }*/
}
