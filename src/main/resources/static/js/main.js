document.addEventListener('DOMContentLoaded', function() {
    console.log('Инициализация расчетно-платежной ведомости...');

    // Убедимся, что элементы существуют
    const monthSelect = document.getElementById('month');
    const yearInput = document.getElementById('year');
    const paymentDateInput = document.getElementById('paymentDate');

    console.log('Проверка элементов:');
    console.log('- month элемент:', monthSelect);
    console.log('- year элемент:', yearInput);
    console.log('- paymentDate элемент:', paymentDateInput);

    // Навешиваем обработчики событий на все инпуты для расчета
    const inputs = document.querySelectorAll('.calculate-total');
    inputs.forEach(input => {
        input.addEventListener('input', function() {
            calculateRow(this);
        });
        input.addEventListener('change', function() {
            calculateRow(this);
        });
    });

    // Обработчики для кнопок удаления
    const removeButtons = document.querySelectorAll('.btn-remove-employee');
    removeButtons.forEach(button => {
        button.addEventListener('click', function() {
            removeEmployee(this);
        });
    });

    // Обработчик отправки формы
    const form = document.getElementById('payrollForm');
    if (form) {
        form.addEventListener('submit', function(event) {
            event.preventDefault();
            console.log('Форма отправляется...');
            submitPayroll(event);
        });
    }

    // Устанавливаем дату выплаты (сегодняшнюю по умолчанию)
    if (paymentDateInput && !paymentDateInput.value) {
        const today = new Date().toISOString().split('T')[0];
        paymentDateInput.value = today;
        console.log('Установлена дата выплаты по умолчанию:', today);
    }

    // Устанавливаем год по умолчанию, если не заполнен
    if (yearInput && !yearInput.value) {
        const currentYear = new Date().getFullYear();
        yearInput.value = currentYear;
        console.log('Установлен год по умолчанию:', currentYear);
    }

    // Выполняем первоначальный расчет всех строк
    calculateAll();

    console.log('Расчетно-платежная ведомость инициализирована');
});