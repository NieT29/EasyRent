document.addEventListener("DOMContentLoaded", function () {
    const radioButtons = document.querySelectorAll('input[name="amount"]');
    const inputField = document.querySelector('input[name="amount_input"]');

    radioButtons.forEach(function (radio) {
        radio.addEventListener("click", function () {
            inputField.value = new Intl.NumberFormat('de-DE').format(this.value);
        });
    });

    inputField.addEventListener("input", function () {
        let value = this.value.replace(/\./g, '');

        if (!isNaN(value) && value !== '') {
            value = new Intl.NumberFormat('de-DE').format(value);
            this.value = value;
        }
        else if (value === '') {
            this.value = '';
        }

        const rawValue = value.replace(/\./g, '');
        let radioMatched = false;

        radioButtons.forEach(function (radio) {
            if (radio.value === rawValue) {
                radio.checked = true;
                radioMatched = true;
            } else {
                radio.checked = false;
            }
        });

        if (!radioMatched) {
            radioButtons.forEach(function (radio) {
                radio.checked = false;
            });
        }
    });
});

$.validator.addMethod("validateAmount", function(value, element) {
    let amount = value.replace(/\./g, '');
    return this.optional(element) || (!isNaN(amount) && parseInt(amount, 10) >= 10000);
}, "Vui lòng nhập số tiền hợp lệ và số tiền nạp tối thiểu là 10.000đ");

$('#depositForm').validate({
    rules: {
        amount_input: {
            required: true,
            validateAmount: true,
            max: 5000000
        }
    },
    messages: {
        amount_input: {
            required: "Số tiền không được để trống",
            validateAmount: "Vui lòng nhập số tiền hợp lệ và số tiền nạp tối thiểu là 10.000đ",
            max: "Số tiền nạp tối đa là 5.000.000"
        }
    },
    errorElement: 'span',
    errorPlacement: function (error, element) {
        error.addClass('invalid-feedback');
        element.closest('.input-group').append(error);
    },
    highlight: function (element, errorClass, validClass) {
        $(element).addClass('is-invalid');
    },
    unhighlight: function (element, errorClass, validClass) {
        $(element).removeClass('is-invalid');
    }
});

document.getElementById('depositForm').addEventListener('submit', function (event) {
    event.preventDefault();

    if (!$("#depositForm").valid()) {
        return;
    }

    let amountInput = document.querySelector('input[name="amount_input"]').value.replace(/[^0-9]/g, '');


    const depositRequest = {
        amount: parseInt(amountInput, 10)
    };

    axios.post('/api/payments/create_payment', depositRequest)
        .then(function (response) {
            if (response.data.paymentUrl) {
                window.location.href = response.data.paymentUrl;
            } else {
                toastr.error('Đã xảy ra lỗi trong quá trình chuyển sang trang thanh toán. Vui lòng thử lại.');
            }
        })
        .catch(function (error) {
            console.log(error);
            toastr.error('Đã xảy ra lỗi trong quá trình nạp tiền, vui lòng thử lại.');
        });
});