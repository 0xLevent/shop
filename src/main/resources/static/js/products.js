console.log("✅ products.js yüklendi");

document.addEventListener("DOMContentLoaded", () => {
    console.log("🚀 DOMContentLoaded tetiklendi");

    const itemList = document.getElementById("item-list");
    const loading = document.getElementById("loading");
    const error = document.getElementById("error");

    checkAuthStatus();

    async function fetchItems() {
        try {
            loading.style.display = "block";
            error.style.display = "none";
            itemList.innerHTML = "";

            const response = await fetch("http://localhost:8282/api/items");
            if (!response.ok) throw new Error("API isteği başarısız: " + response.status);

            const items = await response.json();
            console.log("🧪 Gelen ürün verisi:", items); // ürünleri kontrol etmek için

            items.forEach((item) => {
                console.log(`🧾 Ürün: ${item.name}, ID: ${item.id}`); // ← ürün ID’leri net görünsün

                const li = document.createElement("li");

                const nameSpan = document.createElement("span");
                nameSpan.textContent = item.name;

                const priceSpan = document.createElement("span");
                priceSpan.textContent = item.price + " TL";

                const stockSpan = document.createElement("span");
                stockSpan.id = `stock-${item.id}`;
                stockSpan.textContent = "Stok: " + item.stock;

                const button = document.createElement("button");
                button.textContent = "Sepete Ekle";
                button.addEventListener("click", () => {
                    console.log(`🛒 addToCart çağrılıyor... itemId=${item.id}`);
                    addToCart(item.id, 1);
                });

                li.appendChild(nameSpan);
                li.appendChild(priceSpan);
                li.appendChild(stockSpan);
                li.appendChild(button);

                itemList.appendChild(li);
            });

        } catch (err) {
            error.style.display = "block";
            error.textContent = "Ürünler yüklenirken bir hata oluştu: " + err.message;
            console.error(err);
        } finally {
            loading.style.display = "none";
        }
    }

    fetchItems();
});

function checkAuthStatus() {
    const token = localStorage.getItem("token");
    const loginStatusElement = document.getElementById("login-status");

    if (token) {
        console.log("User is logged in with token");
        if (loginStatusElement) {
            loginStatusElement.textContent = "Giriş yapıldı";
            loginStatusElement.className = "logged-in";
        }
    } else {
        console.log("User is not logged in");
        if (loginStatusElement) {
            loginStatusElement.textContent = "Giriş yapılmadı";
            loginStatusElement.className = "logged-out";
        }
    }
}

async function login(email, password) {
    try {
        const response = await fetch("http://localhost:8282/api/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ email, password })
        });

        const data = await response.json();

        if (response.ok && data.success) {
            localStorage.setItem("token", data.data.token);
            console.log("Login successful, token stored");
            checkAuthStatus();
            return true;
        } else {
            console.error("Login failed:", data.message);
            alert("Login failed: " + data.message);
            return false;
        }
    } catch (error) {
        console.error("Login error:", error);
        alert("Login error: " + error.message);
        return false;
    }
}

async function addToCart(itemId, quantity) {
    try {
        const token = localStorage.getItem("token");
        console.log("Token from localStorage:", token ? "Found" : "Not found");

        if (!token) {
            alert("Sepete ürün eklemek için lütfen giriş yapın");
            return;
        }

        const requestBody = { itemId, quantity };
        console.log("Sending request body:", requestBody);

        const response = await fetch("http://localhost:8282/api/cart/add", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(requestBody),
        });

        console.log("Cart add response status:", response.status);

        if (!response.ok) {
            const errorText = await response.text();
            console.error("Server error response:", errorText);
            throw new Error("Sepete ekleme başarısız: " + response.status);
        }

        alert("Ürün sepete eklendi");

        const stockElement = document.getElementById(`stock-${itemId}`);
        if (stockElement) {
            const currentStock = parseInt(stockElement.textContent.replace("Stok: ", ""), 10);
            stockElement.textContent = `Stok: ${currentStock - quantity}`;
        }

    } catch (error) {
        console.error("Sepete ekleme hatası:", error);
        alert("Ürün sepete eklenirken bir hata oluştu: " + error.message);
    }
}

async function logout() {
    localStorage.removeItem("token");
    checkAuthStatus();
    alert("Çıkış yapıldı");
}

window.addToCart = addToCart;
window.login = login;
window.logout = logout;

window.addEventListener("click", () => {
    console.log("🖱️ Sayfada bir şey tıklandı");
});
