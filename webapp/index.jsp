<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link href="https://cdn.bootcss.com/font-awesome/5.11.2/css/fontawesome.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://cdn.bootcss.com/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>

    <link rel="stylesheet" href="./css/silkworm.css"/>
    <script src="./js/silkworm.js"></script>
</head>
<body>
    <center>
    <div id="stage" class="container">
        <div class="row">
            <div class="col">
                <div id="chatList" class="container" style="">

                </div>
            </div>
        </div>
        <div class="row" id="bottomContainer">
            <div class="col">
                <input id="question" type="text" class="form-control" placeholder="关于蚕的问题，尽管问我 ：）" aria-label="关于蚕的问题，尽管问我 ：）" aria-describedby="basic-addon1">
            </div>
            <div class="col-2">
                <button id="sendButton" type="button" class="btn btn-success">发送</button>
            </div>
        </div>
    </div>
    </center>
</body>
</html>