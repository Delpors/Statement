document.addEventListener('DOMContentLoaded', function() {
    console.log('Initializing payroll calculator...');

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

    // Устанавливаем дату выплаты
    const today = new Date().toISOString().split('T')[0];
    const paymentDateInput = document.getElementById('paymentDate');

    console.log('Payment date input element:', paymentDateInput);
    console.log('Today date:', today);

    if (paymentDateInput) {
        // Сначала проверяем, есть ли уже значение в инпуте
        if (paymentDateInput.value) {
            console.log('Payment date already set to:', paymentDateInput.value);
        } else {
            // Ищем первую строку с данными о выплате
            const firstRowWithPaymentData = document.querySelector('tr[data-payment-data]');
            console.log('First row with payment data:', firstRowWithPaymentData);

            if (firstRowWithPaymentData) {
                const paymentData = firstRowWithPaymentData.getAttribute('data-payment-data');
                console.log('Payment data from row attribute:', paymentData);

                // Проверяем, что значение не null, не undefined и не пустая строка
                if (paymentData && paymentData.trim() !== '' && paymentData !== 'null') {
                    paymentDateInput.value = paymentData;
                    console.log('Set payment date from row data:', paymentData);
                } else {
                    paymentDateInput.value = today;
                    console.log('Set payment date to today (invalid row data):', today);
                }
            } else {
                paymentDateInput.value = today;
                console.log('Set payment date to today (no rows):', today);
            }
        }
    } else {
        console.error('Payment date input not found!');
    }

    // Выполняем первоначальный расчет всех строк
    calculateAll();

    console.log('Payroll calculator initialized successfully');
});