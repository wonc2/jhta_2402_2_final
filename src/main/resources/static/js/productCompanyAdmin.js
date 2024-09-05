$(document).ready(function (){
    getProductOrderList()
    // getProductOrderChart()

    //생산업체 등록 Script
    $("#btn-insertProductCompany").on("click", function () {
        $("#insertProductCompanyForm").submit();
    })

    //버튼으로 테이블 리스트 전환 기능
    var productTableChangeClassName;
    $("#productTableChange").on("click", function () {
        productTableChangeClassName = $("#productTableChange").attr('class');
        if (productTableChangeClassName.includes("order")) {
            $("#productTableChange").removeClass().addClass("btn btn-sm btn-outline-danger production").text("상품 현황");
            $("#productTableChange").val("production");
            $("#productOrderTableContainer").hide()
            $("#sourcePriceTableContainer").show()
            if (!$.fn.DataTable.isDataTable('#productAdminSourceTable')) {
                getSourcePriceList();  // warehouseTable 초기화
            } else {
                $('#productAdminOrderTable').DataTable().ajax.reload(function () {} , true);  // warehouseTable 재로드
            }
        }
        if (productTableChangeClassName.includes("production")) {
            $("#productTableChange").removeClass().addClass("btn btn-sm btn-outline-primary order").text("주문 현황");
            $("#productTableChange").val("order");
            $("#productOrderTableContainer").show()
            $("#sourcePriceTableContainer").hide()
        }

    })
    // 검색 버튼 클릭 시 이벤트
    $('#productAdminSearchBtn').on('click', function (e) {
        var status = $("#productTableChange").val()
        if(status === "order"){
            $("#productAdminOrderTable").DataTable().ajax.reload(function () {} , true)
        }else if(status === "production"){
            $("#productAdminSourceTable").DataTable().ajax.reload(function () {} , true)
        }
    });
})

function getSourcePriceList() {
    $('#productAdminSourceTable').DataTable({
        ajax: {
            url: '/api/product/admin/main/data/sourcePriceSearchList',
            dataSrc: '',
            data : function (d) {
                d.companyName = $("#productAdminSearchTextCompanyName").val()
                d.productName = $("#productAdminSearchTextProductName").val()
            },
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
            url: '/api/product/admin/main/data/productOrderSearchList',
            dataSrc: '',
            data : function (d) {
                d.companyName = $("#productAdminSearchTextCompanyName").val()
                d.productName = $("#productAdminSearchTextProductName").val()
            },
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
        ],
        language: {emptyTable: '데이터가 없습니다.'},
        lengthMenu: [ [5, 10, 25, 50], [5, 10, 25, 50] ],
        searching : false
    });
}


function  getProductOrderChart(){
    $.ajax({
        url : "/api/product/admin/main/data/productOrderChart" ,
        type : "GET",
        success : function (data){
            console.log(data)
        },
        error: function (xhr, status, error) {
            console.error('차트 데이터 가져오는데 실패함:', error);
        }
    })
}
// const ctx = document.getElementById('productOrderAdminChart');
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