$(document).ready(function () {
    // 페이지 초기화
    initPage();

    // 생산품 관리 및 신규 생산품목 등록
    addSourceProcess();

    // 주문 처리
    orderProcess();

    // 차트 타입, 테이블 변경, 버튼 감추기 등 드롭다운 및 버튼 이벤트 관리
    pageEventHandler();
});

// csrf 설정
const csrfToken = $('meta[name="_csrf"]').attr('content');
const csrfHeader = $('meta[name="_csrf_header"]').attr('content');

// 주문 처리 테이블 초기화
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
            {data: 'quantity'},
            {
                data: 'totalPrice',
                render: function (data) {
                    return data.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + '₩';
                }
            },
            {data: 'orderDate'},
            {
                data: 'orderStatus',
                render: function (data) {
                    return (data === 1 || data === 2) ? '주문접수' : '완료';
                }
            },
            {
                data: null,
                render: function (data, type, row) {
                    let buttons = '';

                    if (row.orderStatus === 1) {
                        if (row.stockBalance - row.quantity < 0) {
                            buttons += `<button type="button" class="btn btn-outline-warning btn-sm" data-action="orderProcessBlock">재고부족</button>`;
                        } else {
                            buttons += `<button type="button" class="btn btn-outline-primary btn-sm" data-action="orderProcess">출하</button>`;
                        }
                    } else if (row.orderStatus === 2) {
                        buttons += `<button type="button" class="btn btn-primary btn-sm" data-action="orderProcess">처리중...</button>`;
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
        },
        createdRow: function (row, data) {
            if (data.orderStatus === 2) {
                $(row).addClass('table-secondary');
            } else {
                $(row).removeClass('table-secondary');
            }
        }
    });
    // 소스 이름별 검색 select: 검색 리스트에 소스 리스트 api 로 추가
    $.ajax({
        url: '/api/product/company/allCompanySources',
        type: 'GET',
        success: function (response) {
            const sourceNames = response;
            const $sourceSelect = $('#sourceNameSearchSelect');

            sourceNames.forEach(function (sourceName) {
                $sourceSelect.append(new Option(sourceName, sourceName));
            });
        },
        error: function () {
            console.error('소스 이름을 가져오는 중 오류 발생');
        }
    });
    // 주문 상태 검색 select
    $('#orderTable_filter').append(`
        <select id="orderStatusSearchSelect" class="form-select form-select-sm orderSearch" style="display:inline-block; width:auto;">
            <option value="" selected>주문상태별 검색</option>
            <option value="1">주문</option>
            <option value="2">완료</option>
        </select>
        <select id="sourceNameSearchSelect" class="form-select form-select-sm orderSearch" style="display:inline-block; width:auto;">
            <option value="all" selected>생산품별 검색</option>
        </select>
    `);
}

// 생산품 관리 테이블 초기화
function getCompanySourceTable() {
    $('#companySourceTable').DataTable({
        ajax: {
            url: '/api/product/company/sources',
            dataSrc: 'companySourceList'
        },
        columns: [
            {data: null, render: (data, type, row, meta) => meta.row + 1},
            // {data: 'companySourceId', render: data => data.substring(0, 8)},
            {
                data: 'sourceName'
                // ,
                // render: function (data) {
                //     return `<button type="button" class="btn btn-light btn-sm" data-action="view">${data.toString()}</button>`;
                // }
            },
            {
                data: 'sourcePrice',
                render: function (data) {
                    return `<button type="button" class="btn btn-light btn-sm" data-action="update">
                         ${data.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")}₩</button>`;
                }
            },
            {data: 'totalQuantity'},
            {
                data: null,
                orderable: false,
                render: function (data) {
                    return `<button type="button" class="btn btn-outline-primary btn-sm work-btn"
                            data-id="${data.companySourceId}" data-action="produce">등록</button>`
                }
            }
        ],
        language: {emptyTable: '데이터가 없습니다.'},
        lengthMenu: [[5, 10, 25, 50], [5, 10, 25, 50]], pageLength: 10,
        initComplete: function () {
            $('#companySourceTable').css({
                'width': '100%',
                'table-layout': 'fixed'
            });
        },
        // drawCallback: function () {
        //     // 페이지가 변경되면, 각 버튼 상태를 다시 설정
        //     if ($('#companySourceBtnView').hasClass('work')) {
        //         $('.edit-btn').hide();
        //         $('.editablePrice').show();
        //     } else if ($('#companySourceBtnView').hasClass('edit')) {
        //         $('.edit-btn').show();
        //         $('.editablePrice').hide();
        //     }
        // }
    });
}

// 입출고 기록 테이블 초기화
function getWarehouseTable() {
    $('#warehouseTable').DataTable({
        ajax: {
            url: '/api/product/company/warehouse',
            data: function (d) {
                d.type = $('#warehouseTypeSelect').val();
                return $.param(d);
            },
            dataSrc: ''
        },
        columns: [
            {data: 'rowNum'},
            // {data: 'sourceWarehouseId', render: data => data.substring(0, 8)},
            {data: 'sourceName'},
            {data: 'sourceQuantity'},
            {
                data: 'type',
                orderable: false
            },
            {data: 'produceDate'},
            {
                data: null,
                orderable: false,
                render: function (data, type, row) {
                    console.log(row.sourceQuantity);
                    if (row.sourceQuantity > 0) {
                        return `<button type="button" class="btn btn-outline-danger btn-sm"
                        data-id="${data.sourceWarehouseId}" data-action="delete">삭제</button>`;
                    } else {
                        return '';
                    }
                }
            }
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

// 웹소켓
function webSocketConnect() {
    // socketCompanyId: 본인 업체의 DB가 변경 되었을 때만 메시지 수신하려고 받아옴
    let socketCompanyId = '';
    $.getJSON('/api/product/company/info', function (data) {
        socketCompanyId = data.companyId;
    });
    const socket = new SockJS('/websocket-endpoint');
    const stompClient = Stomp.over(socket);

    // 소켓 메시지 콘솔창에 안찍히게
    stompClient.debug = function (msg) {
    };

    // WebSocket 연결 시작
    stompClient.connect({}, function () {
        // WebSocket 구독 경로 설정
        stompClient.subscribe('/topic/product/company/' + socketCompanyId, function () {
            pageUpdate();
        });
        stompClient.subscribe('/topic/product/company', function () {
            pageUpdate();
        });
    });
}

// 차트 및 테이블 업데이트
function pageUpdate() {
    updateChart();
    $('#orderTable').DataTable().ajax.reload(null, false);
    // $('#warehouseTable').DataTable().ajax.reload(null, false);
    $('#companySourceTable').DataTable().ajax.reload(null, false);
}

// 차트 변수
let warehouseChart = null;
let orderChart = null;
let chartType = 'bar';
let orderChartMonthOption = '';

// 차트 업데이트 ajax
function updateChart() {
    $.ajax({
        url: '/api/product/company/chart', // 창고 데이터를 가져오는 URL
        type: 'GET',
        contentType: 'application/json',
        success: function (response) {
            const labels = [];
            const data = [];

            // response 데이터를 순회하며 필요한 정보 추출
            response.warehouseChart.forEach(function (item) {
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
    $.ajax({
        url: '/api/product/company/chart?month=' + orderChartMonthOption, // 창고 데이터를 가져오는 URL
        contentType: 'application/json',
        success: function (response) {
            const labels = [];
            const data = [];

            // response 데이터를 순회하며 필요한 정보 추출
            response.salesChart.forEach(function (item) {
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

// 창고 재고 차트 초기화
function initWarehouseChart(type) {
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

// 판매량 차트 초기화
function initOrderChart(type) {
    const ctx = $('#orderChart')[0].getContext('2d');

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

// 신규 생산품목 등록의 select 리스트 업데이트 ( SOURCE 에 있는 재료중 SOURCE_PRICE 엔 없는거 셀렉트에 추가해줌 )
function updateSourceSelectList() {
    $.ajax({
        url: '/api/product/company/sources',
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

// 페이지 초기화
function initPage() {
    $.ajax({
        url: '/api/product/company/info',
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            // 생산품 등록의 재료 리스트 초기화 ( 이미 등록된건 안나옴 )
            updateSourceSelectList();

            // 제일 처음 보이는 테이블 초기화 ( 현재 설정: 주문 처리 테이블 )
            getOrderTable();

            // 차트 초기화 아무 값 없는 빈 차트
            initWarehouseChart(chartType);
            initOrderChart(chartType);

            // 위에서 초기화한 차트에 첫번째 값 넣음 이거 사용해서 계속 값 업데이트 해주면됨
            updateChart();

            // 웹 소켓 연결
            webSocketConnect();

            // 나머지 테이블 전부 불러오고 숨김
            // 처음 페이지에 접속할때 초기화 하는 이유: 테이블 나중에 초기화하면 화면이 새로고침 비슷하게 되는 버그?가 있음.. @@:로딩 오래걸리면 로직 원래대로 변경
            getWarehouseTable();
            $('#warehouseTableContainer').hide();
            getCompanySourceTable();
            $('#companySourceTableContainer').hide();
        },
        error: function (xhr) {
            const response = JSON.parse(xhr.responseText);
            if (xhr.status === 403) {
                window.location.href = '/index';
                alert(response.message);
            } else {
                window.location.href = '/index';
                alert("알 수 없는 에러");
            }
        }
    });
}

// 생산품 관리 및 신규 생산품목 등록
function addSourceProcess() {

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
            url: '/api/product/company/sources',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data),
            beforeSend: function (xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);  // CSRF 헤더와 토큰을 함께 보냄
            },
            success: function () {
                $('#addSourceModal').modal('hide');
                // $('#companySourceTable').DataTable().ajax.reload(null, false);
                updateSourceSelectList();
                showToast("등록 완료")
            },
            error: function (xhr) {
                const response = JSON.parse(xhr.responseText);
                if (xhr.status === 400) {
                    alert(response.message)
                } else if (xhr.status === 409) {
                    $('#addSourceModal').modal('hide');
                    $('#companySourceTable').DataTable().ajax.reload(null, false);
                    alert(response.message);
                } else alert('알 수 없는 에러가 발생');
            }
        });
    });

    // 생산 모달창 -> 갯수 입력후 생산 로직 수행
    $('#companySourceTable').on('click', 'button[data-action="produce"]', function () {
        const row = $(this).closest('tr');
        const data = $('#companySourceTable').DataTable().row(row).data();

        $('#sourceQuantity').val(10);
        $('#produceSourceName').val(data.sourceName);
        $('#produceSourcePrice').val(data.sourcePrice.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
        $('#sourcePriceId').val(data.companySourceId);
        $('#checkWarehouseQuantity').val(data.totalQuantity);
        $('#produceSourceModal').modal('show');
    });
    $('#produceSourceBtn').on('click', function () {
        const sourceQuantity = $('#sourceQuantity').val();
        const sourcePriceId = $('#sourcePriceId').val();
        const sourceName = $('#produceSourceName').val();
        const checkQuantity = $('#checkWarehouseQuantity').val();

        const data = {
            sourceQuantity: sourceQuantity,
            sourcePriceId: sourcePriceId,
            checkQuantity: checkQuantity
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
                showToast("입고 완료 되었습니다: " + sourceName + " " + sourceQuantity + " 개");
                // $('#companySourceTable').DataTable().ajax.reload(null, false);
                updateChart();
                $('#produceSourceModal').modal('hide');
            },
            error: function (xhr) {
                const response = JSON.parse(xhr.responseText);
                if (xhr.status === 400) {
                    const errors = response.errors || [];
                    alert(errors[0] || response.message);
                } else if (xhr.status === 409) {
                    $('#companySourceTable').DataTable().ajax.reload(null, false);
                    updateChart();
                    $('#produceSourceModal').modal('hide');
                    alert(response.message);
                    $('#sourceQuantity').val(10);
                } else alert('알 수 없는 에러가 발생');
            }
        });
    });

    // 생산 품목 삭제 ( 삭제 로직 @@: 일단 안씀 )
    $('#companySourceTable').on('click', 'button[data-action="delete"]', function () {
        const row = $(this).closest('tr');
        const data = $('#companySourceTable').DataTable().row(row).data();
        const companySourceId = data.companySourceId;

        if (confirm('정말로 이 항목을 삭제하시겠습니까?')) {
            $.ajax({
                url: `/api/product/company/sources/${companySourceId}`,
                type: 'DELETE',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(csrfHeader, csrfToken);  // CSRF 헤더와 토큰을 함께 보냄
                },
                success: function () {
                    // $('#companySourceTable').DataTable().ajax.reload(null, false);
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

    // 생산 기록 ( 창고 ) 삭제
    $('#warehouseTable').on('click', 'button[data-action="delete"]', function () {
        const row = $(this).closest('tr');
        const data = $('#warehouseTable').DataTable().row(row).data();
        const sourceWarehouseId = data.sourceWarehouseId;

        if (confirm('정말로 이 항목을 삭제하시겠습니까?')) {
            $.ajax({
                url: `/api/product/company/warehouse/${sourceWarehouseId}`,
                type: 'DELETE',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(csrfHeader, csrfToken);  // CSRF 헤더와 토큰을 함께 보냄
                },
                success: function () {
                    $('#warehouseTable').DataTable().ajax.reload(null, false);
                    showToast("삭제 완료")
                },
                error: function () {
                    $('#warehouseTable').DataTable().ajax.reload(null, false);
                    alert('삭제 실패: 해당 입고 기록을 삭제할 수 없음\n( 현재 창고 재고보다 많은 입고 기록 삭제 불가 )');
                }
            });
        }
    });


    // view
    $('#companySourceTable').on('click', 'button[data-action="view"]', function () {
        const row = $(this).closest('tr');
        const data = $('#companySourceTable').DataTable().row(row).data();
        const companySourceId = data.companySourceId;
        $.ajax({
            url: `/api/product/company/sources/${companySourceId}`,
            type: 'GET',
            contentType: 'application/json',
            success: function () {
                $('#viewSourceModal').modal('show');
            }
        });
    });

    // 생산 품목 업데이트 ( 가격 수정 로직 )
    $('#companySourceTable').on('click', 'button[data-action="update"]', function () {
        const row = $(this).closest('tr');
        const data = $('#companySourceTable').DataTable().row(row).data();

        $('#updateSourcePriceError').text('');
        $('#updateSourcePrice').removeClass('is-invalid');

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
            url: `/api/product/company/sources/${companySourceId}`,
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(data),
            beforeSend: function (xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);  // CSRF 헤더와 토큰을 함께 보냄
            },
            success: function () {
                $('#updateSourceModal').modal('hide');
                showToast('생산품 가격 수정이 완료되었습니다.');
                // $('#companySourceTable').DataTable().ajax.reload(null, false);
            },
            error: function (xhr) {
                const response = JSON.parse(xhr.responseText);
                if (xhr.status === 400) {
                    const errors = response.errors || [];
                    $('#updateSourcePrice').addClass('is-invalid');
                    $('#updateSourcePriceError').text(errors[0] || response.message);
                    $('#updateSourcePrice').on('input', function () {
                        $(this).removeClass('is-invalid'); // 값을 변경하면 유효성 검사 실패 클래스 제거
                    });
                } else if (xhr.status === 409) {
                    $('#updateSourceModal').modal('hide');
                    $('#companySourceTable').DataTable().ajax.reload(null, false);
                    alert(response.message);
                } else {
                    alert('알 수 없는 에러가 발생');
                }
            }
        });
    });
}

// 주문 처리
function orderProcess() {
    let intervalId; // Order Process 작업중인거 시간 업데이트용 모달 닫거나 페이지 끄면 null 로 초기화

    // 셀렉트 검색 요소 변경시 api 새로 받아옴
    $(document).on('change', '.orderSearch', function () {
        $('#orderTable').DataTable().ajax.reload(function () {
        }, true);
    });

    // 재고 부족 버튼 클릭시 알림
    $('#orderTable').on('click', 'button[data-action="orderProcessBlock"]', function () {
        alert("출하 불가: 현재 상품의 재고가 부족함");
    });

    // 주문 처리, 주문 상태 변경하고 창고에서 주문받은 재료 갯수만큼 출고하는 로직 수행
    $('#orderTable').on('click', 'button[data-action="orderProcess"]', function () {
        const row = $(this).closest('tr');
        const data = $('#orderTable').DataTable().row(row).data();

        $('#orderIdView').val(data.orderId.substring(0, 8));
        $('#orderId').val(data.orderId);
        $('#orderSourceName').val(data.sourceName);
        $('#orderSourcePrice').val(data.sourcePrice.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
        $('#orderQuantity').val(data.quantity);
        $('#orderTotalPrice').val(data.totalPrice.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
        $('#orderDate').val(data.orderDate);
        $('#orderSourcePriceId').val(data.sourcePriceId);
        $('#orderStockBalance').val(data.stockBalance);

        // 출하 버튼 클릭시 다른 사용자가 해당 orderId 접근 불가능하게 잠금
        $.ajax({
            url: '/api/product/company/lockOrder',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                orderId: data.orderId,
                orderStatus: 2
            }),
            beforeSend: function (xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);  // CSRF 헤더와 토큰을 함께 보냄
            },
            success: function (response) {
                $('#orderProcessModal').modal('show');
                intervalId = setInterval(function () {
                    const orderId = $('#orderId').val();
                    $.ajax({
                        url: `/api/product/company/lockOrder/${orderId}`,
                        type: 'PUT',
                        contentType: 'application/json',
                        beforeSend: function (xhr) {
                            xhr.setRequestHeader(csrfHeader, csrfToken);
                        },
                        success: function (response) {
                            console.log("Order time updated");
                        },
                        error: function (error) {
                            console.error("Failed to update order time:", error);
                        }
                    });
                }, 10000);
            },
            error: function (xhr) {
                const response = JSON.parse(xhr.responseText);
                if (xhr.status === 409) { // 처리 중인 주문이라는 예외 발생 시
                    alert(response.message);
                } else {
                    alert("알 수 없는 에러 발생");
                }
                $('#orderProcessModal').modal('hide'); // 모달을 닫음
            }
        });
    });

    // 모달 닫힐시 orderId 잠금 해제 ( 새로고침이나 창끄기 시스템 종료같은 예외 상황엔 적용 안됨 스케줄러로 삭제 )
    $('#orderProcessModal').on('hidden.bs.modal', function () {
        const orderId = $('#orderId').val();

        if (intervalId) {
            clearInterval(intervalId);
            intervalId = null;
        }

        // 임시 상태 삭제 요청
        $.ajax({
            url: `/api/product/company/lockOrder/${orderId}`, // 임시 상태 삭제 요청 URL
            type: 'DELETE',
            contentType: 'application/json',
            beforeSend: function (xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            }
        });
    });

    // 실제 주문 처리 로직 실행
    $('#orderProcessBtn').on('click', function () {
        const orderId = $('#orderId').val();
        // const orderStatus = $('#orderStatusSelect').val();
        const sourcePriceId = $('#orderSourcePriceId').val();
        const orderQuantity = $('#orderQuantity').val();
        // const stockBalance = $('#orderStockBalance').val();

        const data = {
            orderId: orderId,
            orderStatus: 5,
            sourceQuantity: orderQuantity,
            sourcePriceId: sourcePriceId,
            // stockBalance: stockBalance
        };

        $.ajax({
            url: '/api/product/company/order',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data),
            beforeSend: function (xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function () {
                $('#orderProcessModal').modal('hide');
                showToast("주문 번호: '" + orderId.substring(0, 8) + "' 의 출하가 완료되었습니다.")
                updateChart();
                $('#orderTable').DataTable().ajax.reload(function () {
                }, false);
                if (intervalId) {
                    clearInterval(intervalId);
                    intervalId = null;
                }
            },
            error: function (xhr) {
                $('#orderProcessModal').modal('hide');
                const response = JSON.parse(xhr.responseText);
                if (xhr.status === 400) {
                    alert(response.message);
                } else if (xhr.status === 409) {
                    $('#orderTable').DataTable().ajax.reload(null, false);
                    alert(response.message);
                } else {
                    alert('알 수 없는 에러가 발생');
                }
            }
        });
    });
}

// 차트 타입, 테이블 변경, 버튼 감추기 등 드롭다운 및 버튼 이벤트 관리
function pageEventHandler() {

    // // 생산품 관리 테이블 edit 버튼 활성화
    // $('#companySourceBtnView').on('click', function () {
    //     if ($(this).hasClass('work')) {
    //         $(this).removeClass('btn-outline-secondary work').addClass('btn-secondary edit');
    //         $('.edit-btn').show();
    //         $('.editablePrice').hide();
    //     } else if ($(this).hasClass('edit')) {
    //         $(this).removeClass('btn-secondary edit').addClass('btn-outline-secondary work');
    //         $('.edit-btn').hide();
    //         $('.editablePrice').show();
    //     }
    // });

    // 셀렉트 검색 요소 변경시 api 새로 받아옴
    $('#warehouseTypeSelect').on('change', function () {
        $('#warehouseTable').DataTable().ajax.reload(function () {
        }, true);
    });

    // 차트 타입 변경
    $('.warehouse-dropdown-item').on('click', function () {
        const selectedType = $(this).data('value'); // 선택된 차트 타입 가져오기
        const selectedName = $(this).data('name'); // 선택된 차트 타입 가져오기
        $('#warehouseChartDropdown').text(selectedName);
        chartType = selectedType; // 선택된 차트 타입 저장
        initWarehouseChart(chartType); // 새로운 차트 타입으로 초기화
        updateChart(); // 새로운 차트에 데이터 업데이트
    });

    // 판매량 월별 검색 조건 파라미터로 api 새로 받기
    $('#chartMonthSelect').on('change', function () {
        orderChartMonthOption = $('#chartMonthSelect').val();
        updateChart();
    })

    // 테이블 변경
    $('.table-dropdown-item').on('click', function () {

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