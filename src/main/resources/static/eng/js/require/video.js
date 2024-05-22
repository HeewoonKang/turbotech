$(document).on('click', '#btn-insert', function() {
    if(productName && description && copyright) {
        onSavePost()
    } else {
        alert('제목과 내용, 출처를 모두 입력해주세요.');
    }
});

$(document).on('click', '#btn-update', function() {

    if(productName && description && copyright) {
        onUpdatePost()
    } else {
        alert('제목과 내용을 모두 입력해주세요.');
    }
});


function onUpdatePost(){
    oEditors.getById["description"].exec("UPDATE_CONTENTS_FIELD", [])
    const accessToken = localStorage.getItem('accessToken');
    const productName = document.getElementById('productName').innerText;
    const description = document.getElementById('description').value;
    const copyright = document.getElementById('copyright').innerText;
    const tag = document.getElementById('tag').innerText;
    const resolution = document.getElementById('resolution').innerText;
    const imageFile = document.getElementById('imageFile').files[0];
    const videoFile = document.getElementById('videoFile').files[0];
    const postId = document.getElementById('postId').value;

    let formData = new FormData();
    formData.append('productName', productName);
    formData.append('description', description);
    formData.append('copyright', copyright);
    formData.append('tag', tag);
    formData.append('resolution', resolution);
    formData.append('image', imageFile);
    formData.append('video', videoFile);


    $.ajax({
        type: 'POST',
        url: `/api/Video/updateVideo/${postId}`,
        processData: false,
        contentType: false,
        data: formData,
        headers: {
            'Authorization': 'Bearer ' + accessToken
        },
        success: function(response) {
            console.log('성공적으로 게시를 완료했습니다:', response);
            window.location.href = "/membership/Video"
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
    oEditors.getById["description"].exec("UPDATE_CONTENTS_FIELD", [])
    const accessToken = localStorage.getItem('accessToken');
    const productName = document.getElementById('productName').innerText;
    const description = document.getElementById('description').value;
    const copyright = document.getElementById('copyright').innerText;
    const tag = document.getElementById('tag').innerText;
    const resolution = document.getElementById('resolution').innerText;
    const imageFile = document.getElementById('imageFile').files[0];
    const videoFile = document.getElementById('videoFile').files[0];

    let formData = new FormData();
    formData.append('productName', productName);
    formData.append('description', description);
    formData.append('copyright', copyright);
    formData.append('tag', tag);
    formData.append('resolution', resolution);
    formData.append('image', imageFile);
    formData.append('video', videoFile);


    $.ajax({
        type: 'POST',
        url: '/api/Video/saveVideo',
        processData: false,
        contentType: false,
        data: formData,
        headers: {
            'Authorization': 'Bearer ' + accessToken
        },
        success: function(response) {
            console.log('성공적으로 게시를 완료했습니다:', response);
            window.location.href = "/membership/Video"
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