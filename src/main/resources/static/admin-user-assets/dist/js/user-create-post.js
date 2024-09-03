$(document).ready(function() {
    $('.select2').select2();

    loadProvinces();

    $('#province').on('change', function() {
        const provinceId = $(this).val();
        console.log('Province selected:', provinceId);

        // Đặt lại quận/huyện và phường/xã khi chọn tỉnh khác
        $('#district').html('<option value="">-- Chọn Quận/Huyện --</option>').trigger('change');
        $('#ward').html('<option value="">-- Chọn Phường/Xã --</option>').trigger('change');

        // Kiểm tra nếu provinceId là giá trị hợp lệ trước khi gọi API
        if (provinceId && provinceId !== '-- Chọn Tỉnh/TP --') {
            loadDistricts(provinceId);
        }
    });


    // Khi người dùng chọn quận/huyện
    $('#district').on('change', function() {
        const districtId = $(this).val();
        console.log('District selected:', districtId);

        // Đặt lại phường/xã khi chọn quận/huyện khác
        $('#ward').html('<option value="">-- Chọn Phường/Xã --</option>').trigger('change');

        // Kiểm tra nếu districtId là giá trị hợp lệ trước khi gọi API
        if (districtId && districtId !== '-- Chọn Quận/Huyện --') {
            loadWards(districtId);
        }
    });

    function updateAddress() {
        const streetDetail = $('#street-detail').val().trim();
        const ward = $('#ward option:selected').text().trim();
        const district = $('#district option:selected').text().trim();
        const province = $('#province option:selected').text().trim();

        let exactAddress = streetDetail;
        if (streetDetail !== "") {
            exactAddress += ", ";
        }
        if (ward !== "-- Chọn Phường/Xã --" && ward !== "") {
            exactAddress += `${ward}, `;
        }
        if (district !== "-- Chọn Quận/Huyện --" && district !== "") {
            exactAddress += `${district}, `;
        }
        if (province !== "-- Chọn Tỉnh/TP --" && province !== "") {
            exactAddress += `${province}`;
        }

        $('#exact-address').val(exactAddress);
    }

    $('#province').on('change', function() {
        updateAddress();
    });

    $('#district').on('change', function() {
        updateAddress();
    });

    $('#ward').on('change', function() {
        updateAddress();
    });

    $('#street-detail').on('input', function() {
        updateAddress();
    });
});

// Hàm loadProvinces để tải danh sách tỉnh
async function loadProvinces() {
    try {
        const response = await axios.get('/api/location/provinces');
        const provinces = response.data;
        const provinceSelect = document.getElementById('province');

        provinces.forEach(function (province) {
            let option = document.createElement('option');
            option.value = province.id;
            option.text = province.name;
            provinceSelect.add(option);
        });

        // Cập nhật select2 sau khi thêm các tùy chọn mới
        $('#province').select2();

    } catch (error) {
        console.error('Có lỗi xảy ra khi tải danh sách tỉnh/thành phố:', error);
    }
}

// Hàm loadDistricts để tải danh sách quận/huyện theo tỉnh
async function loadDistricts(provinceId) {
    try {
        const response = await axios.get('/api/location/districts', {
            params: { provinceId: provinceId }
        });
        const districts = response.data;
        const districtSelect = document.getElementById('district');
        districtSelect.innerHTML = '<option value="">-- Chọn Quận/Huyện --</option>';

        districts.forEach(function (district) {
            let option = document.createElement('option');
            option.value = district.id;
            option.text = district.name;
            districtSelect.add(option);
        });

        // Cập nhật select2 sau khi thêm các tùy chọn mới
        $('#district').select2();

    } catch (error) {
        console.error('Có lỗi xảy ra khi tải danh sách quận/huyện:', error);
    }
}

// Hàm loadWards để tải danh sách phường/xã theo quận/huyện
async function loadWards(districtId) {
    try {
        const response = await axios.get('/api/location/wards', {
            params: { districtId: districtId }
        });
        const wards = response.data;
        const wardSelect = document.getElementById('ward');
        wardSelect.innerHTML = '<option value="">-- Chọn Phường/Xã --</option>';

        wards.forEach(function (ward) {
            let option = document.createElement('option');
            option.value = ward.id;
            option.text = ward.name;
            wardSelect.add(option);
        });

        // Cập nhật select2 sau khi thêm các tùy chọn mới
        $('#ward').select2();

    } catch (error) {
        console.error('Có lỗi xảy ra khi tải danh sách phường/xã:', error);
    }
}


Dropzone.autoDiscover = false;

var myDropzone = new Dropzone("#images-room", {
    url: "/url", // URL cho upload, nếu bạn muốn tự quản lý thì để trống
    paramName: "images", // Tên tham số truyền file lên server
    maxFilesize: 2, // MB
    acceptedFiles: "image/*",
    autoProcessQueue: false, // Không tự động upload, bạn sẽ xử lý bằng tay
    previewsContainer: "#list-photos-dropzone-previews", // Container để hiển thị các ảnh
    clickable: false, // Không sử dụng mặc định, bạn sẽ tự kiểm soát
    previewTemplate: document.getElementById("tpl").innerHTML, // Sử dụng template của bạn
    init: function () {
        var dz = this;
        var fileNames = [];

        // Xử lý khi thêm file mới
        dz.on("addedfile", function(file) {
            if (fileNames.includes(file.name)) {
                dz.removeFile(file); // Xóa file nếu trùng
                toastr.error("Ảnh này đã được chọn. Vui lòng chọn ảnh khác.");
            } else {
                fileNames.push(file.name);
                console.log("Đã thêm file:", file);
            }
        });

        dz.on("removedfile", function(file) {
            // Loại bỏ tên file khỏi danh sách fileNames khi file bị xóa
            fileNames = fileNames.filter(function(name) {
                return name !== file.name;
            });
            console.log("Đã xóa file:", file);
        });

        dz.on("success", function(file, response) {
            console.log("Upload thành công:", response);
        });

        dz.on("error", function(file, errorMessage) {
            console.error("Upload thất bại:", errorMessage);
        });

        // Khi người dùng click vào nút thêm ảnh
        document.querySelector(".js-browse-photos").addEventListener("click", function() {
            document.getElementById("images-room").click();
        });

        // Khi người dùng chọn ảnh từ file input
        document.getElementById("images-room").addEventListener("change", function(event) {
            var files = event.target.files;
            for (var i = 0; i < files.length; i++) {
                dz.addFile(files[i]); // Thêm file vào Dropzone
            }

            // Reset input để đảm bảo sự kiện `change` luôn được kích hoạt
            event.target.value = '';
        });
    }
});


const provinceEl = document.getElementById('province');
const districtEl = document.getElementById('district');
const wardEl =document.getElementById('ward');
const streetDetailEl = document.getElementById('street-detail');
const exactAddressEl = document.getElementById('exact-address');
const categoryEl = document.getElementById('room-category');
const roomTitleEl = document.getElementById('room-title');
const roomDescriptionEl = document.getElementById('room-description');
const roomPriceEl = document.getElementById('room-price');
const roomAcreageEl = document.getElementById('room-acreage');
const roomSubjectRentEl = document.getElementById('room-subject-rent');
const btnCreateEl = document.getElementById('btn-create-room');

btnCreateEl.addEventListener('click', async (e) => {
    e.preventDefault();

    const data = {
        provinceId: provinceEl.value,
        districtId: districtEl.value,
        wardId: wardEl.value,
        streetDetail: streetDetailEl.value,
        exactAddress: exactAddressEl.value,
        categoryId: categoryEl.value,
        title: roomTitleEl.value,
        description: roomDescriptionEl.value,
        price: roomPriceEl.value,
        acreage: roomAcreageEl.value,
        subjectRent: roomSubjectRentEl.value,
    }


    try {
        const images = myDropzone.getAcceptedFiles();
        if (images.length === 0) {
            throw new Error("Vui lòng cập nhật ảnh phòng");
        }

        const roomResponse = await axios.post('/api/rooms/createRoom', data);

        const roomId = roomResponse.data;

        const formData = new FormData();
        formData.append("roomId", roomId);
        images.forEach(image => {
            formData.append("images", image);
        });

        const imageResponse = await axios.post("/api/rooms/upload-imageRoom", formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });

        const imageUrls = imageResponse.data;

        console.log("Phòng và ảnh đã được đăng thành công:", roomId, imageUrls);

        setTimeout(() => {
            window.location.href = "/";
        }, 1000)
    } catch (error) {
        if (error.message === "Vui lòng cập nhật ảnh phòng") {
            toastr.error(error.message);
        } else if (error.response && error.response.data && error.response.data.message) {
            const errorMessage = error.response.data.message;

            // Hiển thị toastr cho từng lỗi cụ thể
            if (typeof errorMessage === 'object') {
                Object.keys(errorMessage).forEach(key => {
                    toastr.error(errorMessage[key]);
                });
            } else {
                toastr.error(errorMessage);
            }

            // Log lỗi đơn giản hơn để theo dõi
            console.error("Error response from API:", errorMessage);
        } else {
            toastr.error("Có lỗi xảy ra, vui lòng thử lại.");
            console.error("Unknown error occurred:", error);
        }
    }
})





