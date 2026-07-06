function collectFormData() {
    const formData = [];
    const rows = document.querySelectorAll('tbody tr:not(.total-row)');

    if (rows.length === 0) {
        alert('Нет данных для сохранения');
        return null;
    }

    rows.forEach((row) => {
        const payrollItemId = row.getAttribute('data-payrollItem-id');
        const employeeId = row.getAttribute('data-employee-id');
        const nonTaxable = parseFloat(row.getAttribute('data-nontaxable')) || 0;
        const paymentDate = row.getAttribute('data-payment-data');
        const month = parseInt(row.getAttribute('data-month'));
        const year = parseInt(row.getAttribute('data-year'));

        // Проверяем обязательные поля
        if (!employeeId) {
            console.warn('Строка без employeeId пропущена');
            return; // пропускаем эту строку
        }

        const data = {
            employeeId: parseFloat(employeeId),
            payrollItemId: payrollItemId ? parseFloat(payrollItemId) : null,
            nonTaxable: nonTaxable,
            position: row.querySelector('.fixed-col-position').textContent.trim(),
            baseSalary: parseFloat(row.querySelector('.fixed-col-position span').textContent) || 0,
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
            month: month,
            year: year,
            paymentDate: paymentDate,
        };
        formData.push(data);
    });

    if (formData.length === 0) {
        alert('Нет корректных данных для сохранения');
        return null;
    }

    return formData;
}

function submitPayroll() {
    const formData = collectFormData();
    if (!formData) return;

    if (formData.length === 0) {
        alert('Нет данных для сохранения');
        return;
    }

    const submitBtn = document.querySelector('button[onclick="submitPayroll()"]');
    const originalText = submitBtn.innerHTML;
    submitBtn.innerHTML = '<i class="bi bi-hourglass-split"></i> Сохранение...';
    submitBtn.disabled = true;

    const csrfToken = document.querySelector('meta[name="_csrf"]')?.content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content;

    const headers = {
        'Content-Type': 'application/json',
    };

    if (csrfToken && csrfHeader) {
        headers[csrfHeader] = csrfToken;
    }

    fetch('/payroll/payrollItems/create', {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(formData)
    })
        .then(response => {
            if (!response.ok) {
                if (response.status === 415) {
                    throw new Error('Неподдерживаемый тип содержимого. Сервер ожидает JSON.');
                }
                return response.text().then(text => {
                    throw new Error(`HTTP ${response.status}: ${text}`);
                });
            }
            // Если это redirect, обрабатываем его
            if (response.redirected) {
                window.location.href = response.url;
                return;
            }
            return response.text(); // или .json() если возвращается JSON
        })
        .then(data => {
            console.log('Успешный ответ:', data);
            window.location.href = '/payroll?success=true';
        })
        .catch(error => {
            console.error('Ошибка:', error);
            alert('Ошибка при сохранении: ' + error.message);
            window.location.href = '/payroll?error=true';
        })
        .finally(() => {
            submitBtn.innerHTML = originalText;
            submitBtn.disabled = false;
        });
}
console.log('apiServiceForEditPI.js loaded');