let stompClient = null;

// WebSocket을 통한 실시간 주문 확인
function connect() {
    const socket = new SockJS('/websocket-endpoint'); // WebSocket 연결
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        // 서버로부터 실시간 주문 정보를 받음
        stompClient.subscribe('/topic/new-order', function (message) {
            const orders = JSON.parse(message.body);
            displayNewOrderAlerts(orders); // 받은 주문 데이터를 처리
        });
    });
}

// 실시간 주문 알림 표시 함수
function displayNewOrderAlerts(orders) {
    const orderAlertList = document.getElementById('orderAlertList');
    orderAlertList.innerHTML = ''; // 기존 알림 초기화

    if (orders.length > 0) {
        orders.forEach(order => {
            const alertDiv = document.createElement('div');
            alertDiv.classList.add('alert', 'alert-warning', 'd-flex', 'justify-content-between');
            alertDiv.innerHTML = `
                <span>새 주문이 들어왔습니다! 주문번호: ${order.kitOrderId}</span>
                <button class="btn btn-sm btn-primary" onclick="confirmOrder('${order.kitOrderId}')">확인</button>
            `;
            orderAlertList.appendChild(alertDiv);
        });
    } else {
        orderAlertList.innerHTML = '<p>대기 중인 주문이 없습니다.</p>';
    }
}

// 주문 확인 버튼 클릭 시 주문 정보를 테이블에 추가
function confirmOrder(kitOrderId) {
    fetch(`/distribution/confirm-order?kitOrderId=${encodeURIComponent(kitOrderId)}`)
        .then(response => response.json())
        .then(order => {
            if (order) {
                const orderTableBody = document.getElementById('orderTableBody');
                const newRow = document.createElement('tr');
                newRow.innerHTML = `
                    <td>${order.kitOrderId}</td>
                    <td>${order.mealkitName}</td>
                    <td>${order.quantity}</td>
                    <td>${order.orderDate}</td>
                    <td>${order.status}</td>
                `;
                orderTableBody.appendChild(newRow);

                // 주문 알림 제거
                const alertDiv = document.querySelector(`button[onclick="confirmOrder('${order.kitOrderId}')"]`).parentNode;
                document.getElementById('orderAlertList').removeChild(alertDiv);
            }
        })
        .catch(error => console.error('Error confirming order:', error));
}

// 페이지 로드 시 WebSocket 연결 시작
window.onload = function() {
    connect(); // WebSocket 연결
};
