

// 토스트
function showToast(message) {
    // 새로운 토스트 요소 생성
    const toastId = 'toast-' + new Date().getTime(); // 고유 ID 생성
    const toastHtml = `
        <div id="${toastId}" class="toast" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="toast-body">${message}</div>
        </div>
    `;

    // 토스트 컨테이너에 추가
    $('#toastContainer').append(toastHtml);

    // 새로운 토스트 표시
    const $newToast = $(`#${toastId}`);
    $newToast.toast({
        autohide: true,
        delay: 5000
    }).toast('show');

    // 기존 토스트가 있으면 위치 조정
    const $allToasts = $('#toastContainer .toast');
    $allToasts.each((index, element) => {
        $(element).css({
            'margin-bottom': '10px'
        });
    });

    // 토스트가 사라진 후 DOM에서 제거
    $newToast.on('hidden.bs.toast', function () {
        $(this).remove();
    });
}