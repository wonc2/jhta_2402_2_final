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
        let data = { sourcePrice: $('#sourcePrice').val() };

        if ($('#addSourceInput').prop('disabled')) {
            data.sourceId = $('#addSourceSelect').val();
        } else {
            data.sourceName = $('#addSourceInput').val();
        }

        $.ajax({
            url: '/api/product/company/add',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function () {
                $('#addSourceModal').modal('hide');
                $('#companySourceTable').DataTable().ajax.reload(null, false);
                updateSourceSelectList();
            },
            error: function (xhr, status, error) {
                console.error('Error occurred:', error);
            }
        });
    });

    // 테이블 내 버튼 클릭 이벤트
    $('#companySourceTable').on('click', 'button[data-action="cancel"]', function () {
        const row = $(this).closest('tr');
        const data = $('#companySourceTable').DataTable().row(row).data();
        const companySourceId = data.companySourceId;

        if (confirm('정말로 이 항목을 삭제하시겠습니까?')) {
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

    // 드롭다운 메뉴 항목 선택 시 이벤트 처리
    $('.dropdown-menu a').on('click', function (event) {
        event.preventDefault();

        const selectedValue = $(this).data('value');
        const selectedText = $(this).text();

        $('#viewListDropDown').text(selectedText);

        if (selectedValue === 'companySourceList') {
            $('#companySourceTableContainer').show();
            $('#warehouseTableContainer').hide();
        } else if (selectedValue === 'warehouseList') {
            $('#companySourceTableContainer').hide();
            $('#warehouseTableContainer').show();
            if (!$.fn.DataTable.isDataTable('#warehouseTable')) {
                getWarehouseTable();  // warehouseTable 초기화
            } else {
                $('#warehouseTable').DataTable().ajax.reload(null, false);  // warehouseTable 재로드
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
            {data: 'companyName'},
            {data: 'companyAddress'},
            {data: 'sourceName'},
            {data: 'sourcePrice'},
            {
                data: null,
                render: function () {
                    return `
                        <button type="button" class="btn btn-primary btn-sm" data-action="produce">생산</button> |
                        <button type="button" class="btn btn-info btn-sm" data-action="approve">수정</button> |
                        <button type="button" class="btn btn-danger btn-sm" data-action="cancel">삭제</button>
                    `;
                }
            }
        ],
        language: {emptyTable: '데이터가 없습니다.'}
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
            {data: 'sourcePrice'},
            {data: 'totalPrice'},
            {data: 'produceDate'}
        ],
        language: {emptyTable: '데이터가 없습니다.'}
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