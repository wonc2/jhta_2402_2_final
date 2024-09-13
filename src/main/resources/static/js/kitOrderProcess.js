let warehouseStockChart;


function initChart() {
    const ctx = $('#logisticsWarehouseStockChart')[0].getContext('2d');

    warehouseStockChart = new Chart(ctx, {
        type: 'bar', // 동적으로 받은 차트 타입
        data: {
            labels: [], // 초기 레이블
            datasets: [{
                label: '총 재고량',
                data: [],
                backgroundColor: [],
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

function updateWarehouseChart() {
    $.ajax({
        url: '/api/distribution/warehouseStockChart', // 창고 데이터를 가져오는 URL
        type: 'GET',
        contentType: 'application/json',
        success: function (response) {
            const labels = [];
            const data = [];

            // response 데이터를 순회하며 필요한 정보 추출
            response.forEach(function (item) {
                labels.push(item.sourceName); // sourceName을 labels로 사용
                data.push(item.sourceQuantity); // sourceQuantity 값을 data로 사용
            });

            warehouseStockChart.data.labels = labels;
            warehouseStockChart.data.datasets[0].data = data;
            warehouseStockChart.data.datasets[0].backgroundColor = colors.slice(0, data.length);
            warehouseStockChart.update();
        },
        error: function (xhr, status, error) {
            console.error('차트 데이터 가져오는데 실패함:', error);
        }
    });
}