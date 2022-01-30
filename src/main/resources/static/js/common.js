function isEmpty(value) {
    return !value || !value.length || /^\s+$/.test(value);
}

function preloader(active) {
    if (active) {
        $('.preloader').removeClass('d-none');
    } else {
        $('.preloader').addClass('d-none');
    }
}
