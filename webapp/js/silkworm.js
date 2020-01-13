$(document).ready(function() {
    init();
});

function init() {
    $("#sendButton").click(function(e){
        var query = $("#question").val();
        showQuestion(query)
        $.getJSON( "./search?question=" + query, function(data) {
            showAnswer(data);
        });
    });
}

function showQuestion(question) {
    $("#chatList").append("<div class='row'><div class='col my-words'>" + question + "</div></div>");
}

function showAnswer(data) {
    var c = $("#chatList");
    if (data.result.length > 0) {
        var r = data.result[0];
        c.append("<div class='row'><div class='col bot-answer'>" + r.answer + "</div></div>");
    }
}