//생산업체 등록 Script
$(document).ready(function () {
    $("#btn-insertProductCompany").on("click", function () {
        $("#insertProductCompanyForm").submit();
    })
})

//Table List 변경 Script
$(document).ready(function () {
    var productTableChangeClassName;
    $("#productTableChange").on("click", function () {
        productTableChangeClassName = $("#productTableChange").attr('class');
        if (productTableChangeClassName.includes("order")) {
            $("#productTableChange").removeClass().addClass("btn btn-danger production").text("상품 현황");
            $("#productTableChange").val("production");
            $.ajax({
                url: "/api/product/admin/main/data/sourcePriceList",
                method: "get",
                dataType: "json",
                data: {
                    productTableStatus: $("#productTableChange").val()
                },
                success: function (data) {
                    $("#productAdminTableTHead").empty();
                    $("#productAdminTableTHead").append(
                        "<tr>" +
                        "<th>" + "번호" + "</th>" +
                        "<th>" + "생산 업체 명" + "</th>" +
                        "<th>" + "상품 명" + "</th>" +
                        "<th>" + "상품 가격" + "</th>" +
                        "</tr>"
                    )
                    $("#productAdminTableTBody").empty();
                    data.forEach(function (item) {
                        $("#productAdminTableTBody").append(
                            "<tr>" +
                            "<td>" + item.rowNum + "</td>" +
                            "<td>" + item.productCompanyName + "</td>" +
                            "<td>" + item.sourceName + "</td>" +
                            "<td>" + item.sourcePrice + "</td>" +
                            "</tr>"
                        )
                    })
                },
                error: function (xhr, status, error) {
                    console.log("Error: " + error);
                    console.log("Status: " + status);
                    console.dir(xhr); // 서버에서 반환한 상세 오류 확인
                }
            });
        }

        if (productTableChangeClassName.includes("production")) {
            $("#productTableChange").removeClass().addClass("btn btn-primary order").text("주문 현황");
            $("#productTableChange").val("order");
            $.ajax({
                url: "/api/product/admin/main/data/productOrderList",
                method: "get",
                dataType: "json",
                data: {
                    productTableStatus: $("#productTableChange").val()
                },
                success: function (data) {
                    $("#productAdminTableTHead").empty();
                    $("#productAdminTableTHead").append(
                        "<tr>" +
                        "<th>" + "주문번호" + "</th>" +
                        "<th>" + "생산 업체 명" + "</th>" +
                        "<th>" + "상품 명" + "</th>" +
                        "<th>" + "상품 가격" + "</th>" +
                        "<th>" + "개수" + "</th>" +
                        "<th>" + "총 상품가격" + "</th>" +
                        "<th>" + "주문일" + "</th>" +
                        "<th>" + "상태" + "</th>" +
                        "<th>" + "주문 승인 | 주문 거절" + "</th>" +
                        "</tr>"
                    )
                    $("#productAdminTableTBody").empty();
                    data.forEach(function (item) {
                        $("#productAdminTableTBody").append(
                            "<tr>" +
                            "<td>" + item.rowNum + "</td>" +
                            "<td>" + item.productCompanyName + "</td>" +
                            "<td>" + item.sourceName + "</td>" +
                            "<td>" + item.sourcePrice + "</td>" +
                            "<td>" + item.quantity + "</td>" +
                            "<td>" + item.totalPrice + "</td>" +
                            "<td>" + item.productOrderDate + "</td>" +
                            "<td>" + item.status + "</td>" +
                            "<td>" + "<button class='btn btn-primary'>승인</button><button class='btn btn-danger'>거절</button>" + "</td>" +
                            "</tr>"
                        )
                    })

                },
                error: function (xhr, status, error) {
                    console.log("Error: " + error);
                    console.log("Status: " + status);
                    console.dir(xhr); // 서버에서 반환한 상세 오류 확인
                }
            });
        }

    })
})
$(document).on("click", ".page-link", function(e) {
    var pageValue = $(this).data("value");
    console.log(pageValue)
    var productTableChangeClassName = $("#productTableChange").attr('class');
    // if(pageValue.includes("prev")){
    //
    // }
    // if(pageValue.includes("next")){
    //
    // }
    if(productTableChangeClassName.includes("order")){
        $.ajax({
            url : "/api/product/admin/main/data/productOrderList",
            method: "get",
            dataType: "json",
            data: {
                productTableStatus: $("#productTableChange").val(),
                page : pageValue
            },
            success: function (data) {
                $("#productAdminTableTHead").empty();
                $("#productAdminTableTHead").append(
                    "<tr>" +
                    "<th>" + "주문번호" + "</th>" +
                    "<th>" + "생산 업체 명" + "</th>" +
                    "<th>" + "상품 명" + "</th>" +
                    "<th>" + "상품 가격" + "</th>" +
                    "<th>" + "개수" + "</th>" +
                    "<th>" + "총 상품가격" + "</th>" +
                    "<th>" + "주문일" + "</th>" +
                    "<th>" + "상태" + "</th>" +
                    "<th>" + "주문 승인 | 주문 거절" + "</th>" +
                    "</tr>"
                )
                $("#productAdminTableTBody").empty();
                data.forEach(function (item) {
                    $("#productAdminTableTBody").append(
                        "<tr>" +
                        "<td>" + item.rowNum + "</td>" +
                        "<td>" + item.productCompanyName + "</td>" +
                        "<td>" + item.sourceName + "</td>" +
                        "<td>" + item.sourcePrice + "</td>" +
                        "<td>" + item.quantity + "</td>" +
                        "<td>" + item.totalPrice + "</td>" +
                        "<td>" + item.productOrderDate + "</td>" +
                        "<td>" + item.status + "</td>" +
                        "<td>" + "<button class='btn btn-primary'>승인</button><button class='btn btn-danger'>거절</button>" + "</td>" +
                        "</tr>"
                    )
                })

            },
            error: function (xhr, status, error) {
                console.log("Error: " + error);
                console.log("Status: " + status);
                console.dir(xhr); // 서버에서 반환한 상세 오류 확인
            }
        })
    }
    if(productTableChangeClassName.includes("production")){
        $.ajax({
            url : "/api/product/admin/main/data/sourcePriceList",
            method: "get",
            dataType: "json",
            data: {
                productTableStatus: $("#productTableChange").val(),
                page : pageValue
            },
            success: function (data) {
                $("#productAdminTableTHead").empty();
                $("#productAdminTableTHead").append(
                    "<tr>" +
                    "<th>" + "번호" + "</th>" +
                    "<th>" + "생산 업체 명" + "</th>" +
                    "<th>" + "상품 명" + "</th>" +
                    "<th>" + "상품 가격" + "</th>" +
                    "</tr>"
                )
                $("#productAdminTableTBody").empty();
                data.forEach(function (item) {
                    $("#productAdminTableTBody").append(
                        "<tr>" +
                        "<td>" + item.rowNum + "</td>" +
                        "<td>" + item.productCompanyName + "</td>" +
                        "<td>" + item.sourceName + "</td>" +
                        "<td>" + item.sourcePrice + "</td>" +
                        "</tr>"
                    )
                })
            },
            error: function (xhr, status, error) {
                console.log("Error: " + error);
                console.log("Status: " + status);
                console.dir(xhr); // 서버에서 반환한 상세 오류 확인
            }
        })
    }

    // 이후 필요한 추가 작업을 이곳에 작성 (예: AJAX 요청 등)
});
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