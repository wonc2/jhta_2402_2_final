//생산업체 등록 Script
$(document).ready(function () {
    $("#btn-insertProductCompany").on("click", function () {
        $("#insertProductCompanyForm").submit();
    })
})
$(document).ready(function (){
    getProductOrderList()
})
$(document).ready(function () {
    var productTableChangeClassName;
    $("#productTableChange").on("click", function () {
        productTableChangeClassName = $("#productTableChange").attr('class');
        if (productTableChangeClassName.includes("order")) {
            $("#productTableChange").removeClass().addClass("btn btn-sm btn-outline-danger production").text("상품 현황");
            $("#productTableChange").val("production");
            $("#productOrderTableContainer").hide()
            $("#sourcePriceTableContainer").show()
            $('#productAdminOrderTable').DataTable().ajax.reload(null, false);
            if (!$.fn.DataTable.isDataTable('#productAdminSourceTable')) {
                getSourcePriceList();  // warehouseTable 초기화
            } else {
                $('#productAdminOrderTable').DataTable().ajax.reload(null, false);  // warehouseTable 재로드
            }
            $('#productAdminSourceTable').DataTable().ajax.reload(null, false);
        }
        if (productTableChangeClassName.includes("production")) {
            $("#productTableChange").removeClass().addClass("btn btn-sm btn-outline-primary order").text("주문 현황");
            $("#productTableChange").val("order");
            $("#productOrderTableContainer").show()
            $("#sourcePriceTableContainer").hide()
        }

    })
})
//Table List 변경 Script
function getSourcePriceList() {
    $('#productAdminSourceTable').DataTable({
        ajax: {
            url: '/api/product/admin/main/data/sourcePriceList',
            dataSrc: ''
        },
        columns: [
            {data: 'rowNum' },
            {data: 'productCompanyName'},
            {data: 'sourceName'},
            {data: 'sourcePrice'}
        ],
        language: {emptyTable: '데이터가 없습니다.'},
        lengthMenu: [[5, 10, 25, 50], [5, 10, 25, 50]],
        searching : false
    });
}
function getProductOrderList(){
    $('#productAdminOrderTable').DataTable({
        ajax: {
            url: '/api/product/admin/main/data/productOrderList',
            dataSrc: ''
        },
        columns: [
            {data: 'rowNum' },
            {data: 'productCompanyName'},
            {data: 'sourceName'},
            {data: 'sourcePrice'},
            {data: 'quantity'},
            {data: 'totalPrice'},
            {data: 'productOrderDate'},
            {data: 'status'},
            {
                data: null,
                render: function () {
                    return ` <button class='btn btn-primary'>승인</button><button class='btn btn-danger'>거절</button> `;
                }
            }
        ],
        language: {emptyTable: '데이터가 없습니다.'},
        lengthMenu: [ [5, 10, 25, 50], [5, 10, 25, 50] ],
        searching : false
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