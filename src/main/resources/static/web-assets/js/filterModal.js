let overlay = document.getElementById("overlay");
// đóng mở modal category
let modalCategory = document.getElementById('categoryModal');
let btnOpenCategory = document.getElementById('openCategoryModal');
let closeCategory = modalCategory.querySelector(".popup-close");

btnOpenCategory.onclick = function() {
    modalCategory.style.display = "block";
    overlay.style.display = "block";
}

closeCategory.onclick = function() {
    modalCategory.style.display = "none";
    overlay.style.display = "none";
}

const currentUrl = window.location.pathname;
const listCategory = document.querySelectorAll('#filter-popup-category ul li');

function setDefaultCategory() {
    let defaultText = '';
    let defaultSlug = '';
    listCategory.forEach(item => {
        const slug = item.getAttribute('data'); // Lấy giá trị từ thuộc tính data

        // Kiểm tra nếu slug khớp với URL
        if (currentUrl.includes(slug)) {
            item.classList.add('selected-option'); // Thêm class 'selected-option' cho thẻ li tương ứng
            defaultText = item.textContent;
            defaultSlug = slug;
        }
    });

    // Nếu không tìm thấy URL khớp và là trang chủ, chọn mục "phòng trọ, nhà trọ" mặc định
    if (!defaultText && (currentUrl === '/' )) {
        listCategory.forEach(item => {
            const slug = item.getAttribute('data');
            if (slug === 'cho-thue-phong-tro') {
                item.classList.add('selected-option');
                defaultText = item.textContent;
                defaultSlug = slug;
            }
        });
    }


    // Cập nhật tên của nút với tên mục được chọn
    document.getElementById('openCategoryModal').textContent = defaultText;
    document.getElementById('openCategoryModal').setAttribute("data-category-slug", defaultSlug);
    document.getElementById('openCategoryModal').classList.add('filter-button-active');
}
setDefaultCategory();

listCategory.forEach(item => {
    item.addEventListener('click', function() {
        // Xóa class 'selected-option' khỏi tất cả các thẻ li
        listCategory.forEach(li => li.classList.remove('selected-option'));

        // Thêm class 'selected-option' vào thẻ li được chọn
        this.classList.add('selected-option');

        // Cập nhật tên của nút với tên thẻ li được chọn
        const selectedText = this.textContent;
        const selectedSlug = this.getAttribute('data');

        const openCategoryButton = document.getElementById('openCategoryModal');
        openCategoryButton.textContent = selectedText;
        openCategoryButton.setAttribute('data-category-slug', selectedSlug); // Thêm thuộc tính data-slug vào nút
        openCategoryButton.classList.add('filter-button-active');
        // Đóng modal sau khi chọn
        document.getElementById('categoryModal').style.display = 'none';
        document.getElementById('overlay').style.display = 'none'
    });
});


// đóng mở modal location
let modalProvince = document.getElementById("provinceModal");
let modalDistrict = document.getElementById("districtModal");
let modalWard = document.getElementById("wardModal");
let btnOpenProvince = document.getElementById("openLocationModal");

let closeModalProvince = modalProvince.querySelector(".popup-close");
let closeModalDistrict = modalDistrict.querySelector(".popup-close");
let closeModalWard = modalWard.querySelector(".popup-close");

let selectedProvince = null;
let selectedDistrict = null;
let selectedWard = null;

let selectedProvinceId = null;
let selectedDistrictId = null;
let selectedWardId = null;

const openProvinceModal = async () => {
    await loadProvinces();
    modalProvince.style.display = "block";
    overlay.style.display = "block";
};

const closeCurrentModal = () => {
    if (modalWard.style.display === "block") {
        modalWard.style.display = "none";
        modalDistrict.style.display = "block";
    } else if (modalDistrict.style.display === "block") {
        modalDistrict.style.display = "none";
        modalProvince.style.display = "block";
    } else if (modalProvince.style.display === "block") {
        modalProvince.style.display = "none";
        overlay.style.display = "none";
    }
};

const closeAllModals = () => {
    modalWard.style.display = "none";
    modalDistrict.style.display = "none";
    modalProvince.style.display = "none";
    overlay.style.display = "none";
};

closeModalProvince.onclick = closeCurrentModal;
closeModalDistrict.onclick = closeCurrentModal;
closeModalWard.onclick = closeCurrentModal;

btnOpenProvince.onclick = async () => {
    if (selectedWardId) { // Sử dụng ID thực tế khi tải lại danh sách
        await loadWards(selectedDistrictId);
        modalWard.style.display = 'block';
    } else if (selectedDistrictId) {
        await loadDistricts(selectedProvinceId);
        modalDistrict.style.display = 'block';
    } else if (selectedProvinceId) {
        modalProvince.style.display = 'block';
    } else {
        await openProvinceModal();
    }
    overlay.style.display = 'block';
}

// load province
const loadProvinces = async () => {
    try {
        const res = await axios.get(`/api/location/provinces`);
        const provinces = res.data;
        const provinceList = document.querySelector('#filter-popup-province ul');
        provinceList.innerHTML = '';

        provinces.forEach(province => {
            const li = document.createElement('li');
            li.textContent = province.name;
            li.setAttribute('data-id', province.id);
            li.setAttribute('data-name', province.name);

            li.addEventListener('click', () => {
                selectedProvinceId = province.id;
                selectedProvince = province.name;
                selectedDistrict = null;
                selectedWard = null;

                const allLi = provinceList.querySelectorAll('li');
                allLi.forEach(item => item.classList.remove('selected-option'));
                li.classList.add('selected-option');

                document.getElementById("modal-header-province").textContent = province.name;
                loadDistricts(province.id);
                document.getElementById('provinceModal').style.display = 'none';
                document.getElementById('districtModal').style.display = 'block';

                document.getElementById('openLocationModal').textContent = province.name;
                document.getElementById('openLocationModal').setAttribute('data-selected-province', province.name);
                document.getElementById('openLocationModal').classList.add('filter-button-active');

            });

            provinceList.appendChild(li);
        });
    } catch (err) {
        console.log(err);
    }
}

// load districts
const loadDistricts = async (provinceId) => {
    try {
        const res = await axios.get(`/api/location/districts`, {
            params: { provinceId: provinceId }
        });
        const districts = res.data;
        const districtList = document.querySelector('#filter-popup-district ul');
        districtList.innerHTML = '';

        const liAll = document.createElement('li');
        liAll.textContent = 'Tất cả';
        liAll.setAttribute('data-name', 'all');

        liAll.addEventListener('click', () => {
            selectedDistrict = null;
            selectedWard = null;
            selectedDistrictId = null;
            selectedWardId = null;

            const allLi = districtList.querySelectorAll('li');
            allLi.forEach(item => item.classList.remove('selected-option'));
            liAll.classList.add('selected-option');

            const provinceName = document.getElementById('openLocationModal').getAttribute('data-selected-province');
            document.getElementById('openLocationModal').textContent = provinceName;
            document.getElementById('openLocationModal').removeAttribute('data-selected-district');
            document.getElementById('openLocationModal').classList.add('filter-button-active');

            closeAllModals();
        });
        districtList.appendChild(liAll);

        if (!selectedDistrictId) {
            liAll.classList.add('selected-option');
        }

        districts.forEach(district => {
            const li = document.createElement('li');
            li.textContent = district.name;
            li.setAttribute('data-id', district.id);
            li.setAttribute('data-name', district.name);

            if (selectedDistrictId && selectedDistrictId === district.id) {
                li.classList.add('selected-option');
                liAll.classList.remove('selected-option');
            }

            li.addEventListener('click', () => {
                selectedDistrictId = district.id;
                selectedDistrict = district.name;
                selectedWard = null;
                selectedWardId = null;

                const allLi = districtList.querySelectorAll('li');
                allLi.forEach(item => item.classList.remove('selected-option'));
                li.classList.add('selected-option');

                document.getElementById("modal-header-district").textContent = district.name;
                loadWards(district.id);
                document.getElementById('districtModal').style.display = 'none';
                document.getElementById('wardModal').style.display = 'block';

                const provinceName = document.getElementById('openLocationModal').getAttribute('data-selected-province');
                document.getElementById('openLocationModal').textContent = `${district.name}, ${provinceName}`;
                document.getElementById('openLocationModal').setAttribute('data-selected-district', district.name);
                document.getElementById('openLocationModal').classList.add('filter-button-active');
            });
            districtList.appendChild(li);
        });
    }catch(err) {
        console.error("Lỗi khi tải danh sách huyện:", err)
    }
}

//load ward
const loadWards = async (districtId) => {
    try {
        const res = await axios.get('/api/location/wards', {
            params: { districtId: districtId }
        });
        const wards = res.data;
        const wardList = document.querySelector('#filter-popup-ward ul');
        wardList.innerHTML = '';

        const liAll = document.createElement('li');
        liAll.textContent = 'Tất cả';
        liAll.setAttribute('data-name', 'all');

        liAll.addEventListener('click', () => {
            selectedWard = null;
            selectedWardId = null;

            const allLi = wardList.querySelectorAll('li');
            allLi.forEach(item => item.classList.remove('selected-option'));
            liAll.classList.add('selected-option');

            const provinceName = document.getElementById('openLocationModal').getAttribute('data-selected-province');
            const districtName = document.getElementById('openLocationModal').getAttribute('data-selected-district');
            document.getElementById('openLocationModal').textContent = `${districtName}, ${provinceName}`;
            document.getElementById('openLocationModal').removeAttribute('data-selected-ward');
            document.getElementById('openLocationModal').classList.add('filter-button-active');
            closeAllModals();

        });

        wardList.appendChild(liAll);

        wards.forEach(ward => {
            const li = document.createElement('li');
            li.textContent = ward.name;
            li.setAttribute('data-id', ward.id);
            li.setAttribute('data-name', ward.name);

            if (selectedWardId && selectedWardId === ward.id) {
                li.classList.add('selected-option');
                liAll.classList.remove('selected-option');
            }

            li.addEventListener('click', () => {
                selectedWardId = ward.id;
                selectedWard = ward.name;

                const allLi = wardList.querySelectorAll('li');
                allLi.forEach(item => item.classList.remove('selected-option'));


                li.classList.add('selected-option');

                const provinceName = document.getElementById('openLocationModal').getAttribute('data-selected-province');
                const districtName = document.getElementById('openLocationModal').getAttribute('data-selected-district');
                document.getElementById('openLocationModal').textContent = `${ward.name}, ${districtName}, ${provinceName}`;
                document.getElementById('openLocationModal').setAttribute('data-selected-ward', ward.name);
                document.getElementById('openLocationModal').classList.add('filter-button-active');
                closeAllModals();

            });

            wardList.appendChild(li);
        });

        if (!selectedWardId) {
            liAll.classList.add('selected-option');
        }
    } catch (err) {
        console.error( err);
    }
}

// Đóng mở modal cho Price
let modalPrice = document.getElementById("priceModal");
let btnPrice = document.getElementById("openPriceModal");
let closeModalPrice = modalPrice.querySelector(".popup-close");

btnPrice.onclick = function() {
    modalPrice.style.display = "block";
    overlay.style.display = "block";
}

closeModalPrice.onclick = function() {
    modalPrice.style.display = "none";
    overlay.style.display = "none";
}

// Slider Price
let priceSlider = document.getElementById('priceRange');

noUiSlider.create(priceSlider, {
    start: [0, 15], // Giá trị bắt đầu
    connect: true,
    range: {
        'min': 0,
        'max': 15
    },
    step: 0.5,
    behaviour: 'unconstrained-tap',
    format: {
        to: function (value) {
            return value.toFixed(1) + ' triệu';
        },
        from: function (value) {
            return Number(value.replace(' triệu', ''));
        }
    }
});

function updateActiveButtonPrice(lowerValue, upperValue) {
    let buttons = document.querySelectorAll('#priceModal .btn-quick-select');
    buttons.forEach(function(button) {
        let range = JSON.parse(button.getAttribute('data-range'));
        if (lowerValue === range[0] && upperValue === range[1]) {
            button.classList.add('quick-select-active');
        } else {
            button.classList.remove('quick-select-active');
        }
    });
}

let quickSelectButtonsPrice = document.querySelectorAll('#priceModal .btn-quick-select');
quickSelectButtonsPrice.forEach(function(button) {
    button.addEventListener('click', function() {
        let range = JSON.parse(this.getAttribute('data-range'));
        priceSlider.noUiSlider.set(range);
        updateActiveButtonPrice(range[0], range[1]);
    });
});

priceSlider.noUiSlider.on('update', function (values, handle) {
    let lowerValue = parseFloat(values[0].replace(' triệu', ''));
    let upperValue = parseFloat(values[1].replace(' triệu', ''));

    if (lowerValue > upperValue) {
        let temp = lowerValue;
        lowerValue = upperValue;
        upperValue = temp;
    }

    // Kiểm tra và định dạng cho các giá trị nguyên
    let lowerDisplay = Number.isInteger(lowerValue) ? lowerValue : lowerValue.toFixed(1);
    let upperDisplay = Number.isInteger(upperValue) ? upperValue : upperValue.toFixed(1);

    if (lowerValue === 15 && upperValue === 15) {
        document.getElementById('priceRangeLabel').value = 'Trên 15 triệu';
    } else {
        document.getElementById('priceRangeLabel').value = lowerDisplay + ' - ' + upperDisplay + ' triệu';
    }

    updateActiveButtonPrice(lowerValue, upperValue);
});

document.getElementById('btn-apply-price').addEventListener('click', function() {
    const priceRangeLabel = document.getElementById('priceRangeLabel').value;

    let lowerValue, upperValue;

    if (priceRangeLabel === '0 - 1 triệu') {
        // Xử lý trường hợp "Dưới 1 triệu"
        lowerValue = 0;
        upperValue = 1000000;
    } else if (priceRangeLabel.includes('Trên 15 triệu')) {
        // Xử lý trường hợp "Trên 15 triệu"
        lowerValue = 15000000;
        upperValue = null;
    } else {
        // Xử lý các trường hợp bình thường
        lowerValue = parseFloat(priceRangeLabel.split(' - ')[0].replace(' triệu', '')) * 1000000;
        upperValue = parseFloat(priceRangeLabel.split(' - ')[1].replace(' triệu', '')) * 1000000;
    }

    // Cập nhật nội dung của nút "Chọn giá"
    const openPriceModal = document.getElementById('openPriceModal');
    openPriceModal.textContent = priceRangeLabel;
    openPriceModal.classList.add('filter-button-active');

    // Thêm thuộc tính data-min-price và data-max-price vào nút
    openPriceModal.setAttribute('data-min-price', lowerValue);
    openPriceModal.setAttribute('data-max-price', upperValue);

    // Đóng modal giá và overlay sau khi áp dụng
    modalPrice.style.display = 'none';
    overlay.style.display = 'none';
});


// Đóng mở modal cho Acreage
let modalAcreage = document.getElementById("acreageModal");
let btnAcreage = document.getElementById("openAcreageModal");
let closeModalAcreage = modalAcreage.querySelector(".popup-close");

btnAcreage.onclick = function() {
    modalAcreage.style.display = "block";
    overlay.style.display = "block";
}

closeModalAcreage.onclick = function() {
    modalAcreage.style.display = "none";
    overlay.style.display = "none";
}

// Slider Acreage
let acreageSlider = document.getElementById('acreageRange');

noUiSlider.create(acreageSlider, {
    start: [0, 90], // Giá trị bắt đầu
    connect: true,
    range: {
        'min': 0,
        'max': 90
    },
    step: 5,
    behaviour: 'unconstrained-tap',
    format: {
        to: function (value) {
            return value.toFixed(0) + ' m2';
        },
        from: function (value) {
            return Number(value.replace(' m2', ''));
        }
    }
});

function updateActiveButtonAcreage(lowerValue, upperValue) {
    let buttons = document.querySelectorAll('#acreageModal .btn-quick-select');
    buttons.forEach(function(button) {
        let range = JSON.parse(button.getAttribute('data-range'));
        if (lowerValue === range[0] && upperValue === range[1]) {
            button.classList.add('quick-select-active');
        } else {
            button.classList.remove('quick-select-active');
        }
    });
}

let quickSelectButtonsAcreage = document.querySelectorAll('#acreageModal .btn-quick-select');
quickSelectButtonsAcreage.forEach(function(button) {
    button.addEventListener('click', function() {
        let range = JSON.parse(this.getAttribute('data-range'));
        acreageSlider.noUiSlider.set(range);
        updateActiveButtonAcreage(range[0], range[1]);
    });
});

acreageSlider.noUiSlider.on('update', function (values, handle) {
    let lowerValue = parseFloat(values[0].replace(' m2', ''));
    let upperValue = parseFloat(values[1].replace(' m2', ''));

    if (lowerValue > upperValue) {
        let temp = lowerValue;
        lowerValue = upperValue;
        upperValue = temp;
    }

    if (lowerValue === 90 && upperValue === 90) {
        document.getElementById('acreageRangeLabel').value = 'Trên 90m2';
    } else {
        document.getElementById('acreageRangeLabel').value = lowerValue.toFixed(0) + ' - ' + upperValue.toFixed(0) + ' m2';
    }

    updateActiveButtonAcreage(lowerValue, upperValue);
});

document.getElementById('btn-apply-acreage').addEventListener('click', function() {
    const acreageRangeLabel = document.getElementById('acreageRangeLabel').value;

    let lowerValue, upperValue;

    // Xử lý trường hợp "Dưới 20 m²"
    if (acreageRangeLabel === '0 - 20 m2') {
        lowerValue = 0;
        upperValue = 20;
    }
    // Xử lý trường hợp "Trên 90 m²"
    else if (isNaN(parseFloat(acreageRangeLabel.split(' - ')[1]))) {
        lowerValue = 90;
        upperValue = null;
    }
    // Xử lý các trường hợp bình thường
    else {
        lowerValue = parseFloat(acreageRangeLabel.split(' - ')[0].replace(' m2', ''));
        upperValue = parseFloat(acreageRangeLabel.split(' - ')[1].replace(' m2', ''));
    }

    // Cập nhật nội dung của nút "Chọn diện tích"
    const openAcreageModal = document.getElementById('openAcreageModal');
    openAcreageModal.textContent = acreageRangeLabel;
    openAcreageModal.classList.add('filter-button-active');

    // Thêm thuộc tính data-min-acreage và data-max-acreage vào nút
    openAcreageModal.setAttribute('data-min-acreage', lowerValue);
    openAcreageModal.setAttribute('data-max-acreage', upperValue);

    // Đóng modal diện tích và overlay sau khi áp dụng
    modalAcreage.style.display = 'none';
    overlay.style.display = 'none';
});



// đóng mở overlay
window.onclick = function(event) {
    if (event.target == overlay) {
        modalCategory.style.display = "none"
        modalPrice.style.display = "none";
        modalAcreage.style.display = "none";
        modalProvince.style.display = "none"
        modalDistrict.style.display = "none"
        modalWard.style.display = "none"
        overlay.style.display = "none";
    }
}

document.getElementById('btn-submit-filter').addEventListener('click', function() {
    const categorySlug = document.getElementById('openCategoryModal').getAttribute('data-category-slug');
    const provinceSlug = document.getElementById('openLocationModal').getAttribute('data-selected-province');
    const districtSlug = document.getElementById('openLocationModal').getAttribute('data-selected-district');
    const wardSlug = document.getElementById('openLocationModal').getAttribute('data-selected-ward');

    let searchUrl = `/${categorySlug}`;
    if (provinceSlug) {
        searchUrl += `/${provinceSlug}`;
    }
    if (districtSlug) {
        searchUrl += `/${districtSlug}`;
    }
    if (wardSlug) {
        searchUrl += `/${wardSlug}`;
    }

    const minPrice = document.getElementById('openPriceModal').getAttribute('data-min-price');
    const maxPrice = document.getElementById('openPriceModal').getAttribute('data-max-price');
    const minAcreage = document.getElementById('openAcreageModal').getAttribute('data-min-acreage');
    const maxAcreage = document.getElementById('openAcreageModal').getAttribute('data-max-acreage');
    let params = [];

    if (minPrice && minPrice !== "0") {
        params.push(`gia_tu=${minPrice}`);
    }
    if (maxPrice && maxPrice !== "null") {
        params.push(`gia_den=${maxPrice}`);
    } else if (!minPrice && maxPrice) {
        params.push(`gia_den=${maxPrice}`);
    }

    if (minAcreage && minAcreage !== "0") {
        params.push(`dien_tich_tu=${minAcreage}`);
    }
    if (maxAcreage && maxAcreage !== "null") {
        params.push(`dien_tich_den=${maxAcreage}`);
    } else if (!minAcreage && maxAcreage) { // Trường hợp "Dưới 20m²"
        params.push(`dien_tich_den=${maxAcreage}`);
    }

    if (params.length > 0) {
        searchUrl += `?${params.join('&')}`;
    }

    window.location.href = searchUrl;
})






