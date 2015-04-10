function addVip(){
    transOptToSel(document.main.notvipch, document.main.vipch);
}

function remVip() {
    transOptToSel(document.main.vipch, document.main.notvipch);
}

function transOptToSel(fromSelect, toSelect) {
	for (var i = 0; i < fromSelect.options.length; i++) {
        var opt = fromSelect.options[i];
        if (opt.selected) {
            opt.selected = false;
            toSelect.appendChild(opt);
        }
    }
}