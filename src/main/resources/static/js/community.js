
/**
 * 回复功能
 */
function post(e) {
    var url = e.getAttribute("data-url");
    var question_id=$("#questionDTO_id").val();
    var content = $("#comment_content").val();
    comment2target(question_id, 1, content, url);
}

function comment2target(targetId, type, content, url) {
    if (!content){
        alert("回复内容不能为空~~~");
        return;
    }
    $.ajax({
        type:"post",
        url:"/comment",
        contentType:"application/json",
        data: JSON.stringify({
            "parent_id":targetId,
            "content":content,
            "type":type
        }),
        success: function (response) {
            if (response.code==200){
                location.replace(location.href);
            }else {
                if (response.code == 2003) {
                    var isAccepted =confirm(response.message);
                    if (isAccepted){
                        window.open(url);
                        window.localStorage.setItem("closable","true");
                    }
                } else {
                    alert(response.message);
                }
            }
            console.log(response)
        },
        dataType:"json"
    });
}

function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-"+commentId).val()
    comment2target(commentId,2,content)
}

/**
 * 二级评论
 */
function  collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-"+id);

    //获取一下继而评论开展状态
    var collapse = e.getAttribute("data-collapse");

    //判断评论展开状态
    if (collapse) {
        //已展开，需折叠
        comments.removeClass("in");
        //标记二级评论为折叠
        e.removeAttribute("data-collapse");
        //取消高亮
        e.classList.remove("active");

    } else {
        var subCommentContainer=$("#comment-"+id);
        if (subCommentContainer.children().length != 1){
            //已折叠，需展开
            comments.addClass("in");
            //标记二级评论为展开
            e.setAttribute("data-collapse", "in");
            //高亮
            e.classList.add("active");
        }else{
            $.getJSON("/comment/" + id, function (data) {

                $.each(data.data.reverse(),function (index,comment) {
                    var mediaLeftElement=$("<div/>",{
                        "class":"media-left"
                    }).append($("<img/>",{
                        "class":"img-rounded logo",
                        "src":comment.user.avatar_url
                    }));

                    var mediaBodyElement=$("<div/>",{
                        "class":"media-body"
                    }).append($("<h5>",{
                        "class":"media-heading",
                        "html":comment.user.account_name
                    })).append($("<div/>",{
                        "html":comment.content
                    })).append($("<div/>",{
                        "class":"menu"
                    }).append($("<span/>",{
                        "class":"pull-right",
                        "html":moment(comment.gmt_create).format("YYYY-MM-DD")
                    })));

                    var mediaElement=$("<div/>",{
                        "class":"media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
                    }).append(mediaElement);
                    subCommentContainer.prepend(commentElement);
                });
                //已折叠，需展开
                comments.addClass("in");
                //标记二级评论为展开
                e.setAttribute("data-collapse", "in");
                //高亮
                e.classList.add("active");


            })
        }
    }
}