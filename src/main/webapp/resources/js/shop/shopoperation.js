$(function () {
    var initUrl = '/o2o/shopadmin/getshopinitinfo';
    var registerShopUrl = '/o2o/shopadmin/registershop';

    getShopInitInfo();

    // 获取下拉列表的内容
    function getShopInitInfo() {
        $.getJSON(initUrl, function (data) {
            if (data.success) {
                var tempHtml = '';
                var tempAreaHtml = '';

                data.shopCategoryList.map(function (item, index) {
                    tempHtml += '<option data-id="' + item.shopCategoryId + '">' + item.shopCategoryName + '</option>';
                });

                data.areaList.map(function (item, index) {
                    tempAreaHtml += '<option data-id="' + item.areaId + '">' + item.areaName + '</option>';
                });

                $('#shop-category').html(tempHtml);
                $('#area').html(tempAreaHtml);
            }
        });

        $('#submit').click(function () {
            var shop = {};

            // 文本框变量获取
            shop.shopName = $('#shop-name').val();
            shop.shopAddr = $('#shop-addr').val();
            shop.phone = $('#shop-phone').val();
            shop.shopDesc = $('#shop-desc').val();

            // 下拉列表选择获取
            shop.shopCategory = {
                shopCategoryId: $('#shop-category').find('option').not(function () {
                    return !this.selected;
                }).data('id')
            };

            shop.area = {
                areaId: $('#area').find('option').not(function () {
                    return !this.selected;
                }).data('id')
            };

            // 图片获取
            var shopImg = $('#shop-img')[0].files[0];

            // 验证码获取
            var verifyCodeActual = $('#j_captcha').val();
            if (!verifyCodeActual) {
                $.toast("请输入验证码！");
                return;
            }

            // 封装信息
            var formData = new FormData();
            formData.append('shopImg', shopImg);
            formData.append('shopStr', JSON.stringify(shop));
            formData.append("verifyCodeActual", verifyCodeActual);

            // 将数据提交后台
            $.ajax({
                url: registerShopUrl,
                type: 'POST',
                data: formData,
                contentType: false,
                processData: false,
                cache: false,
                success: function (data) {
                    if (data.success) {
                        $.toast('提交成功!');
                    } else {
                        $.toast('提交成功!' + data.errMsg);
                    }

                    $('#captcha_img').click();
                }
            });
        });
    };
});