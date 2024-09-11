/* Init Process - 페이지 처음 방문시 필요한거 전부 초기화 */
$(document).ready(function () {
    // 생산품 등록의 재료 리스트 초기화 ( 이미 등록된건 안나옴 )
    updateSourceSelectList();

    // 제일 처음 보이는 테이블 초기화 ( 현재 설정: 생산품 등록 테이블 )
    getCompanySourceTable();

    // 차트 초기화 아무 값 없는 빈 차트
    initChart(chartType);
    initOrderChart(oderChartType);

    // 위에서 초기화한 차트에 첫번째 값 넣음 이거 사용해서 계속 값 업데이트 해주면됨
    updateWarehouseChart();
    updateOrderChart();

    // 테이블 불러오고 숨김
    // 처음 페이지에 접속할때 초기화 하는 이유: 테이블 나중에 초기화하면 화면이 새로고침 비슷하게 되는 버그?가 있음.. @@:로딩 오래걸리면 로직 원래대로 변경
    getWarehouseTable();
    $('#warehouseTableContainer').hide();
    getOrderTable();
    $('#orderTableContainer').hide();
});

/* Produce Process - 생산 현황 테이블 스크립트 */
$(document).ready(function () {
    // 생산품 등록 모달 재료 입력 스크립트 ( 셀렉트나 인풋텍스트 중 하나만 할 수 있게 해줌 )
    $('#addSourceSelect').on('change', function () {
        if ($(this).val() !== "directInput") {
            $('#addSourceInput').val('').prop('disabled', true);
            $('#addSourceSelect').prop('disabled', false);
        } else {
            $('#addSourceInput').prop('disabled', false).focus();
        }
    });

    // 생산품 등록 스크립트( 생산품 등록하고 업데이트된 리스트 가져옴 )
    $('#addSourceBtn').on('click', function () {
        let data = {
            sourcePrice: $('#sourcePrice').val(),
            sourceId: $('#addSourceInput').prop('disabled') ? $('#addSourceSelect').val() : null,
            sourceName: $('#addSourceInput').prop('disabled') ? null : $('#addSourceInput').val()
        };

        $('#addSourceInput').val('').prop('disabled', false);
        $('#addSourceSelect').val('directInput');

        $.ajax({
            url: '/api/product/company/add',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data),
            beforeSend: function (xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);  // CSRF 헤더와 토큰을 함께 보냄
            },
            success: function () {
                $('#addSourceModal').modal('hide');
                $('#companySourceTable').DataTable().ajax.reload(null, false);
                updateSourceSelectList();
                showToast("등록 완료")
            },
            error: function (xhr) {
                const response = JSON.parse(xhr.responseText);
                if (xhr.status === 400) alert(response.message);
                else alert('알 수 없는 에러가 발생');
            }
        });
    });

    // 생산 모달창 -> 갯수 입력후 생산 로직 수행 @@: 이거 여기에 있는게 나은거같음
    $('#companySourceTable').on('click', 'button[data-action="produce"]', function () {
        const row = $(this).closest('tr');
        const data = $('#companySourceTable').DataTable().row(row).data();

        $('#produceSourceName').val(data.sourceName);
        $('#produceSourcePrice').val(data.sourcePrice);
        $('#sourcePriceId').val(data.companySourceId);
        $('#produceSourceModal').modal('show');
    });
    $('#produceSourceBtn').on('click', function () {
        const sourceQuantity = $('#sourceQuantity').val();
        const sourcePriceId = $('#sourcePriceId').val();
        const sourceName = $('#produceSourceName').val();

        const data = {
            sourceQuantity: sourceQuantity,
            sourcePriceId: sourcePriceId
        };
        $.ajax({
            url: '/api/product/company/produce',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data),
            beforeSend: function (xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);  // CSRF 헤더와 토큰을 함께 보냄
            },
            success: function () {
                showToast("입고 요청이 완료 되었습니다: " + sourceName + " " + sourceQuantity + " 개");
                $('#companySourceTable').DataTable().ajax.reload(null, false);
                updateWarehouseChart();
                $('#produceSourceModal').modal('hide');
                setTimeout(function () {
                    $('#sourceQuantity').val(10);
                }, 500);
            },
            error: function (xhr) {
                const response = JSON.parse(xhr.responseText);
                if (xhr.status === 400) alert(response.message);
                else alert('알 수 없는 에러가 발생');
            }
        });
    });

    // 생산 품목 삭제 ( 삭제 로직 @@:시간나면 수정 )
    $('#companySourceTable').on('click', 'button[data-action="delete"]', function () {
        const row = $(this).closest('tr');
        const data = $('#companySourceTable').DataTable().row(row).data();
        const companySourceId = data.companySourceId;

        if (confirm('정말로 이 항목을 삭제하시겠습니까?\n *참조키 있으면 삭제 안됨: 수정 예정*')) {
            $.ajax({
                url: `/api/product/company/add/${companySourceId}`,
                type: 'DELETE',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(csrfHeader, csrfToken);  // CSRF 헤더와 토큰을 함께 보냄
                },
                success: function () {
                    $('#companySourceTable').DataTable().ajax.reload(null, false);
                    showToast("삭제 완료")
                },
                error: function (xhr) {
                    const response = JSON.parse(xhr.responseText);
                    if (xhr.status === 409) alert(response.message);
                    else alert('삭제 실패함 원인은 모름~');
                }
            });
        }
    });

    // 생산 품목 업데이트 ( 가격 수정 로직 )
    $('#companySourceTable').on('click', 'button[data-action="update"]', function () {
        const row = $(this).closest('tr');
        const data = $('#companySourceTable').DataTable().row(row).data();

        $('#updateSourceName').val(data.sourceName);
        $('#updateSourcePrice').val(data.sourcePrice);
        $('#oldPrice').val(data.sourcePrice);
        $('#updateCompanySourceId').val(data.companySourceId);
        $('#updateSourceModal').modal('show');
    });
    $('#updateSourceBtn').on('click', function () {
        const sourcePrice = $('#updateSourcePrice').val();
        const companySourceId = $('#updateCompanySourceId').val();
        const oldPrice = $('#oldPrice').val();

        const data = {
            sourcePrice: sourcePrice,
            oldPrice: oldPrice
        };
        $.ajax({
            url: `/api/product/company/add/${companySourceId}`,
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(data),
            beforeSend: function (xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);  // CSRF 헤더와 토큰을 함께 보냄
            },
            success: function () {
                $('#updateSourceModal').modal('hide');
                showToast('생산품 가격 수정이 완료되었습니다.');
                $('#companySourceTable').DataTable().ajax.reload(null, false);
            },
            error: function (xhr) {
                const response = JSON.parse(xhr.responseText);
                if (xhr.status === 400) alert(response.message);
                else alert('알 수 없는 에러가 발생');
            }
        });
    });

});

/* Order Process - 주문 현황 테이블 스크립트 */
$(document).ready(function () {
    $('.orderSearch').on('change', function () {
        $('#orderTable').DataTable().ajax.reload(function () {
        }, true);
    });

    $('#orderTable').on('click', 'button[data-action="orderProcessBlock"]', function () {
        alert("출하 불가: 현재 상품의 재고가 부족함");
    });
    // 주문 처리, 주문 상태 변경하고 창고에서 주문받은 재료 갯수만큼 출고하는 로직 수행
    $('#orderTable').on('click', 'button[data-action="orderProcess"]', function () {
        const row = $(this).closest('tr');
        const data = $('#orderTable').DataTable().row(row).data();

        console.log(data);

        $('#orderIdView').val(data.orderId.substring(0, 8));
        $('#orderId').val(data.orderId);
        $('#orderSourceName').val(data.sourceName);
        $('#orderSourcePrice').val(data.sourcePrice);
        $('#orderQuantity').val(data.quantity);
        $('#orderTotalPrice').val(data.totalPrice);
        $('#orderDate').val(data.orderDate);
        $('#orderSourcePriceId').val(data.sourcePriceId);
        $('#orderStockBalance').val(data.stockBalance);

        $('#orderProcessModal').modal('show');
    });
    $('#orderProcessBtn').on('click', function () {
        const orderId = $('#orderId').val();
        // const orderStatus = $('#orderStatusSelect').val();
        const sourcePriceId = $('#orderSourcePriceId').val();
        const orderQuantity = $('#orderQuantity').val();
        const stockBalance = $('#orderStockBalance').val();

        const data = {
            orderId: orderId,
            orderStatus: 5,
            sourceQuantity: orderQuantity,
            sourcePriceId: sourcePriceId,
            stockBalance: stockBalance
        };

        $.ajax({
            url: '/api/product/company/order',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data),
            beforeSend: function (xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);  // CSRF 헤더와 토큰을 함께 보냄
            },
            success: function () {
                $('#orderProcessModal').modal('hide');
                showToast("주문 번호: '" + orderId.substring(0, 8) + "' 의 출하가 완료되었습니다.")
                updateWarehouseChart();
                $('#orderTable').DataTable().ajax.reload(function () {
                }, false);
            },
            error: function (xhr) {
                $('#orderProcessModal').modal('hide');
                const response = JSON.parse(xhr.responseText);
                if (xhr.status === 400) alert(response.message);
                if (xhr.status === 409) {
                    $('#orderTable').DataTable().ajax.reload(null, false);
                    alert(response.message);
                }
                else alert('알 수 없는 에러가 발생');
            }
        });
    });
});

/* DropDown Process - 드롭다운 메뉴 항목 선택 시 이벤트 처리 */
$(document).ready(function () {
    // 차트 타입 변경
    $('.warehouse-dropdown-item').on('click', function () {
        const selectedType = $(this).data('value'); // 선택된 차트 타입 가져오기
        const selectedName = $(this).data('name'); // 선택된 차트 타입 가져오기
        $('#warehouseChartDropdown').text(selectedName);
        chartType = selectedType; // 선택된 차트 타입 저장
        initChart(chartType); // 새로운 차트 타입으로 초기화
        updateWarehouseChart(); // 새로운 차트에 데이터 업데이트
    });

    $('#chartMonthSelect').on('change', function (){
        orderChartMonthOption = $('#chartMonthSelect').val();
        updateOrderChart();
    })

    // 테이블 변경
    $('.table-dropdown-item').on('click', function () {
        $('#companySourceEditCheckBox').prop('checked', false);

        const selectedValue = $(this).data('value');
        const selectedName = $(this).data('name');

        $('#tableDropDown').text(selectedName);

        if (selectedValue === 'companySourceList') {
            $('#companySourceTableContainer').show();
            $('#warehouseTableContainer').hide();
            $('#orderTableContainer').hide();
            $('#companySourceTable').DataTable().ajax.reload(null, false);
        } else if (selectedValue === 'warehouseList') {
            $('#companySourceTableContainer').hide();
            $('#orderTableContainer').hide();
            $('#warehouseTableContainer').show();
            $('#warehouseTable').DataTable().ajax.reload(null, false);
            // }
        } else if (selectedValue === 'orderList') {
            $('#warehouseTableContainer').hide();
            $('#companySourceTableContainer').hide();
            $('#orderTableContainer').show();
            $('#orderTable').DataTable().ajax.reload(null, false);
        }
    });
})

/* 여기부턴 전역함수, 전역변수 */
/* 여기부턴 전역함수, 전역변수 */
/* 여기부턴 전역함수, 전역변수 */

// csrf: ajax 로 post,put,delete 요청 하려면 csrfToken, csrfHeader 담아서 같이 보내야함
const csrfToken = $('meta[name="_csrf"]').attr('content');
const csrfHeader = $('meta[name="_csrf_header"]').attr('content');

/* 테이블 리스트 초기화 */

function getCompanySourceTable() {
    $('#companySourceTable').DataTable({
        ajax: {
            url: '/api/product/company/add',
            dataSrc: 'companySourceList'
        },
        columns: [
            {data: null, render: (data, type, row, meta) => meta.row + 1},
            // {data: 'companySourceId', render: data => data.substring(0, 8)},
            {data: 'sourceName'},
            {data: 'sourcePrice'},
            {data: 'totalQuantity'},
            {
                data: null,
                orderable: false,
                render: function () {
                    return `
                            <div class="d-flex justify-content-between">
                            <div>
                            <button type="button" class="btn btn-outline-primary btn-sm" data-action="produce">재고 등록</button>
                            </div>
                            <div>
                            <button type="button" class="btn btn-outline-info btn-sm" data-action="update">가격 수정</button>
                            <button type="button" class="btn btn-outline-danger btn-sm edit-btn" data-action="delete">삭제</button>
                            </div>
                            </div>
                        `;
                }
            }
        ],
        language: {emptyTable: '데이터가 없습니다.'},
        lengthMenu: [[5, 10, 25, 50], [5, 10, 25, 50]], pageLength: 10
    });
}

function getWarehouseTable() {
    $('#warehouseTable').DataTable({
        ajax: {
            url: '/api/product/company/produce',
            dataSrc: ''
        },
        columns: [
            {data: 'rowNum'},
            // {data: 'sourceWarehouseId', render: data => data.substring(0, 8)},
            {data: 'sourceName'},
            {data: 'sourceQuantity'},
            {data: 'type'},
            {data: 'produceDate'}
        ],
        order: [[0, 'desc']],
        language: {emptyTable: '데이터가 없습니다.'},
        lengthMenu: [[5, 10, 25, 50], [5, 10, 25, 50]], pageLength: 10,
        initComplete: function () {
            $('#warehouseTable').css({
                'width': '100%',
                'table-layout': 'fixed'
            });
        }
    });
}

function getOrderTable() {
    $('#orderTable').DataTable({
        ajax: {
            url: '/api/product/company/order',
            data: function (d) {
                // d.orderMonthOption = $('#orderMonthSearchSelect').val() || "all"; // 기본값은 "all"
                d.statusId = $('#orderStatusSearchSelect').val();
                d.sourceName = $('#sourceNameSearchSelect').val() || "all";
                return $.param(d);
            },
            dataSrc: ''
        },
        columns: [
            {data: 'rowNum'},
            {data: 'orderId', render: data => data.substring(0, 8)},
            {data: 'sourceName'},
            {data: 'sourcePrice'},
            {data: 'quantity'},
            {data: 'totalPrice'},
            {data: 'orderDate'},
            {
                data: 'orderStatus',
                render: function (data) {
                    return data === '처리전' ? '주문접수' : '완료';
                }
            },
            {
                data: null,
                render: function (data, type, row) {
                    let buttons = '';
                    //`<button type="button" class="btn btn-primary btn-sm" data-action="produce">생산</button>`;

                    // 'orderStatus'가 '처리전'일 때만 출하 버튼을 활성화
                    if (row.orderStatus === '처리전') {
                        // checkStockAmount가 0 이상일 때만 출하 로직 활성화
                        if (row.stockBalance - row.quantity < 0) {
                            buttons += `<button type="button" class="btn btn-outline-warning btn-sm" data-action="orderProcessBlock">재고부족</button>`;
                        } else {
                            buttons += `<button type="button" class="btn btn-outline-primary btn-sm" data-action="orderProcess">출하</button>`;
                        }
                    }
                    return buttons;
                }
            }
        ],
        order: [[0, 'desc']],
        language: {emptyTable: '데이터가 없습니다.'},
        lengthMenu: [[5, 10, 25, 50], [5, 10, 25, 50]], pageLength: 10,
        initComplete: function () {
            $('#orderTable').css({
                'width': '100%',
                'table-layout': 'fixed'
            });
        }
    });
    $.ajax({
        url: '/api/product/company/allCompanySources', // 소스 이름을 가져오는 API 호출
        type: 'GET',
        success: function(response) {
            const sourceNames = response; // List<String> 형태의 소스 이름
            const $sourceSelect = $('#sourceNameSearchSelect');

            // 소스 이름을 셀렉트 박스에 추가
            sourceNames.forEach(function(sourceName) {
                $sourceSelect.append(new Option(sourceName, sourceName));
            });
        },
        error: function() {
            console.error('소스 이름을 가져오는 중 오류 발생');
        }
    });
    // $('#orderTable_filter').css('display', 'flex').addClass('justify-content-end');
    $('#orderTable_filter').append(`
        <select id="orderStatusSearchSelect" class="form-select form-select-sm orderSearch" style="display:inline-block; width:auto;">
            <option value="" selected>주문 상태</option>
            <option value="1">주문</option>
            <option value="2">완료</option>
        </select>
        <select id="sourceNameSearchSelect" class="form-select form-select-sm orderSearch" style="display:inline-block; width:auto;">
            <option value="all" selected>생산품별</option>
        </select>
    `);
}

/* 테이블 리스트 초기화 끝 */

/* 차트 관련 */

let warehouseChart; // 전역 변수로 차트 객체를 선언
let orderChart; // 전역 변수로 차트 객체를 선언
let chartType = 'bar'; // 기본 차트 타입은 'bar'
let oderChartType = 'bar'; // 기본 차트 타입은 'bar'
let orderChartMonthOption = '';

function updateWarehouseChart() {
    $.ajax({
        url: '/api/product/company/chart', // 창고 데이터를 가져오는 URL
        type: 'GET',
        contentType: 'application/json',
        success: function (response) {
            const labels = [];
            const data = [];

            // response 데이터를 순회하며 필요한 정보 추출
            response.forEach(function (item) {
                labels.push(item.sourceName); // sourceName을 labels로 사용
                data.push(item.sourceQuantity); // totalQuantity 값을 data로 사용
            });

            warehouseChart.data.labels = labels;
            warehouseChart.data.datasets[0].data = data;
            warehouseChart.data.datasets[0].backgroundColor = colors.slice(0, data.length);
            warehouseChart.update();
        },
        error: function (xhr, status, error) {
            console.error('차트 데이터 가져오는데 실패함:', error);
        }
    });
}

function updateOrderChart() {
    $.ajax({
        url: '/api/product/company/orderChart?month='+orderChartMonthOption, // 창고 데이터를 가져오는 URL
        contentType: 'application/json',
        success: function (response) {
            const labels = [];
            const data = [];

            // response 데이터를 순회하며 필요한 정보 추출
            response.forEach(function (item) {
                labels.push(item.sourceName);
                data.push(-item.sales); // 판매량
            });

            orderChart.data.labels = labels;
            orderChart.data.datasets[0].data = data;
            orderChart.data.datasets[0].backgroundColor = colors.slice(0, data.length);
            orderChart.update();
        },
        error: function (xhr, status, error) {
            console.error('차트 데이터 가져오는데 실패함:', error);
        }
    });
}

// 차트 초기화 함수, 차트의 타입을 동적으로 받음, 차트 두개 띄우려면 아이디랑 차트 객체 파라미터로 받으면 될듯?

function initChart(type) {
    const ctx = $('#warehouseChart')[0].getContext('2d');

    // 기존 차트가 있으면 삭제하고 새로 생성
    if (warehouseChart) {
        warehouseChart.destroy(); // 기존 차트 삭제
    }

    warehouseChart = new Chart(ctx, {
        type: type, // 동적으로 받은 차트 타입
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

function initOrderChart(type) {
    const ctx = $('#orderChart')[0].getContext('2d');

    // 기존 차트가 있으면 삭제하고 새로 생성
    if (orderChart) {
        orderChart.destroy(); // 기존 차트 삭제
    }

    orderChart = new Chart(ctx, {
        type: type, // 동적으로 받은 차트 타입
        data: {
            labels: [], // 초기 레이블
            datasets: [{
                label: '총 판매량',
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

/* 차트 관련 끝 */

// 소스 리스트 업데이트 ( SOURCE 에 있는 재료중 SOURCE_PRICE 엔 없는거 셀렉트에 추가해줌 )
function updateSourceSelectList() {
    $.ajax({
        url: '/api/product/company/add',
        type: 'GET',
        contentType: 'application/json',
        success: function (response) {
            const sources = response.sources;
            const selectBox = $('#addSourceSelect');

            selectBox.empty();
            selectBox.append($('<option>', {value: 'directInput', text: '직접입력'}));

            sources.forEach(function (source) {
                const option = $('<option>', {value: source.sourceId, text: source.sourceName});
                selectBox.append(option);
            });
        },
        error: function (xhr, status, error) {
            console.error('Error occurred while fetching sources:', error);
        }
    });
}

const colors = [
    '#E57373', '#F06292', '#BA68C8', '#64B5F6', '#4FC3F7',
    '#4DD0E1', '#4DB6AC', '#81C784', '#AED581', '#DCE775',
    '#FFD54F', '#FFB74D', '#FF8A65', '#A1887F', '#90A4AE',
    '#FF6F61', '#6D4C41', '#5C6BC0', '#EF5350', '#AB47BC',
    '#7E57C2', '#42A5F5', '#29B6F6', '#26C6DA', '#1E88E5',
    '#039BE5', '#0288D1', '#0277BD', '#01579B', '#C2185B',
    '#D81B60', '#E91E63', '#F06292', '#EC407A', '#AB47BC',
    '#8E24AA', '#7B1FA2', '#6D1B9D', '#4A148C', '#2C6C9A',
    '#00838F', '#00695C', '#004D40', '#004D40', '#1B5E20'
];