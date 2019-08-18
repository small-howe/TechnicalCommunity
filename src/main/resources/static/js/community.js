// 获取回复问题的时候的ID 在question.html 页面
/**
 *
 * 提交回复
 */

function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    comment2target(questionId, 1, content)

}

function comment2target(targetId, type, content) {
    if (!content) {
        alert("不能回复空内容")
        return;
    }
    $.ajax({
        type: "POST",
        url:"/comment",
        contentType:'application/json',
        data:JSON.stringify({
            "parentId": targetId,
            "content":content,
            "type": type
        }),
        success:function (response) {
            if(response.code==200){
                // 刷新当前页面
                window.location.reload();
                // $("#comment_section").hide();
            }else {
                if (response.code = 2003) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=36e1699961c8659528eb&redirect_uri=http://localhost:8887/callback&scope=user&state=1")
                        window.localStorage.setItem("closable", true);
                    }
                }
            }
            console.log(response)
        },
        dataType:"json"
    })
}

function comment(e) {
    var commentId = e.getAttribute("data-id");
    console.log(commentId);
    var content = $("#input-" + commentId).val();
    console.log(content);
    comment2target(commentId, 2, content);

}


/**
 * 展开二级评论
 */
function collapseComments(e) {

    var id = e.getAttribute("data-id");
    console.log(id);
    var comments = $("#comment-" + id);
    // 获取二级评论状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse) {
        // 折叠二级评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    } else {
        var subCommentContainer = $("#comment-" + id);
        if (subCommentContainer.children().length != 1) {
            //展开二级评论
            comments.addClass("in");
            // 标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            // 高亮选中标签 加样式
            e.classList.add("active");
        } else {
            $.getJSON("/comment/" + id, function (data) {
                $.each(data.data.reverse(), function (index, comment) {
                    // 左边
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl
                    }));
                    // 右边
                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(comment.gmtCreate).format('YYYY-MM-DD')
                    })));

                    //整合
                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);
                    //整合后放在div 里面
                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });
                //展开二级评论
                comments.addClass("in");
                // 标记二级评论展开状态
                e.setAttribute("data-collapse", "in");
                // 高亮选中标签 添加样式
                e.classList.add("active");
            });
        }
    }
}

//点击标签 显示在 文本框中
function selectTag(e) {

    var value = e.getAttribute("data-tag");

    var pervious = $("#tag").val();
    if (pervious.indexOf(value) == -1) {
        if (pervious) {
            $("#tag").val(pervious + ',' + value);
        } else {
            $("#tag").val(value);
        }
    }
}


//
    function ShowSelectTag() {
        $("#ShowTag").show();
    }

