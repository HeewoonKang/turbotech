$(document).ready(function () {
    const checkRefreshToken = setInterval(checkRefresh, 10 * 60 * 1000);

    $('#loginForm').on('submit', function (e) {
        e.preventDefault();
        handleLogin();
    });

    $('#signupForm').on('submit', function (e) {
        e.preventDefault();
        handleSignup();
    });

    $(document).on('click', '#logoutForm, #sidebarLogoutForm',  function (e) {
        e.preventDefault();
        clearInterval(checkRefreshToken);
        handleLogout();
    });

    updateAuthLink();
});

function checkRefresh() {
    const nowTime=Date.now();
    var expiredAt = localStorage.getItem("expiredAt");
    console.log({expiredAt ,nowTime})
    if(expiredAt && Number(expiredAt) <= nowTime)
    {
        var refreshToken = localStorage.getItem('refreshToken');

        $.ajax({
            type: 'POST',
            url: '/auth/refreshToken',
            contentType: 'application/json',
            headers: { refreshToken: refreshToken },
            success: function (response) {
                console.log('토큰 새로고침 완료:', response);
                localStorage.setItem('expiredAt', response.result.expiredAt)
                localStorage.setItem('accessToken', response.result.accessToken);},
            error: function (error) {
                console.error('리프레시토큰 인증 실패:', error);
            }
        });
    }
}

function updateAuthLink() {
    var accessToken = localStorage.getItem('accessToken');
    var username = localStorage.getItem('username');
    var nickname = localStorage.getItem('nickname');
    if (accessToken && username) {
        /*$('#authLink').html('<span>어 반갑고, ' + nickname + '</span> | <a href="#" id="logoutForm">Logout</a>' +
            ' | <a href="/post/insert">포스트 등록</a>');*/
        $('#loginLinkContainer').html('<a href="#" id="logoutForm">LOGOUT</a>');
        $('.side-bar-login-section a').text('LOGOUT').attr('href', '#').attr('id', 'sidebarLogoutForm');
    } else {
        $('#loginLinkContainer').html('<a href="/user/login">LOGIN</a>');
        $('.side-bar-login-section a').text('Login').attr('href', '/user/login').removeAttr('id');
    }
}

function handleLogin() {
    var username = $('#username').val();
    var password = $('#password').val();
    var accessToken = localStorage.getItem('accessToken');

    $.ajax({
        type: 'POST',
        url: '/user/login',
        headers: {
            'Authorization': 'Bearer ' + accessToken
        },
        contentType: 'application/json',
        data: JSON.stringify({ username: username, password: password }),
        success: function (response) {
            console.log('Login successful:', response);
            localStorage.setItem('username', username);
            localStorage.setItem('nickname', response.result.nickName)
            localStorage.setItem('role', response.result.role)
            localStorage.setItem('expiredAt', response.result.expiredAt)
            localStorage.setItem('accessToken', response.result.accessToken);
            localStorage.setItem('refreshToken', response.result.refreshToken);
            updateAuthLink();

            /*if (response.result.role === 'ADMIN') {
                window.location.href = '/admin';
            } else {*/
                window.location.href = '/';
            /*}*/
        },
        error: function (error) {
            console.error('로그인 실패:', error);
        }
    });
}

function handleSignup() {
    var username = $('#username').val();
    var password = $('#password').val();
    var nickName = $('#nickName').val();
    var familyName = $('#familyName').val();
    var name = $('#name').val();
    var birth = $('#birth').val();
    var contact = $('#contact').val();
    var email = $('#email').val();
    var etcContact = $('#etcContact').val();
    var region = $('#region').val();
    var postcode = $('#postcode').val();
    var county = $('#county').val();
    var city = $('#city').val();
    var address = $('#address').val();
    var etcAddress = $('#etcAddress').val();
    var serialNumber = $('#serialNumber').val();
    var role = $('input[name="option"]:checked').val() === 'GENERAL' ? 'GENERAL' : 'RETAIL';

    $.ajax({
        type: 'POST',
        url: '/user/signUp',
        contentType: 'application/json',
        data: JSON.stringify({
            username: username,
            password: password,
            familyName: familyName,
            name: name,
            nickName: nickName,
            email: email,
            etcContact: etcContact,
            birth: birth,
            contact: contact,
            region: region,
            postcode: postcode,
            county: county,
            city: city,
            address: address,
            etcAddress: etcAddress,
            serialNumber: serialNumber,
            role: role
        }),
        success: function (response) {
            console.log('회원가입 성공:', response);
            window.location.href = '/user/login';
        },
        error: function (error) {
            if (error.status === 400) {
                var errors = error.responseJSON;
                var errorMessages = [];
                for (var key in errors) {
                    errorMessages.push(errors[key]);
                }
                alert('회원가입 오류:\n' + errorMessages.join('\n'));
            } else {
                console.error('뭐땜시든 회원가입 성공:', error);
            }
        }
    });
}
function handleLogout() {
    localStorage.removeItem('username');
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('nickname');
    localStorage.removeItem('role');
    localStorage.removeItem('expiredAt');

    window.location.href = '/';
}
