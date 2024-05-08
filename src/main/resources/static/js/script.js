var inputs = document.querySelectorAll('input[required]');
inputs.forEach(function(input) {
    input.addEventListener('invalid', function() {
        input.setCustomValidity('Please fill out this field.');
    });
});