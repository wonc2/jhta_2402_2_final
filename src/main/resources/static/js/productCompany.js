    // const csrfToken = $('meta[name="_csrf"]').attr('content');
    // const csrfHeader = $('meta[name="_csrf_header"]').attr('content');
    //
    // console.log(csrfToken);
    // console.log(csrfHeader);



    $(document).ready(function () {
        updateSourceSelectList();
        getCompanySourceTable();

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

            $.ajax({
                url: '/api/product/company/add',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(data),
                // beforeSend: function (xhr) {
                //     xhr.setRequestHeader(csrfHeader, csrfToken);
                // },
                success: function () {
                    $('#addSourceModal').modal('hide');
                    $('#companySourceTable').DataTable().ajax.reload(null, false);
                    updateSourceSelectList();
                },
                error: function (xhr, status, error) {
                    const response = JSON.parse(xhr.responseText);
                    if (response.error) {
                        alert('에러 발생: ' + response.message);
                    } else {
                        alert('이미 생산 목록에 등록된 상품입니다.');
                    }
                }
            });
        });

        // 생산 품목 삭제
        $('#companySourceTable').on('click', 'button[data-action="delete"]', function () {
            const row = $(this).closest('tr');
            const data = $('#companySourceTable').DataTable().row(row).data();
            const companySourceId = data.companySourceId;

            if (confirm('정말로 이 항목을 삭제하시겠습니까?\n -- 삭제 -> 가리기(생산중단) 버튼으로 바꿔야할듯 참조키 있으면 삭제하면 안됨 로그 날아감')) {
                $.ajax({
                    url: `/api/product/company/add/${companySourceId}`,
                    type: 'DELETE',
                    success: function () {
                        $('#companySourceTable').DataTable().ajax.reload(null, false);
                    },
                    error: function (xhr, status, error) {
                        console.error('Error occurred while deleting:', error);
                    }
                });
            }
        });

        // 생산 품목 업데이트
        $('#companySourceTable').on('click', 'button[data-action="update"]', function () {
            const row = $(this).closest('tr');
            const data = $('#companySourceTable').DataTable().row(row).data();

            $('#updateSourceName').val(data.sourceName);
            $('#updateSourcePrice').val(data.sourcePrice);
            $('#updateCompanySourceId').val(data.companySourceId);

            $('#updateSourceModal').modal('show');
        });
        $('#updateSourceBtn').on('click', function () {
            const sourcePrice = $('#updateSourcePrice').val();
            const companySourceId = $('#updateCompanySourceId').val();

            const data = { sourcePrice: sourcePrice };
            $.ajax({
                url: `/api/product/company/add/${companySourceId}`,
                type: 'PUT',
                contentType: 'application/json',
                data: JSON.stringify(data),
                success: function () {
                    $('#updateSourceModal').modal('hide');
                    alert('생산품 가격 수정이 완료되었습니다.');
                    $('#companySourceTable').DataTable().ajax.reload(null, false);
                },
                error: function (xhr, status, error) {
                    console.error('Error occurred:', error);
                    alert('가격 수정중 오류가 발생했습니다.');
                }
            });
        });

        // 생산 품목 생산
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

            const data = {
                sourceQuantity: sourceQuantity,
                sourcePriceId: sourcePriceId
            };

            $.ajax({
                url: '/api/product/company/produce',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(data),
                success: function () {
                    $('#produceSourceModal').modal('hide');
                    alert('생산 요청이 완료되었습니다.');
                    $('#companySourceTable').DataTable().ajax.reload(null, false);
                },
                error: function (xhr, status, error) {
                    console.error('Error occurred:', error);
                    alert('생산 요청 중 오류가 발생했습니다.');
                }
            });
        });

        /* Order Process */
        // 주문 처리
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

            $('#orderProcessModal').modal('show');
        });
        $('#orderProcessBtn').on('click', function () {
            const orderId = $('#orderId').val();
            const orderStatus = $('#orderStatusSelect').val();
            const sourcePriceId =  $('#orderSourcePriceId').val();
            const orderQuantity = $('#orderQuantity').val();

            const data = {
                orderId: orderId,
                orderStatus: orderStatus,
                sourceQuantity: orderQuantity,
                sourcePriceId: sourcePriceId
            };

            $.ajax({
                url: '/api/product/company/order',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(data),
                success: function () {
                    $('#orderProcessModal').modal('hide');
                    alert('주문 처리가 완료되었습니다.');
                    $('#orderTable').DataTable().ajax.reload(null, false);
                },
                error: function (xhr, status, error) {
                    console.error('Error occurred:', error);
                    alert('주문 처리 도중 오류가 발생했습니다. 재고가 모자랄수도?');
                }
            });
        });
        /* Order Process 끝 */

        // 드롭다운 메뉴 항목 선택 시 이벤트 처리
        $('.dropdown-menu a').on('click', function (event) {
            event.preventDefault();

            const selectedValue = $(this).data('value');
            const selectedText = $(this).text();

            $('#viewListDropDown').text(selectedText);

            if (selectedValue === 'companySourceList') {
                $('#companySourceTableContainer').show();
                $('#warehouseTableContainer').hide();
                $('#orderTableContainer').hide();
                $('#companySourceTable').DataTable().ajax.reload(null, false);
            } else if (selectedValue === 'warehouseList') {
                $('#companySourceTableContainer').hide();
                $('#orderTableContainer').hide();
                $('#warehouseTableContainer').show();
                if (!$.fn.DataTable.isDataTable('#warehouseTable')) {
                    getWarehouseTable();  // warehouseTable 초기화
                } else {
                    $('#warehouseTable').DataTable().ajax.reload(null, false);  // warehouseTable 재로드
                }
            } else if (selectedValue === 'orderList') {
                $('#warehouseTableContainer').hide();
                $('#companySourceTableContainer').hide();
                $('#orderTableContainer').show();
                if (!$.fn.DataTable.isDataTable('#orderTable')) {
                    getOrderTable();  // warehouseTable 초기화
                } else {
                    $('#orderTable').DataTable().ajax.reload(null, false);  // warehouseTable 재로드
                }
            }
        });
    });

    // Helper function
    function updateSourceSelectList() {
        $.ajax({
            url: '/api/product/company/add',
            type: 'GET',
            contentType: 'application/json',
            success: function (response) {
                const sources = response.sources;
                const selectBox = $('#addSourceSelect');

                selectBox.empty();
                selectBox.append($('<option>', { value: 'directInput', text: '직접입력' }));

                sources.forEach(function (source) {
                    const option = $('<option>', { value: source.sourceId, text: source.sourceName });
                    selectBox.append(option);
                });
            },
            error: function (xhr, status, error) {
                console.error('Error occurred while fetching sources:', error);
            }
        });
    }

    function getCompanySourceTable() {
        $('#companySourceTable').DataTable({
            ajax: {
                url: '/api/product/company/add',
                dataSrc: 'companySourceList'
            },
            columns: [
                {data: 'companySourceId', render: data => data.substring(0, 8)},
                {data: 'sourceName'},
                {data: 'sourcePrice'},
                {data: 'totalQuantity'},
                {
                    data: null,
                    render: function () {
                        return `
                            <button type="button" class="btn btn-primary btn-sm" data-action="produce">생산</button> |
                            <button type="button" class="btn btn-info btn-sm" data-action="update">수정</button> |
                            <button type="button" class="btn btn-danger btn-sm" data-action="delete">삭제</button>
                        `;
                    }
                }
            ],
            language: {emptyTable: '데이터가 없습니다.'},
            lengthMenu: [ [5, 10, 25, 50], [5, 10, 25, 50] ]
        });
    }
    function getWarehouseTable(){
        $('#warehouseTable').DataTable({
            ajax: {
                url: '/api/product/company/produce',
                dataSrc: ''
            },
            columns: [
                {data: 'sourceWarehouseId', render: data => data.substring(0, 8) },
                {data: 'sourceName'},
                {data: 'sourceQuantity'},
                {data: 'produceDate'}
            ],
            language: {emptyTable: '데이터가 없습니다.'},
            lengthMenu: [ [5, 10, 25, 50], [5, 10, 25, 50] ]
        });
    }
    function getOrderTable(){
        $('#orderTable').DataTable({
            ajax: {
                url: '/api/product/company/order',
                dataSrc: ''
            },
            columns: [
                {data: 'orderId', render: data => data.substring(0, 8) },
                {data: 'sourceName'},
                {data: 'sourcePrice'},
                {data: 'quantity'},
                {data: 'totalPrice'},
                {data: 'orderDate'},
                {data: 'orderStatus'},
                {
                    data: null,
                    render: function (data, type, row) {
                        // 'orderStatus'가 '처리전'일 때만 버튼을 렌더링
                        if (row.orderStatus === '처리전') {
                            return `<button type="button" class="btn btn-outline-primary btn-sm" data-action="orderProcess">승인?확인?발주?</button>`;
                        } else {
                            return ''; // 처리 완료 상태라면 버튼을 렌더링하지 않음
                        }
                    }
                }
            ],
            language: {emptyTable: '데이터가 없습니다.'},
            lengthMenu: [ [5, 10, 25, 50], [5, 10, 25, 50]]
        });
    }

    // const ctx = document.getElementById('salesProductChart');
    // new Chart(ctx, {
    //     type: 'doughnut',
    //     data: {
    //         labels: ['감자', '삼겹살', '김치', '고구마', '고등어', '문어'],
    //         datasets: [{
    //             label: '# of Votes',
    //             data: [12, 19, 3, 5, 2, 3],
    //             borderWidth: 1
    //         }]
    //     },
    //     options: {
    //         scales: {
    //             y: {
    //                 beginAtZero: true
    //             }
    //         }
    //     }
    // });
    // const ctx2 = document.getElementById('salesProductPriceChart');
    // new Chart(ctx2, {
    //     type: 'bar',
    //     data: {
    //         labels: ['감자', '삼겹살', '김치', '고구마', '고등어', '문어'],
    //         datasets: [{
    //             label: '# of Votes',
    //             data: [12, 19, 3, 5, 2, 3],
    //             borderWidth: 1
    //         }]
    //     },
    //     options: {
    //         scales: {
    //             y: {
    //                 beginAtZero: true
    //             }
    //         }
    //     }
    // });