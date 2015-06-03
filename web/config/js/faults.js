//nothing
function descDivById() {
    var mylist = $('#maindiv');
    var listitems = mylist.children('div').get();
    listitems.sort(function (a, b) {
        var compA = $(a).attr('id');
        var compB = $(b).attr('id');
        return (compA > compB) ? -1 : (compA < compB) ? 1 : 0;
    })
    $.each(listitems, function (idx, itm) {
        mylist.append(itm);
    });
}