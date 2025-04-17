class WebSocketManager {
    constructor(serverUrl) {
        this.serverUrl = serverUrl;
        this.stompClient = null;
        this.socket = null;
        this.reconnectAttempts = 0;
        this.maxReconnectAttempts = 5;
        this.reconnectDelay = 2000;
        this.topicHandlers = new Map();
        this.connected = false;
        this.connectionPromise = null;
        this.connectCallbacks = [];
    }

    connect() {
        if (this.connectionPromise) {
            return this.connectionPromise;
        }

        this.connectionPromise = new Promise((resolve, reject) => {
            try {
                console.log("WebSocket sunucusuna bağlanılıyor...");
                this.socket = new SockJS(this.serverUrl);
                this.stompClient = Stomp.over(this.socket);

                // Debug loglarını devre dışı bırak
                this.stompClient.debug = null;

                this.stompClient.connect(
                    {},
                    () => {
                        this.connected = true;
                        this.reconnectAttempts = 0;
                        console.log("WebSocket bağlantısı kuruldu!");

                        // Tüm topic'lere yeniden abone ol
                        this.topicHandlers.forEach((handler, topic) => {
                            this._subscribeToTopic(topic, handler);
                        });

                        // Bağlantı callback'lerini çalıştır
                        this.connectCallbacks.forEach(callback => callback());

                        resolve(this.stompClient);
                    },
                    (error) => {
                        console.error("WebSocket bağlantı hatası:", error);
                        this.connected = false;
                        this.connectionPromise = null;
                        this.onError(error);
                        reject(error);
                    }
                );
            } catch (error) {
                console.error("WebSocket bağlantısı kurulamadı:", error);
                this.connectionPromise = null;
                reject(error);
            }
        });

        return this.connectionPromise;
    }

    onError(error) {
        console.error("WebSocket hatası:", error);
        this.connected = false;
        this.connectionPromise = null;

        if (this.reconnectAttempts < this.maxReconnectAttempts) {
            setTimeout(() => this.reconnect(), this.reconnectDelay);
        } else {
            console.error(`Maksimum yeniden bağlanma denemesi (${this.maxReconnectAttempts}) aşıldı.`);
            this.showConnectionError();
        }
    }

    showConnectionError() {
        const errorElement = document.getElementById("error");
        if (errorElement) {
            errorElement.textContent = "Sunucuya bağlanılamıyor. Lütfen sayfayı yenileyin.";
            errorElement.style.display = "block";
        }
    }

    /**
     * Attempt to reconnect to the WebSocket server
     */
    reconnect() {
        if (!this.connected) {
            this.reconnectAttempts++;
            console.log(`Reconnection attempt ${this.reconnectAttempts}/${this.maxReconnectAttempts}...`);
            this.connect();
        }
    }

    /**
     * Disconnect from the WebSocket server
     */
    disconnect() {
        if (this.stompClient && this.connected) {
            this.stompClient.disconnect();
            this.connected = false;
            this.connectionPromise = null;
            console.log("Disconnected from WebSocket server");
        }
    }

    /**
     * Add a callback to be executed when connection is established
     * @param {Function} callback Function to execute on connection
     */
    onConnect(callback) {
        if (typeof callback === 'function') {
            this.connectCallbacks.push(callback);
            if (this.connected) {
                callback();
            }
        }
    }


    subscribe(topic, handler) {
        // Store the handler for this topic
        this.topicHandlers.set(topic, handler);

        // If already connected, subscribe immediately
        if (this.connected && this.stompClient) {
            return Promise.resolve(this._subscribeToTopic(topic, handler));
        }

        // If not connected, connect first then subscribe
        return this.connect().then(() => {
            return this._subscribeToTopic(topic, handler);
        });
    }


    _subscribeToTopic(topic, handler) {
        console.log(`Subscribing to ${topic}`);
        const subscription = this.stompClient.subscribe(topic, (message) => {
            try {
                const data = JSON.parse(message.body);
                handler(data);
            } catch (error) {
                console.error(`Error handling message from ${topic}:`, error);
            }
        });
        return subscription;
    }


    sendMessage(destination, data) {
        return this.connect().then(() => {
            this.stompClient.send(destination, {}, JSON.stringify(data));
            console.log(`Message sent to ${destination}:`, data);
        });
    }


    isConnected() {
        return this.connected;
    }
}

const websocketManager = new WebSocketManager("http://localhost:8282/ws");

function handleStockUpdate(data) {
    console.log("Stock update received:", data);

    const productElement = document.querySelector(`[data-product="${data.productName}"]`);
    if (productElement) {
        const stockElement = productElement.querySelector('.stock-count');
        if (stockElement) {
            stockElement.textContent = data.availableStock;

            productElement.classList.add('stock-updated');
            setTimeout(() => {
                productElement.classList.remove('stock-updated');
            }, 2000);
        }

        showNotification(`${data.productName} stock updated to ${data.availableStock}`);
    }
}

function handleOrderStatus(data) {
    console.log("Order status update received:", data);

    const orderElement = document.querySelector(`[data-order-id="${data.orderId}"]`);
    if (orderElement) {
        const statusElement = orderElement.querySelector('.order-status');
        if (statusElement) {
            statusElement.textContent = data.status;
            orderElement.className = `order-item status-${data.status.toLowerCase()}`;
        }
    }

    showNotification(`Order #${data.orderId}: ${data.status} - ${data.message}`);
}

function showNotification(message, type = 'info') {
    let notificationContainer = document.getElementById('notification-container');
    if (!notificationContainer) {
        notificationContainer = document.createElement('div');
        notificationContainer.id = 'notification-container';
        document.body.appendChild(notificationContainer);
    }

    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.textContent = message;

    notificationContainer.appendChild(notification);

    setTimeout(() => {
        notification.classList.add('fade-out');
        setTimeout(() => {
            notification.remove();
        }, 500);
    }, 5000);
}

document.addEventListener('DOMContentLoaded', function() {
    // Subscribe to topics
    websocketManager.subscribe('/topic/stockUpdates', handleStockUpdate);
    websocketManager.subscribe('/topic/orderUpdates', handleOrderStatus);

    // Add a visual indicator that real-time updates are active
    websocketManager.onConnect(() => {
        const statusIndicator = document.createElement('div');
        statusIndicator.id = 'realtime-status';
        statusIndicator.className = 'realtime-active';
        statusIndicator.innerHTML = '<span class="pulse"></span> Real-time updates active';

        document.body.appendChild(statusIndicator);
    });
});

window.addEventListener('beforeunload', function() {
    websocketManager.disconnect();
});