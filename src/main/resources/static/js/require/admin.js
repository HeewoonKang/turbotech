let adminObject = {
    init: function() {
        let _this = this;

        $("#btn-update").on("click", (event) => {
            event.preventDefault();
            _this.updateUser();
        });
        $(".btn-delete").on("click", function(event) {
            event.preventDefault();
            let userId = $(this).data('user-delete-id');
            _this.deleteUser(userId);
        });
    },

    updateUser: function() {
        let user = { // user 객체 선언
            id: $("#id").val(),
            username: $("#username").val(),
            password: $("#password").val(),
            role: $("#role").val()
        }
        if(user.password.trim() === "")
        {
            alert("비밀번호를 입력해주세요.");
            return;
        }
        var accessToken = localStorage.getItem('accessToken');
        $.ajax({
            type: "PUT",
            url: "/api/admin/usermanage/" + user.id,
            headers: {
                'Authorization': 'Bearer ' + accessToken
            },
            data: JSON.stringify(user),
            contentType: "application/json; charset=utf-8"
        }).done(function(response) {
            let status = response["status"];
            if (status === 200) {
                let message = response["data"];
                alert(message);
                location.href = "/membership/Admin";
            } else {
                let warn = "";
                let errors = response["data"];
                if (errors.username != null) warn += errors.username + "\n";
                if (errors.password != null) warn += errors.password + "\n";
                if (errors.role != null) warn += errors.role;
                alert(warn);
            }
        }).fail(function(error) {
            alert("에러 발생 : " + error);
        });
    },

    deleteUser: function (userId) {
        var accessToken = localStorage.getItem('accessToken');
        $.ajax({
            type: "DELETE",
            url: "/api/admin/usermanage/" + userId,
            headers: {
                'Authorization': 'Bearer ' + accessToken
            },
            contentType: "application/json; charset=utf-8"
        }).done(function (response) {
            let status = response["status"];
            if (status === 200) {
                let message = response["data"];
                alert(message);
            }
            location.href = "/admin";
        }).fail(function (error) {
            alert("에러 : " + error);
        })
    }
}

adminObject.init();