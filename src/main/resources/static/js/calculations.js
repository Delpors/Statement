function calculateRow(input) {
    const row = input.closest('tr');

    // Получаем базовые данные
    const baseSalary = parseFloat(row.querySelector('.base-Salary span').textContent) || 0;
    const nontaxable = parseFloat(row.getAttribute('data-nontaxable')) || 0;

    // Получаем значения из инпутов
    const bonus = parseFloat(row.querySelector('.bonus-input').value) || 0;
    const fss = parseFloat(row.querySelector('.fss-input').value) || 0;
    const replace = parseFloat(row.querySelector('.replace-input').value) || 0;
    const otherIncome = parseFloat(row.querySelector('.otherIncome-input').value) || 0;
    const absent = parseFloat(row.querySelector('.absent-input').value) || 0;
    const advance = parseFloat(row.querySelector('.advance-input').value) || 0;

    // Расчеты
    const totalEmployeeIncome = baseSalary + bonus + fss + replace + otherIncome - absent;
    const unionFee = Math.max(0, Math.round(totalEmployeeIncome * 0.01));
    const incomeTax = Math.max(0, Math.round((totalEmployeeIncome - nontaxable) * 0.13));
    const totalEmployeeDeduction = unionFee + incomeTax + advance;
    const totalIssued = Math.max(0, totalEmployeeIncome - totalEmployeeDeduction);

    // Обновляем ячейки
    row.querySelector('.total-income span').textContent = totalEmployeeIncome.toFixed(2);
    row.querySelector('.unionFee span').textContent = unionFee.toFixed(2);
    row.querySelector('.incomeTax span').textContent = incomeTax.toFixed(2);
    row.querySelector('.total-deduction span').textContent = totalEmployeeDeduction.toFixed(2);
    row.querySelector('.total-issued span').textContent = totalIssued.toFixed(2);

    calculateGrandTotals();
}

function calculateAll() {
    const rows = document.querySelectorAll('tbody tr:not(.total-row)');
    rows.forEach(row => {
        const input = row.querySelector('.calculate-total');
        if (input) calculateRow(input);
    });
}

function calculateGrandTotals() {
    const totals = {
        salary: 0, bonus: 0, fss: 0, replace: 0, otherIncome: 0,
        income: 0, absent: 0, unionFee: 0, incomeTax: 0,
        advance: 0, deduction: 0, issued: 0
    };

    const rows = document.querySelectorAll('tbody tr:not(.total-row)');

    rows.forEach(row => {
        totals.salary += parseFloat(row.querySelector('.base-Salary span').textContent) || 0;
        totals.bonus += parseFloat(row.querySelector('.bonus-input').value) || 0;
        totals.fss += parseFloat(row.querySelector('.fss-input').value) || 0;
        totals.replace += parseFloat(row.querySelector('.replace-input').value) || 0;
        totals.otherIncome += parseFloat(row.querySelector('.otherIncome-input').value) || 0;
        totals.income += parseFloat(row.querySelector('.total-income span').textContent) || 0;
        totals.absent += parseFloat(row.querySelector('.absent-input').value) || 0;
        totals.unionFee += parseFloat(row.querySelector('.unionFee span').textContent) || 0;
        totals.incomeTax += parseFloat(row.querySelector('.incomeTax span').textContent) || 0;
        totals.advance += parseFloat(row.querySelector('.advance-input').value) || 0;
        totals.deduction += parseFloat(row.querySelector('.total-deduction span').textContent) || 0;
        totals.issued += parseFloat(row.querySelector('.total-issued span').textContent) || 0;
    });

    // Обновляем итоговые значения
    Object.keys(totals).forEach(key => {
        const element = document.getElementById(`grand-total-${key}`);
        if (element) {
            element.textContent = totals[key].toFixed(2);
        }
    });
}

console.log('calculations.js loaded');