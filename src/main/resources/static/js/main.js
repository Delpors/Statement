document.addEventListener('DOMContentLoaded', function() {
    console.log('Инициализация расчетно-платежной ведомости...');

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
        form.addEventListener('submit', submitPayroll);
    }

    // Устанавливаем дату выплаты (сегодняшнюю по умолчанию)
    const paymentDateInput = document.getElementById('paymentDate');
    if (paymentDateInput && !paymentDateInput.value) {
        const today = new Date().toISOString().split('T')[0];
        paymentDateInput.value = today;
        console.log('Установлена дата выплаты по умолчанию:', today);
    }

    // Выполняем первоначальный расчет всех строк
    calculateAll();

    console.log('Расчетно-платежная ведомость инициализирована');
});