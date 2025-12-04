function collectFormData() {
    const paymentDateInput = document.getElementById('paymentDate');
    const paymentDate = paymentDateInput.value.trim()
    if (!paymentDate) {
        alert('Пожалуйста, выберите дату выплаты');
        return null;
    }

    const formData = [];
    const rows = document.querySelectorAll('tbody tr:not(.total-row)');

    rows.forEach((row) => {
        const payrollItemId = row.getAttribute('data-payrollItem-id');
        const employeeId = row.getAttribute('data-employee-id');
        const data = {
            payrollItemId: payrollItemId ? parseFloat(payrollItemId) : null,
            employeeId: employeeId ? parseFloat(employeeId) : null,
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
            paymentDate: paymentDate
        };
        formData.push(data);
    });

    return formData;
}

function submitPayroll() {
    const formData = collectFormData();
    if (!formData) return;

    // Валидация - проверяем, что есть данные для отправки
    if (formData.length === 0) {
        alert('Нет данных для сохранения');
        return;
    }

    const submitBtn = document.querySelector('button[onclick="submitPayroll()"]');
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
                window.location.href = '/payroll?success';
            } else {
                throw new Error(`Server error: ${response.status}`);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            window.location.href = '/payroll?error';
        })
        .finally(() => {
            submitBtn.innerHTML = originalText;
            submitBtn.disabled = false;
        });
}
console.log('apiServiceForEditPI.js loaded');