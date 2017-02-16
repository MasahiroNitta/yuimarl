var clipboardNode = null;
var pasteMode = null;

function copyPaste(action, node) {
    switch (action) {
        case "cut":
        case "copy":
            clipboardNode = node;
            pasteMode = action;
            break;
        case "paste":
            if (!clipboardNode) {
                //alert("Clipoard is empty.");
                break;
            }
            if (pasteMode === "cut") {
                // Cut mode: check for recursion and remove source
                var isRecursive = false;
                var cb = clipboardNode.toDict(true, function(dict) {
                    // If one of the source nodes is the target, we must not move
                    if (dict.key === node.data.key)
                        isRecursive = true;
                });
                if (isRecursive) {
                    //alert("Cannot move a node to a sub node.");
                    return;
                }
                node.addChild(cb);
                clipboardNode.remove();
            } else {
                // Copy mode: prevent duplicate keys:
                var cb = clipboardNode.toDict(true, function(dict) {
                    dict.title = "Copy of " + dict.title;
                    delete dict.key; // Remove key, so a new one will be created
                });
                node.addChild(cb);
            }
            clipboardNode = pasteMode = null;
            break;
        default:
            //alert("Unhandled clipboard action '" + action + "'");
    }
}

// ツリーを右クリックされた時に表示するコンテキストメニューの動作を定義
function bindContextMenu(span) {
    $(span).contextMenu({menu: "myMenu"}, function(action, el, pos) {
        var node = $.ui.dynatree.getNode(el);
        switch (action) {
            case "cut":
            case "copy":
            case "paste":
                copyPaste(action, node);
                break;
            case "add":
                if (node) {
                    $("#mode").val("A");
                    $("#name").val("");
                    $("#nameKana").val("");
                    $("#categoryNo").val("0");
                    $("#dialog").dialog("open");
                }
                break;
            case "top":
                if (node) {
                    $("#mode").val("T");
                    $("#name").val("");
                    $("#nameKana").val("");
                    $("#categoryNo").val("0");
                    $("#dialog").dialog("open");
                }
                break;
            case "edit":
                if (node) {
                    $("#mode").val("E");
                    $("#name").val(node.data.title);
                    // ノードのキーには、カテゴリーNo.と名前よみがカンマ区切りで格納されているので、分解する。
                    var key = node.data.key;
                    var idx = key.indexOf(',');
                    $("#nameKana").val(key.substring(idx + 1));
                    $("#categoryNo").val(key.substring(0, idx));
                    $("#dialog").dialog("open");
                }
                break;
            case "delete":
                if (node) {
                    node.remove();
                }
                break;
            case "close":
                break;
            default:
                alert("Todo: appply action '" + action + "' to node " + node);
        }
    });
}

// 保存ボタン押下時の動作
function onSave() {
    // ツリーの中身をJSONに変換して、#jsonに格納する。
    var obj = $("#tree").dynatree("getRoot").toDict(true);
    var tx = JSON.stringify(obj);
    $("#json").val(tx);
}

$(function() {
    //console.log('tree=' + $("#tree").html());
    // dynatreeを使用して、#treeにセットされたタグから、ツリーを生成する。
    $("#tree").dynatree({
        clickFolderMode: 3,
        onCreate: function(node, span) {
            bindContextMenu(span);
        }
    });

    // コンテキストメニューで追加または編集を選択された時に表示されるダイアログの定義
    $("#dialog").dialog({
        autoOpen: false,
        width: 400,
        height: 200,
        buttons: [
            {
                // OKボタン
                text: "Ok",
                click: function() {
                    if ($("#name").val().length === 0) {
                        alert("名前を入力してください。");
                    } else if ($("#nameKana").val().length === 0) {
                        alert("名前よみを入力してください。");
                    } else {
                        var node = $("#tree").dynatree("getActiveNode");
                        if (node) {
                            switch($("#mode").val()) {
                                case "A": // add
                                    // 新規のノードは、カテゴリーNo.を0とする。
                                    node.addChild({
                                        title: $("#name").val(),
                                        key: "0," + $("#nameKana").val(),
                                        icon: false,
                                        expand: true });
                                    break;
                                case "T": // top
                                    node = $("#tree").dynatree("getRoot");
                                    // 新規のノードは、カテゴリーNo.を0とする。
                                    node.addChild({
                                        title: $("#name").val(),
                                        key: "0," + $("#nameKana").val(),
                                        icon: false,
                                        expand: true });
                                    break;
                                case "E": // edit
                                    node.data.title = $("#name").val();
                                    // ノードのキーには、カテゴリーNo.と名前よみをカンマ区切りで格納する。
                                    node.data.key = $("#categoryNo").val() + "," + $("#nameKana").val();
                                    break;
                            }
                            node.render();
                        }
                        $(this).dialog("close");
                    }
                }
            },
            {
                // Cancelボタン
                text: "Cancel",
                click: function() {
                    $(this).dialog("close");
                }
            }
        ]
    });
});


