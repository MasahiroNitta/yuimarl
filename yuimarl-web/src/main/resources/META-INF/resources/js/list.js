function setLineNo(line) {
    $("#lineNo").val(line);
}

function setDlgPartyNo(line) {
    $("#dlgPartyNo").val(line);
    $("#partyJson").val("");
}

function relationDelConf(line, relNo) {
    $("#lineNo").val(line);
    $("#conf").val("0");
    if (confirm("関連No." + relNo + "を削除します。よろしいですか？")) {
        $("#conf").val("1");
    }
}

function userDelConf(line, name) {
    $("#lineNo").val(line);
    $("#conf").val("0");
    if (confirm(name + "を削除します。よろしいですか？")) {
        $("#conf").val("1");
    }
}

function escapeHTML(str) {
    return str.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '&#39;');
};

function showPartyDialog(event) {
    var val = $("#partyJson").val();
    if (val === "" || event.status !== "success") {
        return;
    }
    var obj = JSON.parse(val);
    $(".ui-dialog-title").text(obj.name.value);
    var tag = "<table>";
    tag += "<tr><td>" + obj.partyNo.label + "</td><td>&nbsp;&nbsp;</td><td>" + obj.partyNo.value + "</td></tr>";
    if (obj.partyType.value === "1") {
        tag += "<tr><td>" + obj.lastName.label + "</td><td></td><td>" + escapeHTML(obj.lastName.value) + "</td></tr>";
        tag += "<tr><td>" + obj.firstName.label + "</td><td></td><td>" + escapeHTML(obj.firstName.value) + "</td></tr>";
        tag += "<tr><td>" + obj.lastNameKana.label + "</td><td></td><td>" + escapeHTML(obj.lastNameKana.value) + "</td></tr>";
        tag += "<tr><td>" + obj.firstNameKana.label + "</td><td></td><td>" + escapeHTML(obj.firstNameKana.value) + "</td></tr>";
        tag += "<tr><td>" + obj.gender.label + "</td><td></td><td>" + obj.gender.value + "</td></tr>";
    } else {
        tag += "<tr><td>" + obj.name.label + "</td><td></td><td>" + escapeHTML(obj.name.value) + "</td></tr>";
        tag += "<tr><td>" + obj.nameKana.label + "</td><td></td><td>" + escapeHTML(obj.nameKana.value) + "</td></tr>";
    }
    tag += "<tr><td>" + obj.zipCode.label + "</td><td></td><td>" + escapeHTML(obj.zipCode.value) + "</td></tr>";
    tag += "<tr><td>" + obj.prefecture.label + "</td><td></td><td>" + obj.prefecture.value + "</td></tr>";
    tag += "<tr><td>" + obj.city.label + "</td><td></td><td>" + escapeHTML(obj.city.value) + "</td></tr>";
    tag += "<tr><td>" + obj.address1.label + "</td><td></td><td>" + escapeHTML(obj.address1.value) + "</td></tr>";
    tag += "<tr><td>" + obj.address2.label + "</td><td></td><td>" + escapeHTML(obj.address2.value) + "</td></tr>";
    tag += "<tr><td>" + obj.phoneNo.label + "</td><td></td><td>" + escapeHTML(obj.phoneNo.value) + "</td></tr>";
    if (obj.partyType.value === "1") {
        tag += "<tr><td>" + obj.cellPhoneNumber.label + "</td><td></td><td>" + escapeHTML(obj.cellPhoneNumber.value) + "</td></tr>";
    }
    tag += "<tr><td>" + obj.faxNo.label + "</td><td></td><td>" + escapeHTML(obj.faxNo.value) + "</td></tr>";
    tag += "<tr><td>" + obj.mailAddress1.label + "</td><td></td><td>" + escapeHTML(obj.mailAddress1.value) + "</td></tr>";
    tag += "<tr><td>" + obj.mailAddress2.label + "</td><td></td><td>" + escapeHTML(obj.mailAddress2.value) + "</td></tr>";
    if (obj.partyType.value === "1") {
        tag += "<tr><td>" + obj.birthDay.label + "</td><td></td><td>" + obj.birthDay.value + "</td></tr>";
        tag += "<tr><td>" + obj.dateOfDeath.label + "</td><td></td><td>" + obj.dateOfDeath.value + "</td></tr>";
    }
    if (obj.partyType.value === "2" || obj.partyType.value === "3") {
        if (obj.partyType.value === "3") {
            tag += "<tr><td>" + obj.url.label + "</td><td></td><td>" + escapeHTML(obj.url.value) + "</td></tr>";
        }
        tag += "<tr><td>" + obj.organizationCategory.label + "</td><td></td><td>" + obj.organizationCategory.value + "</td></tr>";
        tag += "<tr><td>" + obj.personCount.label + "</td><td></td><td>" + obj.personCount.value + "</td></tr>";
        tag += "<tr><td>" + obj.establishDate.label + "</td><td></td><td>" + obj.establishDate.value + "</td></tr>";
        tag += "<tr><td>" + obj.abolitionDate.label + "</td><td></td><td>" + obj.abolitionDate.value + "</td></tr>";
        if (obj.partyType.value === "3") {
            tag += "<tr><td>" + obj.capital.label + "</td><td></td><td>" + obj.capital.value + "</td></tr>";
            tag += "<tr><td>" + obj.accountMonth.label + "</td><td></td><td>" + obj.accountMonth.value + "</td></tr>";
        }
    }
    if (obj.partyType.value === "4") {
        tag += "<tr><td>" + obj.category.label + "</td><td></td><td>" + obj.category.value + "</td></tr>";
        tag += "<tr><td>" + obj.startDate.label + "</td><td></td><td>" + obj.startDate.value + "</td></tr>";
        tag += "<tr><td>" + obj.endDate.label + "</td><td></td><td>" + obj.endDate.value + "</td></tr>";
    }
    tag += "</table>";
    $("#partyViewDialog").html(tag);
    $("#partyViewDialog").dialog("open");
}

$(function() {
    $("#partyViewDialog").dialog({
        autoOpen: false,
        width: 500,
        height: 600,
        buttons: [
            {
                text: "Ok",
                click: function() {
                    $(this).dialog("close");
                }
            }
        ]
    });
});
