
$(document).ready(function() {
    updateSourceSelectList();

    /* 테이블 가져옴 */
    let companySourceTable = $('#companySourceTable').DataTable({
        searching: false,
        ajax: {
            url: '/api/product/company/add',
            dataSrc: 'companySourceList'
        },
        columns: [
            { data: 'companySourceId'},
            { data: 'companyName' },
            { data: 'companyAddress' },
            { data: 'sourceName' },
            { data: 'sourcePrice' },
            {
                data: null,
                render: function (data, type, row) {
                    return '<button type="button" class="btn btn-primary btn-sm" data-action="produce">생산</button> | ' +
                        '<button type="button" class="btn btn-info btn-sm" data-action="approve">가격 수정</button> | ' +
                        '<button type="button" class="btn btn-danger btn-sm" data-action="cancel">등록 삭제</button>';
                }
            }
        ],
        columnDefs: [
            { targets: 0, visible: false },  // 첫 번째 열 숨기기
        ],
        pagingType: 'simple_numbers',
        language: {
            paginate: {
                next: '>', // 다음 페이지 버튼
                previous: '<' // 이전 페이지 버튼
            }
        }
    });

    // let warehouseTable = $('#warehouseTable').DataTable({
    //     searching: false,
    //     ajax: {
    //         url: '/api/product/company/produce',
    //         dataSrc: ''
    //     },
    //     columns: [
    //         { data: 'sourceWarehouseId' },
    //         { data: 'sourceName' },
    //         { data: 'sourceQuantity' },
    //         { data: 'sourcePrice' },
    //         { data: 'totalPrice' },
    //         { data: 'produceDate'},
    //         {
    //             data: null,
    //             render: function (data, type, row) {
    //                 return '<button type="button" class="btn btn-primary btn-sm" data-action="manage">재고 관리</button>';
    //             }
    //         }
    //     ],
    //     pagingType: 'simple_numbers',
    //     language: {
    //         paginate: {
    //             next: '>', // 다음 페이지 버튼
    //             previous: '<' // 이전 페이지 버튼
    //         }
    //     }
    // });

    /* 생산품 등록 관련 스크립트 */
    // 셀렉트 박스 변경 이벤트
    $('#addSourceSelect').on('change', function() {
        if ($(this).val() !== "directInput") {
            $('#addSourceInput').val('').prop('disabled', true);  // 인풋 필드 비활성화 및 초기화
            $('#addSourceSelect').prop('disabled', false);  // 셀렉트 박스 활성화
        } else {
            $('#addSourceInput').prop('disabled', false).focus();  // 인풋 필드 활성화 및 포커스
        }
    });

    $('#addSourceBtn').on('click', function() {
        let data = {
            sourcePrice: $('#sourcePrice').val()
        };

        if ($('#addSourceInput').prop('disabled')) {
            // 셀렉트 박스가 활성화된 경우 (옵션 선택)
            data.sourceId = $('#addSourceSelect').val();
        } else {
            // 인풋 필드가 활성화된 경우 (직접 입력)
            data.sourceName = $('#addSourceInput').val();
        }

        $.ajax({
            url: '/api/product/company/add',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function(response) {
                $('#addSourceModal').modal('hide');
                companySourceTable.ajax.reload(null, false);  // 데이터 유지하면서 테이블 새로고침
                updateSourceSelectList();
            },
            error: function(xhr, status, error) {
                console.error('Error occurred:', error);
            }
        });
    });
    /* - 생산품 등록 스크립트 끝 -*/

    // Delete
    $('#companySourceTable').on('click', 'button[data-action="cancel"]', function() {
        // 선택된 행의 데이터를 가져옴
        const row = $(this).closest('tr');
        const data = companySourceTable.row(row).data();

        const companySourceId = data.companySourceId;

        // 삭제 확인 메시지
        if (confirm('정말로 이 항목을 삭제하시겠습니까?')) {
            $.ajax({
                url: `/api/product/company/add/${companySourceId}`,
                type: 'DELETE',
                success: function(response) {
                    companySourceTable.ajax.reload(null, false);  // 테이블 새로고침
                },
                error: function(xhr, status, error) {
                    console.error('Error occurred while deleting:', error);
                }
            });
        }
    });

    /* 생산 스크립트 */
    $('#companySourceTable').on('click', 'button[data-action="produce"]', function() {
        // Get the data for the row
        const row = $(this).closest('tr');
        const data = companySourceTable.row(row).data();

        $('#produceSourceName').val(data.sourceName);
        $('#produceSourcePrice').val(data.sourcePrice);
        $('#sourcePriceId').val(data.companySourceId);

        $('#produceSourceModal').modal('show');
    });
    $('#produceSourceBtn').on('click', function() {
        const sourceQuantity = $('#sourceQuantity').val();
        const sourcePriceId = $('#sourcePriceId').val();

        // Create the request payload
        const data = {
            sourceQuantity: sourceQuantity,
            sourcePriceId: sourcePriceId
        };

        // Make the AJAX request to the server
        $.ajax({
            url: '/api/product/company/produce',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function(response) {
                $('#produceSourceModal').modal('hide');
                alert('생산 요청이 완료되었습니다.');
                companySourceTable.ajax.reload(null, false);  // Reload the table
            },
            error: function(xhr, status, error) {
                console.error('Error occurred:', error);
                alert('생산 요청 중 오류가 발생했습니다.');
            }
        });
    });
    /* 생산 스크립트 끝 */

    /* 다른 리스트들 */

    // 드롭다운 메뉴 항목 선택 시 이벤트 처리
    $('.dropdown-menu a').on('click', function(event) {
        event.preventDefault();

        const selectedValue = $(this).data('value');
        const selectedText = $(this).text();

        // 드롭다운 버튼의 텍스트를 선택한 항목으로 변경
        $('#viewListDropDown').text(selectedText);

        if (selectedValue === 'companySourceList') {
            $('#companySourceTableContainer').show();
            $('#warehouseTableContainer').hide();
        } else if (selectedValue === 'warehouseList') {
            $('#companySourceTableContainer').hide();
            $('#warehouseTableContainer').show();
        }
    });
}); // -- document.ready 끝 --

// 생산품 등록 select list 업데이트 하는 메서드
function updateSourceSelectList() {
    $.ajax({
        url: '/api/product/company/add',  // API URL
        type: 'GET',
        contentType: 'application/json',
        success: function(response) {
            const sources = response.sources;
            const selectBox = $('#addSourceSelect');

            // 기존 옵션들 제거
            selectBox.empty();

            // 기본 옵션 추가
            selectBox.append($('<option>', {
                value: 'directInput',
                text: '직접입력'
            }));

            // 새로운 옵션들 추가
            sources.forEach(function(source) {
                const option = $('<option>', {
                    value: source.sourceId,
                    text: source.sourceName
                });
                selectBox.append(option);
            });
        },
        error: function(xhr, status, error) {
            console.error('Error occurred while fetching sources:', error);
        }
    });
}

const ctx = document.getElementById('salesProductChart');
new Chart(ctx, {
    type: 'doughnut',
    data: {
        labels: ['감자', '삼겹살', '김치', '고구마', '고등어', '문어'],
        datasets: [{
            label: '# of Votes',
            data: [12, 19, 3, 5, 2, 3],
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
const ctx2 = document.getElementById('salesProductPriceChart');
new Chart(ctx2, {
    type: 'bar',
    data: {
        labels: ['감자', '삼겹살', '김치', '고구마', '고등어', '문어'],
        datasets: [{
            label: '# of Votes',
            data: [12, 19, 3, 5, 2, 3],
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