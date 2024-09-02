window.addEventListener('DOMContentLoaded', event => {
    // Simple-DataTables
    // https://github.com/fiduswriter/Simple-DataTables/wiki

    const datatables = ['datatablesSimple', 'datatablesSimple02', 'datatablesSimple03', 'datatablesSimple04', 'datatablesSimple05'];

    datatables.forEach(id => {
        const element = document.getElementById(id);
        if (element) {
            new simpleDatatables.DataTable(element);
        }
    });
});
