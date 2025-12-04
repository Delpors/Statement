function removeEmployee(button) {
    const row = button.closest('tr');

    if (!row) {
        alert('Ошибка: не удалось найти строку для удаления');
        return;
    }

    if (!confirm('Вы уверены, что хотите удалить сотрудника из ведомости?')) {
        return;
    }

    row.remove();
    renumberRows();
    calculateGrandTotals();

    const remainingRows = document.querySelectorAll('tbody tr:not(.total-row)');
    if (remainingRows.length === 0) {
        alert('Все сотрудники удалены из ведомости');
    }
}

function renumberRows() {
    const rows = document.querySelectorAll('tbody tr:not(.total-row)');
    rows.forEach((row, index) => {
        const numberCell = row.querySelector('td:first-child');
        if (numberCell) {
            numberCell.textContent = index + 1;
        }
    });
}

console.log('removeEmployee.js loaded');