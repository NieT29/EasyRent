document.addEventListener('DOMContentLoaded', () => {
    const packageTypeSelect = document.getElementById('post-package-type');
    const priceTypeSelect = document.getElementById('post-package-priceType');
    const timeSelect = document.getElementById('post_package-time');
    const paymentButton = document.querySelector('.package-grand-total'); // Nút hiển thị tổng tiền
    const outOfMoneyWarning = document.querySelector('.js-note-outofmoney'); // Thẻ cảnh báo hết tiền
    const submitButton = document.querySelector('button[type="submit"]');
    const paymentMethodAccount = document.getElementById('payment_from_account');
    const paymentMethodVNPay = document.getElementById('payment_from_vnpay');

    let serviceTypes = []; // Lưu dữ liệu dịch vụ để tính toán

    // Gọi API lấy danh sách dịch vụ khi trang tải lên
    function loadServiceTypes(priceType) {
        const selectedServiceId = packageTypeSelect.value; // Lưu lại dịch vụ đã chọn

        axios.get(`/api/service-price/get-price-by-type?priceType=${priceType}`)
            .then(function(response) {
                serviceTypes = response.data; // Lưu dữ liệu dịch vụ
                populateServiceTypes(selectedServiceId); // Đổ dữ liệu vào dropdown
                updateTotalPrice(); // Tính toán tổng giá khi tải trang
            })
            .catch(function(error) {
                console.error('Lỗi khi lấy danh sách dịch vụ:', error);
            });
    }

    // Hàm đổ dữ liệu dịch vụ vào dropdown
    function populateServiceTypes(selectedServiceId) {
        const packageTypeSelect = document.getElementById('post-package-type');
        packageTypeSelect.innerHTML = '';

        serviceTypes.forEach(function(service) {
            const option = document.createElement('option');
            option.value = service.id;
            option.textContent = `${service.name} (${(service.price).toLocaleString('vi-VN')}đ/${service.priceType})`;
            packageTypeSelect.appendChild(option);
        });

        // Nếu dịch vụ đã chọn tồn tại, giữ lại dịch vụ đó. Nếu không, chọn dịch vụ đầu tiên
        if (serviceTypes.some(service => service.id == selectedServiceId)) {
            packageTypeSelect.value = selectedServiceId;
        } else {
            packageTypeSelect.value = serviceTypes[0].id; // Đặt dịch vụ đầu tiên làm mặc định nếu không có dịch vụ đã chọn
        }
    }

    // Hàm tính toán tổng giá tiền
    function updateTotalPrice() {
        const selectedServiceId = packageTypeSelect.value;
        const quantity = timeSelect.value;
        const selectedService = serviceTypes.find(service => service.id == selectedServiceId);

        if (selectedService && quantity) {
            const totalPrice = selectedService.price * quantity;
            paymentButton.textContent = `${totalPrice.toLocaleString('vi-VN')}đ`; // Hiển thị giá tiền

            // Kiểm tra nếu phương thức thanh toán là "Trừ tiền trong tài khoản"
            if (paymentMethodAccount.checked) {
                if (totalPrice > userBalance) {
                    // Số dư không đủ, disable nút thanh toán và hiển thị cảnh báo
                    outOfMoneyWarning.classList.remove('hidden-note');
                    submitButton.disabled = true;
                } else {
                    // Số dư đủ, enable nút thanh toán và ẩn cảnh báo
                    outOfMoneyWarning.classList.add('hidden-note');
                    submitButton.disabled = false;
                }
            } else {
                // Nếu không chọn "Trừ tiền trong tài khoản", enable nút thanh toán và ẩn cảnh báo
                outOfMoneyWarning.classList.add('hidden-note');
                submitButton.disabled = false;
            }
        } else {
            paymentButton.textContent = '0đ';
            submitButton.disabled = true;// Hiển thị giá trị mặc định nếu chưa đủ điều kiện
        }
    }

    document.querySelectorAll('input[name="payment_method"]').forEach(function(paymentMethod) {
        paymentMethod.addEventListener('change', updateTotalPrice);
    });

    // Lắng nghe sự kiện thay đổi trên các dropdown và tính toán lại giá tiền
    packageTypeSelect.addEventListener('change', updateTotalPrice);
    priceTypeSelect.addEventListener('change', function() {
        loadServiceTypes(this.value); // Tải lại dịch vụ khi chọn gói thời gian
    });
    timeSelect.addEventListener('change', updateTotalPrice);

    // Khi trang vừa tải, mặc định chọn gói "theo ngày" và tải dịch vụ tương ứng
    loadServiceTypes('DAILY');
});

// Trigger change event on page load to populate initial values
document.getElementById('post-package-priceType').dispatchEvent(new Event('change'));



// Tự động chọn gói "theo ngày" khi trang tải lần đầu tiên
window.addEventListener('DOMContentLoaded', function() {
    const priceTypeSelect = document.getElementById('post-package-priceType');
    priceTypeSelect.value = 'DAILY';  // Đặt giá trị mặc định là theo ngày

    priceTypeSelect.dispatchEvent(new Event('change'));
});

document.getElementById('post-package-priceType').addEventListener('change', function() {
    const priceType = this.value;
    const timeLabel = document.getElementById('time-unit-label');
    const timeSelect = document.getElementById('post_package-time');

    // Clear existing options
    timeSelect.innerHTML = '';

    if (priceType === 'DAILY') {
        timeLabel.textContent = 'Số ngày';  // Update label to 'Số ngày'

        // Add options for days
        for (let i = 1; i <= 30; i++) {
            const option = document.createElement('option');
            option.value = i;
            option.textContent = `${i} ngày`;
            timeSelect.appendChild(option);
        }

    } else if (priceType === 'WEEKLY') {
        timeLabel.textContent = 'Số tuần';  // Update label to 'Số tuần'

        // Add options for weeks
        for (let i = 1; i <= 10; i++) {
            const option = document.createElement('option');
            option.value = i;
            option.textContent = `${i} tuần`;
            timeSelect.appendChild(option);
        }

    } else if (priceType === 'MONTHLY') {
        timeLabel.textContent = 'Số tháng';  // Update label to 'Số tháng'

        // Add options for months
        for (let i = 1; i <= 12; i++) {
            const option = document.createElement('option');
            option.value = i;
            option.textContent = `${i} tháng`;
            timeSelect.appendChild(option);
        }
    }
});




