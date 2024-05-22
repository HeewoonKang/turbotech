$(document).on('click', '#btn-insert', function() {
    if(title && content) {
        onSavePost()
    } else {
        alert('제목과 내용을 모두 입력해주세요.');
    }
});
$(document).on('click', '#btn-update', function() {
    const title = $('#title').text();
    const content = document.getElementById('content').value;

    if(title && content) {
        onUpdatePost()
    } else {
        alert('제목과 내용을 모두 입력해주세요.');
    }
});

function onUpdatePost() {
    oEditors.getById["content"].exec("UPDATE_CONTENTS_FIELD", [])
    const id = $('#id').val();
    const accessToken = localStorage.getItem('accessToken');
    const title = $('#title').text();
    /*const content = $('#content').text();*/
    const content = document.getElementById('content').value;

    $.ajax({
        type: 'PUT',
        url: '/api/post/update/' + id,
        contentType: 'application/json',
        data: JSON.stringify({
            title : title,
            content: content
        }),
        headers: {
            'Authorization': 'Bearer ' + accessToken
        },
        success: function(response) {
            console.log('성공적으로 게시글을 수정했습니다:', response);
            window.location.href = "/post/" + id;
        },
        error: function(error) {
            if (error.status === 403) {
                var refreshToken = localStorage.getItem('refreshToken');

                $.ajax({
                    type: 'POST',
                    url: '/auth/refreshToken',
                    contentType: 'application/json',
                    headers: { refreshToken: refreshToken },
                    success: function (response) {
                        console.log('토큰 새로고침 완료:', response);
                        localStorage.setItem('expiredAt', response.result.expiredAt)
                        localStorage.setItem('accessToken', response.result.accessToken);
                        onUpdatePost()
                    },
                    error: function (error) {
                        console.error('리프레시토큰 인증 실패:', error);
                    }
                });
            }
        }
    });
}



function onSavePost(){
    oEditors.getById["content"].exec("UPDATE_CONTENTS_FIELD", [])
    const accessToken = localStorage.getItem('accessToken');
    const title = document.getElementById('title').innerText;
    const content = document.getElementById('content').value;
    console.log(content);
    $.ajax({
        type: 'POST',
        url: '/api/post/savePost',
        contentType: 'application/json',
        data: JSON.stringify({
            title : title,
            content: content
        }),
        headers: {
            'Authorization': 'Bearer ' + accessToken
        },
        success: function(response) {
            console.log('성공적으로 게시를 완료했습니다:', response);
            window.location.href = "/"
        },
        error: function(error) {
            if (error.status === 403) {
                var refreshToken = localStorage.getItem('refreshToken');

                $.ajax({
                    type: 'POST',
                    url: '/auth/refreshToken',
                    contentType: 'application/json',
                    headers: { refreshToken: refreshToken },
                    success: function (response) {
                        console.log('토큰 새로고침 완료:', response);
                        localStorage.setItem('expiredAt', response.result.expiredAt)
                        localStorage.setItem('accessToken', response.result.accessToken);
                        onSavePost()
                    },
                    error: function (error) {
                        console.error('리프레시토큰 인증 실패:', error);
                    }
                });
            }

        }
    });
}