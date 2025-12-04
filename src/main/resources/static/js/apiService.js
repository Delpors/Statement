function collectFormData() {
    const paymentDateInput = document.getElementById('paymentDate');

    if (!paymentDateInput) {
        alert('Ошибка: не найден элемент выбора даты');
        return null;
    }

    const paymentDate = paymentDateInput.value.trim();

    if (!paymentDate) {
        alert('Пожалуйста, укажите дату выплаты');
        paymentDateInput.focus();
        return null;
    }

    // Дополнительная валидация даты
    const selectedDate = new Date(paymentDate);
    const today = new Date();

    if (selectedDate > today) {
        if (!confirm('Выбранная дата выплаты находится в будущем. Вы уверены?')) {
            paymentDateInput.focus();
            return null;
        }
    }

    const formData = [];
    const rows = document.querySelectorAll('tbody tr:not(.total-row)');

    if (rows.length === 0) {
        alert('В ведомости нет сотрудников');
        return null;
    }

    let hasErrors = false;

    rows.forEach((row, index) => {
        const employeeId = row.getAttribute('data-employee-id');

        if (!employeeId) {
            alert(`Ошибка: у сотрудника в строке ${index + 1} не указан ID`);
            hasErrors = true;
            return;
        }

        try {
            const data = {
                employeeId: parseInt(employeeId),
                baseSalary: parseFloat(row.querySelector('.base-Salary span').textContent) || 0,
                bonus: parseFloat(row.querySelector('.bonus-input').value) || 0,
                fss: parseFloat(row.querySelector('.fss-input').value) || 0,
                replace: parseFloat(row.querySelector('.replace-input').value) || 0,
                otherIncome: parseFloat(row.querySelector('.otherIncome-input').value) || 0,
                totalEmployeeIncome: parseFloat(row.querySelector('.total-income span').textContent) || 0,
                absent: parseFloat(row.querySelector('.absent-input').value) || 0,
                unionFee: parseFloat(row.querySelector('.unionFee span').textContent) || 0,
                incomeTax: parseFloat(row.querySelector('.incomeTax span').textContent) || 0,
                advance: parseFloat(row.querySelector('.advance-input').value) || 0,
                totalEmployeeDeduction: parseFloat(row.querySelector('.total-deduction span').textContent) || 0,
                totalIssued: parseFloat(row.querySelector('.total-issued span').textContent) || 0,
                paymentDate: paymentDate,
            };

            if (isNaN(data.employeeId) || data.employeeId <= 0) {
                throw new Error('Некорректный ID сотрудника');
            }

            formData.push(data);
        } catch (error) {
            alert(`Ошибка в данных сотрудника (строка ${index + 1}): ${error.message}`);
            hasErrors = true;
        }
    });

    if (hasErrors) {
        return null;
    }

    console.log('Собранные данные для отправки:', formData);
    return formData;
}

function submitPayroll(event) {
    if (event) {
        event.preventDefault();
    }

    const formData = collectFormData();
    if (!formData) return;

    const submitBtn = document.getElementById('submitBtn');
    const originalText = submitBtn.innerHTML;
    submitBtn.innerHTML = '<i class="bi bi-hourglass-split"></i> Сохранение...';
    submitBtn.disabled = true;

    fetch('/payroll/payrollItems/create', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData)
    })
        .then(response => {
            if (response.ok) {
                return response.json().catch(() => ({})); // Обрабатываем случаи когда ответ пустой
            } else {
                return response.json().then(errorData => {
                    throw new Error(errorData.message || `Ошибка сервера: ${response.status}`);
                });
            }
        })
        .then(data => {
            console.log('Успешный ответ:', data);
            window.location.href = '/payroll?success';
        })
        .catch(error => {
            console.error('Ошибка:', error);
            alert(`Ошибка при сохранении: ${error.message}`);
            window.location.href = '/payroll?error';
        })
        .finally(() => {
            submitBtn.innerHTML = originalText;
            submitBtn.disabled = false;
        });
}

console.log('apiService.js loaded');