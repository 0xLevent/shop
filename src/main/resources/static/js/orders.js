// orders.js

$(document).ready(function() {
    // Sipariş modalını hazırla
    $("#checkout-btn").on("click", function() {
        prepareOrderModal();
    });

    // Siparişi gönder
    $("#confirm-order").on("click", function() {
        const addressId = $("#deliveryAddress").val();
        if (!addressId) {
            alert("Lütfen teslimat adresi seçin!");
            return;
        }
        createOrder(addressId);
    });
});

function prepareOrderModal() {
    const token = localStorage.getItem("token");
    if (!token) {
        alert("Sipariş vermek için giriş yapmalısınız!");
        window.location.href = "/login";
        return;
    }

    const cart = JSON.parse(localStorage.getItem("cart") || "[]");
    if (cart.length === 0) {
        alert("Sepetiniz boş!");
        return;
    }

    let totalAmount = 0;
    cart.forEach(item => {
        totalAmount += item.price * item.quantity;
    });

    $("#orderTotalAmount").text(totalAmount.toFixed(2));

    loadUserProfile(() => {
        const balance = $("#orderCurrentBalance").text();
        if (totalAmount > balance) {
            $("#insufficientBalanceWarning").show();
            $("#confirm-order").prop("disabled", true);
        } else {
            $("#insufficientBalanceWarning").hide();
            $("#confirm-order").prop("disabled", false);
        }

        loadUserAddresses();
        $("#orderConfirmModal").modal("show");
    });
}

function loadUserProfile(callback) {
    const token = localStorage.getItem("token");
    $.ajax({
        url: "http://localhost:8282/api/user/profile",
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token
        },
        success: function(response) {
            if (response.success) {
                $("#orderCurrentBalance").text(response.data.balance);
                callback();
            } else {
                alert("Kullanıcı bilgisi alınamadı.");
            }
        },
        error: function() {
            alert("Kullanıcı profili alınırken hata oluştu.");
        }
    });
}

function loadUserAddresses() {
    const token = localStorage.getItem("token");
    $.ajax({
        url: "http://localhost:8282/api/user/addresses",
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token
        },
        success: function(response) {
            if (response.success) {
                const select = $("#deliveryAddress");
                select.empty();
                select.append('<option value="">Adres seçiniz</option>');
                response.data.forEach(addr => {
                    select.append(`<option value="${addr.id}">${addr.title} - ${addr.address}</option>`);
                });
            }
        },
        error: function() {
            alert("Adresler alınırken hata oluştu.");
        }
    });
}

function createOrder(addressId) {
    const token = localStorage.getItem("token");
    const orderRequest = { addressId: addressId };

    showLoadingOverlay();

    fetch("http://localhost:8282/api/orders", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(orderRequest)
    })
        .then(res => {
            if (!res.ok) throw new Error("Sipariş oluşturulamadı.");
            return res.json();
        })
        .then(data => {
            if (data.success) {
                localStorage.setItem("cart", "[]");
                loadCart();
                $("#orderConfirmModal").modal("hide");
                alert("Sipariş başarıyla oluşturuldu!");
            } else {
                alert(data.message || "Sipariş başarısız.");
            }
        })
        .catch(err => {
            alert("Hata oluştu: " + err.message);
        })
        .finally(() => {
            hideLoadingOverlay();
        });
}

function showLoadingOverlay() {
    $("#loading-overlay").css("display", "flex");
}

function hideLoadingOverlay() {
    $("#loading-overlay").css("display", "none");
}