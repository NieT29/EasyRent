let totalPrice = 0;
document.addEventListener('DOMContentLoaded', () => {
    const packageTypeSelect = document.getElementById('post-package-type');
    const priceTypeSelect = document.getElementById('post-package-priceType');
    const timeSelect = document.getElementById('post_package-time');
    const paymentButton = document.querySelector('.package-grand-total');
    const outOfMoneyWarning = document.querySelector('.js-note-outofmoney');
    const submitButton = document.querySelector('button[type="submit"]');
    const paymentMethodAccount = document.getElementById('payment_from_account');
    const paymentMethodVNPay = document.getElementById('payment_from_vnpay');

    let serviceTypes = [];


    function loadServiceTypes(priceType) {
        const selectedServiceTypeId = document.querySelector('#post-package-type option:checked')?.getAttribute('data-service-type-id');  // Lưu lại serviceTypeId đã chọn

        axios.get(`/api/service-price/get-price-by-type?priceType=${priceType}`)
            .then(function(response) {
                serviceTypes = response.data;


                const selectedService = serviceTypes.find(service => service.id == selectedServiceTypeId);

                populateServiceTypes(selectedService?.servicePriceId || serviceTypes[0]?.servicePriceId);
                updateTotalPrice();
            })
            .catch(function(error) {
                console.error('Lỗi khi lấy danh sách dịch vụ:', error);
            });
    }

    function populateServiceTypes(selectedServicePriceId) {
        const packageTypeSelect = document.getElementById('post-package-type');
        packageTypeSelect.innerHTML = '';

        serviceTypes.forEach(function(service) {
            const option = document.createElement('option');
            option.value = service.servicePriceId;
            option.textContent = `${service.name} (${(service.price).toLocaleString('vi-VN')}đ/${service.priceType})`;
            option.setAttribute('data-service-type-id', service.id);
            packageTypeSelect.appendChild(option);
        });

        if (serviceTypes.some(service => service.servicePriceId == selectedServicePriceId)) {
            packageTypeSelect.value = selectedServicePriceId;
        } else {
            packageTypeSelect.value = serviceTypes[0].servicePriceId;
        }
    }

    function updateTotalPrice() {
        const selectedServiceId = packageTypeSelect.value;
        const quantity = timeSelect.value;
        const selectedService = serviceTypes.find(service => service.servicePriceId == selectedServiceId);

        if (selectedService && quantity) {
            totalPrice = selectedService.price * quantity;
            paymentButton.textContent = `${totalPrice.toLocaleString('vi-VN')}đ`;


            if (paymentMethodAccount.checked) {
                if (totalPrice > userBalance) {
                    outOfMoneyWarning.classList.remove('hidden-note');
                    submitButton.disabled = true;
                } else {
                    outOfMoneyWarning.classList.add('hidden-note');
                    submitButton.disabled = false;
                }
            } else {
                outOfMoneyWarning.classList.add('hidden-note');
                submitButton.disabled = false;
            }
        } else {
            paymentButton.textContent = '0đ';
            submitButton.disabled = true;
        }
    }


    document.querySelectorAll('input[name="payment_method"]').forEach(function(paymentMethod) {
        paymentMethod.addEventListener('change', updateTotalPrice);
    });

    packageTypeSelect.addEventListener('change', updateTotalPrice);
    priceTypeSelect.addEventListener('change', function() {
        loadServiceTypes(this.value);
    });
    timeSelect.addEventListener('change', updateTotalPrice);

    loadServiceTypes('DAILY');
});


document.getElementById('post-package-priceType').dispatchEvent(new Event('change'));


window.addEventListener('DOMContentLoaded', function() {
    const priceTypeSelect = document.getElementById('post-package-priceType');
    priceTypeSelect.value = 'DAILY';

    priceTypeSelect.dispatchEvent(new Event('change'));
});

document.getElementById('post-package-priceType').addEventListener('change', function() {
    const priceType = this.value;
    const timeLabel = document.getElementById('time-unit-label');
    const timeSelect = document.getElementById('post_package-time');


    timeSelect.innerHTML = '';

    if (priceType === 'DAILY') {
        timeLabel.textContent = 'Số ngày';
        for (let i = 1; i <= 30; i++) {
            const option = document.createElement('option');
            option.value = i;
            option.textContent = `${i} ngày`;
            timeSelect.appendChild(option);
        }

    } else if (priceType === 'WEEKLY') {
        timeLabel.textContent = 'Số tuần';


        for (let i = 1; i <= 10; i++) {
            const option = document.createElement('option');
            option.value = i;
            option.textContent = `${i} tuần`;
            timeSelect.appendChild(option);
        }

    } else if (priceType === 'MONTHLY') {
        timeLabel.textContent = 'Số tháng';

        for (let i = 1; i <= 12; i++) {
            const option = document.createElement('option');
            option.value = i;
            option.textContent = `${i} tháng`;
            timeSelect.appendChild(option);
        }
    }
});


document.getElementById('btn-payment-service').addEventListener('click', function(e) {
    e.preventDefault()

    const urlParts = window.location.pathname.split('/');
    const roomId = urlParts[urlParts.length - 1];

    const selectedServicePriceId = document.getElementById('post-package-type').value;  // Lấy ID của servicePrice đã chọn
    const selectedServiceTypeId = document.querySelector('#post-package-type option:checked').getAttribute('data-service-type-id');  // Lấy ID của serviceType đã chọn
    const selectedDays = document.getElementById('post_package-time').value;  // Số ngày đã chọn
    const paymentMethodAccount = document.getElementById('payment_from_account');
    const selectedPaymentMethod = paymentMethodAccount.checked ? 'ACCOUNT_BALANCE' : 'VNPAY';

    const priceType = document.getElementById('post-package-priceType').value;

    function calculateTotalDays(selectedDays, priceType) {
        if (priceType === 'DAILY') {
            return selectedDays;
        } else if (priceType === 'WEEKLY') {
            return selectedDays * 7;
        } else if (priceType === 'MONTHLY') {
            return selectedDays * 30;
        }
        return 0;
    }

    const totalDay = calculateTotalDays(selectedDays, priceType);

    axios.put('/api/order-service/update', {
        roomId: roomId,
        serviceTypeId: selectedServiceTypeId,
        servicePriceId: selectedServicePriceId,
        totalDay: totalDay,
        totalPrice: totalPrice,
        paymentMethod: selectedPaymentMethod
    })
        .then(function (response) {
            console.log('Full API response:', response.data);

            const orderServiceId = response.data.id;

            if (!orderServiceId) {
                throw new Error('orderServiceId không tồn tại trong phản hồi');
            }

            let paymentPromise;
            if (selectedPaymentMethod === 'ACCOUNT_BALANCE') {
                paymentPromise = axios.post('/api/payments/payment-from-account', {
                    orderServiceId: orderServiceId,
                    totalPrice: totalPrice
                });
            } else {
                console.log('Thanh toán qua VNPay, xử lý ngoài hệ thống');
                paymentPromise = Promise.resolve();
            }

            return paymentPromise;
        })
        .then(function (paymentResponse) {
            if (paymentResponse && paymentResponse.data) {
                console.log('Thanh toán thành công:', paymentResponse.data);
            }
            alert('Thanh toán và cập nhật dịch vụ hoàn tất!');
        })
        .catch(function (error) {
            console.error('Có lỗi xảy ra trong quá trình thanh toán hoặc cập nhật:', error);
            alert('Có lỗi xảy ra. Vui lòng thử lại!');
        });
});








