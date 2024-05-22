$(document).ready(function () {
    function fetchData() {
        var accessToken = localStorage.getItem('accessToken');

        if (accessToken) {
            $.ajax({
                type: 'GET',
                url: '/',
                headers: {
                    'Authorization': 'Bearer ' + accessToken
                },
                success: function (data) {
                    console.log('Data fetched successfully:', data);
                },
                error: function (error) {
                    console.error('Failed to fetch data:', error);
                    handleLogout();
                }
            });
        } else {
            window.location.href = '/user/login';
        }
    }
    fetchData();
});

$(document).ready(function () {
    function fetchDataFromPostInsert() {
        var accessToken = localStorage.getItem('accessToken');

        if (accessToken) {
            $.ajax({
                type: 'GET',
                url: '/post/insert',
                headers: {
                    'Authorization': 'Bearer ' + accessToken
                },
                success: function (data) {
                    console.log('Data fetched successfully:', data);
                },
                error: function (error) {
                    console.error('Failed to fetch data:', error);
                    handleLogout();
                }
            });
        } else {
            window.location.href = '/user/login';
        }
    }
    fetchDataFromPostInsert();
});
