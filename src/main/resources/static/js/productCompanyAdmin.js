$(document).ready(function (){
    getProductOrderList()

    // 차트 초기화 아무 값 없는 빈 차트
    initSourcePriceChart(chartType);
    initProductOrderChart(chartType)
    // 위에서 초기화한 차트에 첫번째 값 넣음 이거 사용해서 계속 값 업데이트 해주면됨
    updateSourceMinPriceChart();
    updateProductOrderCountChart();

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
//sourcePrice 차트 옵션

const sourcePriceChartOption = document.getElementById("sourcePriceChartOption");
sourcePriceChartOption.addEventListener("change",function (){
    const selectedSourcePriceChartOptionValue = sourcePriceChartOption.value;
    if(selectedSourcePriceChartOptionValue === "sourceMinPrice"){

    }else if(selectedSourcePriceChartOptionValue === "sourcePriceByCompany"){

    }
})

//productOrder 차트 옵션

const productOrderChartOption = document.getElementById("productOrderChartOption");

productOrderChartOption.addEventListener("change",function (){
    const selectedProductOrderChartOptionValue = productOrderChartOption.value;
    if(selectedProductOrderChartOptionValue === "productOrderCountByCompany"){

    }else if (selectedProductOrderChartOptionValue === "productOrderCountByProduct"){

    }
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
/* 차트 관련 */

let sourceMinPriceChart; // 전역 변수로 차트 객체를 선언
let productOrderAdminChart;
let chartType = 'bar'; // 기본 차트 타입은 'bar'

function updateSourceMinPriceChart() {
    $.ajax({
        url: '/api/product/admin/main/data/sourceMinPriceChart', // 최저가 데이터를 가져오는 URL
        type: 'GET',
        contentType: 'application/json',
        success: function (response) {
            const companyNames = []; // 회사 이름 배열
            const productNames = []; // 제품 이름 배열
            const prices = []; // 최저가 배열
            console.log(response)
            // response 데이터를 순회하며 필요한 정보 추출
            response.forEach(function (item) {
                companyNames.push(item.productCompanyName);
                productNames.push(item.productName);
                prices.push(item.minPrice);
            });
            sourceMinPriceChart.data.labels = productNames;
            sourceMinPriceChart.data.datasets[0].label = '최저가';
            sourceMinPriceChart.data.datasets[0].data = prices;
            sourceMinPriceChart.data.datasets[0].backgroundColor = colors.slice(0, prices.length);
            sourceMinPriceChart.update();
        },
        error: function (xhr, status, error) {
            console.error('차트 데이터 가져오는데 실패함:', error);
        }
    });
}
function updateProductOrderCountChart() {
    $.ajax({
        url: '/api/product/admin/main/data/productOrderCountChart', // 최저가 데이터를 가져오는 URL
        type: 'GET',
        contentType: 'application/json',
        success: function (response) {
            const companyNames = []; // 회사 이름 배열
            const count = []; // 개수 배열
            console.log(response)
            // response 데이터를 순회하며 필요한 정보 추출
            response.forEach(function (item) {
                companyNames.push(item.productCompanyName);
                count.push(item.count);
            });
            productOrderAdminChart.data.labels = companyNames;
            productOrderAdminChart.data.datasets[0].label = '주문 수량';
            productOrderAdminChart.data.datasets[0].data = count;
            productOrderAdminChart.data.datasets[0].backgroundColor = colors.slice(0, count.length);
            productOrderAdminChart.update();
        },
        error: function (xhr, status, error) {
            console.error('차트 데이터 가져오는데 실패함:', error);
        }
    });
}

// 차트 초기화 함수, 차트의 타입을 동적으로 받음, 차트 두개 띄우려면 아이디랑 차트 객체 파라미터로 받으면 될듯?

function initSourcePriceChart(type) {
    const ctx = $('#sourcePriceChart')[0].getContext('2d');

    // 기존 차트가 있으면 삭제하고 새로 생성
    if (sourceMinPriceChart) {
        sourceMinPriceChart.destroy(); // 기존 차트 삭제
    }

    sourceMinPriceChart = new Chart(ctx, {
        type: type, // 동적으로 받은 차트 타입
        data: {
            labels: [], // 초기 레이블
            datasets: [{
                label: '최저 가격',
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

function initProductOrderChart(type) {
    const ctx = $('#productOrderAdminChart')[0].getContext('2d');

    // 기존 차트가 있으면 삭제하고 새로 생성
    if (productOrderAdminChart) {
        productOrderAdminChart.destroy(); // 기존 차트 삭제
    }

    productOrderAdminChart = new Chart(ctx, {
        type: type, // 동적으로 받은 차트 타입
        data: {
            labels: [], // 초기 레이블
            datasets: [{
                label: '주문 수량',
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
