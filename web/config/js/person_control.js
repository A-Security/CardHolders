function transOptToSel(fromSelect, toSelect) {
    for (var i = 0; i < fromSelect.options.length; i++) {
        var opt = fromSelect.options[i];
        if (opt.selected) {
            opt.selected = false;
            toSelect.appendChild(opt);
        }
    }
}

function addVip(){
    transOptToSel(document.main.notvipch, document.main.vipch);
}

function remVip() {
    transOptToSel(document.main.vipch, document.main.notvipch);
}

function selAllOptions(selectObj){
    for (var i = 0; i < selectObj.options.length; i++) {
        selectObj.options[i].selected = true;
    }
}

function selAllSelect() {
    selAllOptions(document.main.vipch);
    selAllOptions(document.main.notvipch);
}