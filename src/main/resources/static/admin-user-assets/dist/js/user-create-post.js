$(document).ready(function() {
    $('.select2').select2();

    loadProvinces();

    $('#province').on('change', function() {
        const provinceId = $(this).val();
        console.log('Province selected:', provinceId);

        $('#district').html('<option value="">-- Chọn Quận/Huyện --</option>').trigger('change');
        $('#ward').html('<option value="">-- Chọn Phường/Xã --</option>').trigger('change');

        if (provinceId && provinceId !== '-- Chọn Tỉnh/TP --') {
            loadDistricts(provinceId);
        }
    });


    $('#district').on('change', function() {
        const districtId = $(this).val();
        console.log('District selected:', districtId);

        $('#ward').html('<option value="">-- Chọn Phường/Xã --</option>').trigger('change');

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

        $('#province').select2();

    } catch (error) {
        console.error('Có lỗi xảy ra khi tải danh sách tỉnh/thành phố:', error);
    }
}

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

        $('#district').select2();

    } catch (error) {
        console.error('Có lỗi xảy ra khi tải danh sách quận/huyện:', error);
    }
}

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

        $('#ward').select2();

    } catch (error) {
        console.error('Có lỗi xảy ra khi tải danh sách phường/xã:', error);
    }
}


Dropzone.autoDiscover = false;

var myDropzone = new Dropzone("#images-room", {
    url: "/url",
    paramName: "images",
    maxFilesize: 2,
    acceptedFiles: "image/*",
    autoProcessQueue: false,
    previewsContainer: "#list-photos-dropzone-previews",
    clickable: false,
    previewTemplate: document.getElementById("tpl").innerHTML,
    init: function () {
        var dz = this;
        var fileNames = [];

        dz.on("addedfile", function(file) {
            if (fileNames.includes(file.name)) {
                dz.removeFile(file);
                toastr.error("Ảnh này đã được chọn. Vui lòng chọn ảnh khác.");
            } else {
                fileNames.push(file.name);
                console.log("Đã thêm file:", file);
            }
        });

        dz.on("removedfile", function(file) {
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

        document.querySelector(".js-browse-photos").addEventListener("click", function() {
            document.getElementById("images-room").click();
        });

        document.getElementById("images-room").addEventListener("change", function(event) {
            var files = event.target.files;
            for (var i = 0; i < files.length; i++) {
                dz.addFile(files[i]);
            }
            event.target.value = '';
        });
    }
});

var myDropzoneVideo = new Dropzone("#videos-room", {
    url: "url",
    paramName: "videos",
    maxFilesize: 100,
    acceptedFiles: "video/*",
    autoProcessQueue: false,
    previewsContainer: "#list-videos-dropzone-previews",
    clickable: false,
    previewTemplate: document.getElementById("tpl-video").innerHTML,
    init: function () {
        var dzVideo = this;
        var videoNames = [];

        dzVideo.on("addedfile", function(file) {
            if (videoNames.includes(file.name)) {
                dzVideo.removeFile(file); // Xóa file nếu trùng
                toastr.error("Video này đã được chọn. Vui lòng chọn video khác.");
            } else {
                videoNames.push(file.name);

                var videoElement = file.previewElement.querySelector("video");
                var videoSource = videoElement.querySelector("source");

                if (videoSource) {
                    var videoURL = URL.createObjectURL(file);
                    videoSource.src = videoURL;
                    videoElement.load();
                }

                console.log("Đã thêm video:", file);
            }
        });

        dzVideo.on("removedfile", function(file) {
            videoNames = videoNames.filter(function(name) {
                return name !== file.name;
            });
            console.log("Đã xóa video:", file);
        });

        dzVideo.on("success", function(file, response) {
            console.log("Upload thành công:", response);
        });

        dzVideo.on("error", function(file, errorMessage) {
            console.error("Upload thất bại:", errorMessage);
        });

        document.querySelector(".js-dropzone-video").addEventListener("click", function() {
            document.getElementById("videos-room").click();
        });

        document.getElementById("videos-room").addEventListener("change", function(event) {
            var files = event.target.files;
            for (var i = 0; i < files.length; i++) {
                dzVideo.addFile(files[i]);
            }


            event.target.value = '';
        });
    }
});

document.addEventListener('DOMContentLoaded', function() {
    const priceInput = document.querySelector('#room-price');

    priceInput.addEventListener('input', function() {
        let value = priceInput.value.replace(/[^\d]/g, '');

        if (value) {
            priceInput.value = new Intl.NumberFormat('de-DE').format(value);
        } else {
            priceInput.value = '';
        }
    });
});


$('#form-create-post').validate({
    rules: {
        province: {
            required: true,
        },
        district: {
            required: true,
        },
        ward: {
            required: true,
        },
        streetDetail: {
            required: true,
        },
        category: {
            required: true,
        },
        roomTitle: {
            required: true,
            minlength: 30,
            maxlength: 100
        },
        roomDescription: {
            required: true,
            minlength: 100,
        }
    },
    messages: {
        province: {
            required: "Vui lòng chọn tỉnh/thành phố."
        },
        district: {
            required: "Vui lòng chọn quận/huyện."
        },
        ward: {
            required: "Vui lòng chọn phường/xã."
        },
        streetDetail: {
            required: "Vui lòng chọn vui lòng nhập số nhà, tên đường."
        },
        category: {
            required: "Vui lòng chọn loại chuyên mục cho thuê."
        },
        roomTitle: {
            required: "Vui lòng nhập tiêu đề.",
            minlength: "Tiêu đề quá ngắn.",
            maxlength: "Tiêu đề quá dài."
        },
        roomDescription: {
            required: "Vui lòng nhập mô tả.",
            minlength: "Mô tả tối thiểu 100 kí tự"
        }
    },
    errorElement: 'span',
    errorPlacement: function (error, element) {
        error.addClass('invalid-feedback');
        element.closest('.form-group').append(error);
    },
    highlight: function (element, errorClass, validClass) {
        $(element).addClass('is-invalid');
    },
    unhighlight: function (element, errorClass, validClass) {
        $(element).removeClass('is-invalid');
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

    if (!$('#form-create-post').valid()){
        return;
    }

    const data = {
        provinceId: provinceEl.value,
        districtId: districtEl.value,
        wardId: wardEl.value,
        streetDetail: streetDetailEl.value,
        exactAddress: exactAddressEl.value,
        categoryId: categoryEl.value,
        title: roomTitleEl.value,
        description: roomDescriptionEl.value,
        price: roomPriceEl.value.replace(/\./g, ''),
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


        const videos = myDropzoneVideo.getAcceptedFiles();
        if (videos.length > 0) {
            const formDataVideo = new FormData();
            formDataVideo.append("roomId", roomId);
            videos.forEach(video => {
                formDataVideo.append("videos", video);
            });

            const videoResponse = await axios.post("/api/rooms/upload-videoRoom", formDataVideo, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });

            const videoUrls = videoResponse.data;
        } else {
            console.log("Không có video.");
        }
        // setTimeout(() => {
        //     window.location.href = "/quan-ly/tin-dang/thanh-toan-tin/roomId";
        // }, 1000)

    } catch (error) {
        if (error.message === "Vui lòng cập nhật ảnh phòng") {
            toastr.error(error.message);
        } else if (error.response && error.response.data && error.response.data.message) {
            const errorMessage = error.response.data.message;

            if (typeof errorMessage === 'object') {
                Object.keys(errorMessage).forEach(key => {
                    toastr.error(errorMessage[key]);
                });
            } else {
                toastr.error(errorMessage);
            }

            console.error("Error response from API:", errorMessage);
        } else {
            toastr.error("Có lỗi xảy ra, vui lòng thử lại.");
            console.error("Unknown error occurred:", error);
        }
    }
})





