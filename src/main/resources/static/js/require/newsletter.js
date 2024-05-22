$(document).on('click', '#btn-insert', function() {
    if(title && content && source) {
        onSavePost()
    } else {
        alert('제목과 내용, 출처를 모두 입력해주세요.');
    }
});
$(document).on('click', '#btn-update', function() {

    if(title && content && source) {
        onUpdatePost()
    } else {
        alert('제목과 내용을 모두 입력해주세요.');
    }
});


function onUpdatePost(){
    oEditors.getById["content"].exec("UPDATE_CONTENTS_FIELD", [])
    const accessToken = localStorage.getItem('accessToken');
    const title = document.getElementById('title').innerText;
    const content = document.getElementById('content').value;
    const source = document.getElementById('source').innerText;
    const tag = document.getElementById('tag').innerText;
    const country = document.getElementById('country').innerText;
    const imageFile = document.getElementById('imageFile').files[0];
    const postId = document.getElementById('postId').value;

    let formData = new FormData();
    formData.append('title', title);
    formData.append('content', content);
    formData.append('source', source);
    formData.append('tag', tag);
    formData.append('country', country);
    formData.append('image', imageFile);


    $.ajax({
        type: 'PUT',
        url: `/api/Newsletter/updateNewsletter/${postId}`,
        processData: false,
        contentType: false,
        data: formData,
        headers: {
            'Authorization': 'Bearer ' + accessToken
        },
        success: function(response) {
            console.log('성공적으로 게시를 완료했습니다:', response);
            window.location.href = "/membership/Newsletter"
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
    const source = document.getElementById('source').innerText;
    const tag = document.getElementById('tag').innerText;
    const country = document.getElementById('country').innerText;
    const imageFile = document.getElementById('imageFile').files[0];

    let formData = new FormData();
    formData.append('title', title);
    formData.append('content', content);
    formData.append('source', source);
    formData.append('tag', tag);
    formData.append('country', country);
    formData.append('image', imageFile);


    $.ajax({
        type: 'POST',
        url: '/api/Newsletter/saveNewsletter',
        processData: false,
        contentType: false,
        data: formData,
        headers: {
            'Authorization': 'Bearer ' + accessToken
        },
        success: function(response) {
            console.log('성공적으로 게시를 완료했습니다:', response);
            window.location.href = "/membership/Newsletter"
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