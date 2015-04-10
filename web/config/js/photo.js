/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var xmlfile = "data/source.xml";

var persons = [
        ['', '', '', 0,	'', 'none'], //name: '', card: '', time: '', calcTime: 0,	photo:'', alarm: 'none'
        ['', '', '', 0,	'', 'none'],
        ['', '', '', 0,	'', 'none']
];

$(document).ready(function () {
        var idleInterval = setInterval(loadXMLDoc, 2000); // 2 seconds for updating
});	

function getParameterByName(name) {
        name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
        var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
        return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

function loadXMLDoc() {
        var pathArray = window.location.pathname.split( '/' );
        var new_xmlfile = '/';
        for (i = 0; i < pathArray.length - 1; i++) new_xmlfile += pathArray[i];
        var new_xmlfile = 'http://'+window.location.host + new_xmlfile + '/' + xmlfile + '?v='+getParameterByName('v') + '&time_stamp=' + new Date().getTime(); // because of cashing
        xhttp = new XMLHttpRequest();
        xhttp.open("GET", new_xmlfile, false);
        xhttp.onreadystatechange = function ()	{
                if (xhttp.readyState === 4) {
                        updatePersons(xhttp.responseXML);
                }
        };
        xhttp.send("");	//source file encoding should be in ansi for ie
}

function updatePersons(xmlDoc) {
        // update an array
        var x = xmlDoc.getElementsByTagName('point')[0].getElementsByTagName('visitor');
        var k = persons.length-1;
        for (i = 0; i < x.length; i++) {
                persons[k][0] = x[i].attributes.getNamedItem('name').value;
                persons[k][1] = x[i].attributes.getNamedItem('card').value;
                persons[k][4] = x[i].attributes.getNamedItem('img').value;
                persons[k][5] = x[i].attributes.getNamedItem('alarm').value;
                persons[k][3] = 0;
                // find time as an integer
                var timearr = x[i].attributes.getNamedItem('time').value.split(":");
                persons[k][3] = new Date(timearr[0],timearr[1],timearr[2],timearr[3],timearr[4],timearr[5]).getTime();
                persons[k][2] = timearr[3]+':'+timearr[4];
                k--;
                if (k<0) break; // not more than three persons
        }
        // sort by datetime
        persons.sort(function(a, b){return a[3]-b[3];});
        //
        updatePhotos();
}

function updatePhotos() {
        // update big photo
        if (persons[0][5] === 'alarm') {
                document.getElementById('p0').className = 'first-photo br-darkRed';
                document.getElementById('p1').className = 'content bg-darkRed opacity fg-white';
                document.getElementById('p3').className = 'brand bg-darkRed fg-white';
        }else {
                if (persons[0][5] === 'warn') {
                        document.getElementById('p0').className = 'first-photo br-orange';
                        document.getElementById('p1').className = 'content bg-dark opacity fg-white';
                        document.getElementById('p3').className = 'brand bg-orange fg-white';
                }else{
                        document.getElementById('p0').className = 'first-photo br-white';
                        document.getElementById('p1').className = 'content bg-dark opacity fg-white';
                        document.getElementById('p3').className = 'brand bg-green fg-white';
                }
        }	
        document.getElementById('p1').innerHTML='<span>'+persons[0][0]+'</span>';
        document.getElementById('p2').innerHTML='<img src="photo/'+persons[0][4]+'">';
        document.getElementById('p3').innerHTML='<span>'+persons[0][2]+'</span>';
        // update next photos
        for (i=1; i<3 ; i++) {
                if (persons[i][5] === 'alarm') {
                        document.getElementById('d0'+i).className = 'second-photo br-darkRed';
                        document.getElementById('d1'+i).className = 'content bg-darkRed opacity fg-white';
                }else {
                        if (persons[i][5] === 'warn') {
                                document.getElementById('d0'+i).className = 'second-photo br-orange';
                                document.getElementById('d1'+i).className = 'content bg-dark opacity fg-white';
                        }else{
                                document.getElementById('d0'+i).className = 'second-photo br-white';
                                document.getElementById('d1'+i).className = 'content bg-dark opacity fg-white';
                        }
                }	
                document.getElementById('d1'+i).innerHTML='<span>'+persons[i][0]+'</span>';
                document.getElementById('d2'+i).innerHTML='<img src="photo/'+persons[i][4]+'">';
                document.getElementById('d3'+i).innerHTML='<span>'+persons[i][2]+'</span>';
        }
}

