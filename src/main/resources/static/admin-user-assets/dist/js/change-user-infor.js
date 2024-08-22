// change-password
const changePasswordFormEl = document.getElementById("form-change-password")
const currentPasswordEl = document.getElementById("current-password")
const newPasswordEl = document.getElementById("new-password")
const confirmPassword = document.getElementById("confirm-password")

$.validator.addMethod("notEqualTo", function(value, element, param) {
    return this.optional(element) || value !== $(param).val();
}, "Mật khẩu mới không được giống với mật khẩu hiện tại");

$('#form-change-password').validate({
    rules: {
        currentPassword: {
            required: true
        },
        newPassword: {
            required: true,
            notEqualTo: "current-password"
        },
        confirmPassword: {
            required: true,
            equalTo: '[name="newPassword"]'
        }
    },
    messages: {
        currentPassword: {
            required: "Mật khẩu hiện tại không được để trống",
        },
        newPassword: {
            required: "Mật khẩu mới không được để trống",
            notEqualTo: "Mật khẩu mới không được giống mật khẩu hiện tại"
        },
        confirmPassword: {
            required: "Xác nhận mật khẩu không được để trống",
            equalTo: "Xác nhận mật khẩu không khớp"
        }
    },
    errorElement: 'span',
    errorPlacement: function (error, element) {
        error.addClass('invalid-feedback');
        element.after(error);
    },
    highlight: function (element, errorClass, validClass) {
        $(element).addClass('is-invalid');
    },
    unhighlight: function (element, errorClass, validClass) {
        $(element).removeClass('is-invalid');
    }
});

changePasswordFormEl.addEventListener("submit", async (e) => {
    e.preventDefault()

    if (!$('#form-change-password').valid()) {
        return;
    }
    const data = {
        currentPassword: currentPasswordEl.value,
        newPassword: newPasswordEl.value,
        confirmPassword: confirmPassword.value
    }

    try {
        let res = await axios.put('/api/user/change-password', data)
        toastr.success("Đổi mật khẩu thành công")
        setTimeout(() => {
            window.location.href = "/quan-ly/thong-tin-ca-nhan";
        }, 1000)
    } catch (error) {
        console.log(error)
        toastr.error(error.response.data.message)
    }
})
