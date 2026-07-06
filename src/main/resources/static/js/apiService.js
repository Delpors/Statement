function collectFormData() {
    const monthSelect = document.getElementById('month');
    const yearInput = document.getElementById('year');
    const paymentDateInput = document.getElementById('paymentDate');

    if (!monthSelect || !yearInput || !paymentDateInput) {
        alert('Ошибка: не найдены элементы выбора периода и даты');
        return null;
    }

    const month = monthSelect.value;
    const year = yearInput.value;
    const paymentDate = paymentDateInput.value.trim();

    if (!month || month === '' || month === '0') {
        alert('Пожалуйста, выберите месяц');
        monthSelect.focus();
        return null;
    }

    if (!year || year.trim() === '') {
        alert('Пожалуйста, укажите год');
        yearInput.focus();
        return null;
    }

    if (!paymentDate) {
        alert('Пожалуйста, укажите дату выплаты');
        paymentDateInput.focus();
        return null;
    }

    const monthNumber = parseInt(month);
    const yearNumber = parseInt(year);

    if (isNaN(monthNumber) || monthNumber < 1 || monthNumber > 12) {
        alert('Пожалуйста, выберите корректный месяц (1-12)');
        monthSelect.focus();
        return null;
    }

    if (isNaN(yearNumber) || yearNumber < 2025 || yearNumber > 2100) {
        alert('Пожалуйста, укажите корректный год (2025-2100)');
        yearInput.focus();
        return null;
    }

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
        const nonTaxableAmount = row.getAttribute('data-nontaxable') || 0;

        if (!employeeId) {
            alert(`Ошибка: у сотрудника в строке ${index + 1} не указан ID`);
            hasErrors = true;
            return;
        }

        try {
            const fullName = row.querySelector('.fixed-col-name').textContent.trim();
            const position = row.querySelector('.fixed-col-position').textContent.trim();

            const data = {
                employeeId: employeeId, // Добавляем employeeId
                fullName: fullName,
                nonTaxable: parseFloat(nonTaxableAmount) || 0,
                position: position,
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
                month: monthNumber,
                year: yearNumber,
                paymentDate: paymentDate,
            };

            if (!data.fullName) {
                throw new Error('Не указано ФИО сотрудника');
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
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');

    submitBtn.innerHTML = '<i class="bi bi-hourglass-split"></i> Сохранение...';
    submitBtn.disabled = true;

    const headers = {
        'Content-Type': 'application/json'
    };

    if (csrfToken && csrfHeader) {
        headers[csrfHeader] = csrfToken;
    } else {
        console.warn('CSRF токен не найден в мета-тегах. Возможно, страница не обработана Thymeleaf.');
    }

    fetch('/payroll/payrollItems/create', {
        method: 'POST',
        credentials: 'include',
        headers: headers,
        body: JSON.stringify(formData)
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(`HTTP ${response.status}: ${text}`);
                });
            }
            return response.json();
        })
        .then(data => {
            console.log('Успешный ответ:', data);
            window.location.href = '/payroll?success=true';
        })
        .catch(error => {
            console.error('Ошибка:', error);
            window.location.href = '/payroll?error=true';
        })
        .finally(() => {
            submitBtn.innerHTML = originalText;
            submitBtn.disabled = false;
        });
}
console.log('apiService.js loaded');