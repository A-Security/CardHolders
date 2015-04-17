function transOptToSel(fromSelect, toSelect, tempSelect) {
    for (var i = 0; i < fromSelect.options.length; i++) {
        var opt = fromSelect.options[i];
        if (opt.selected) {
            opt.selected = false;
            toSelect.appendChild(opt);
            if (tempSelect !== null) {
                var tmpOpt = opt.cloneNode(true);
                if (tempSelect.options.length !== 0) {
                    var flag = true;
                    for (var j = 0; j < tempSelect.options.length; j++) {
                        if (tempSelect.options[j].value === opt.value) {
                            tempSelect.remove(j);
                            flag = false;
                        }
                    }
                    if (flag) {
                        tempSelect.appendChild(tmpOpt);
                    }
                }
                else {
                    tempSelect.appendChild(tmpOpt);
                }
            }
        }
    }
}

function addVip(){
    transOptToSel(document.main.allch, document.main.vipch, null);
}

function remVip() {
    transOptToSel(document.main.vipch, document.main.allch, document.main.remvipch);
}

function selAllOptions(selectObj){
    for (var i = 0; i < selectObj.options.length; i++) {
        selectObj.options[i].selected = true;
    }
}

function selAllSelect() {
    selAllOptions(document.main.vipch);
    selAllOptions(document.main.remvipch);
}
