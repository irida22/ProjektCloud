const API = '/api/students';

function showToast(message, type = 'success') {
    const el = document.getElementById('toast');
    el.textContent = message;
    el.className = `toast ${type}`;
    setTimeout(() => el.classList.add('hidden'), 4000);
}

async function api(url, options = {}) {
    const res = await fetch(url, {
        headers: { 'Content-Type': 'application/json', ...options.headers },
        ...options
    });
    if (res.status === 204) return null;
    const text = await res.text();
    let data = null;
    if (text) {
        try {
            data = JSON.parse(text);
        } catch {
            data = text;
        }
    }
    if (!res.ok) {
        const msg = data?.error || data?.message || (typeof data === 'string' ? data : `Gabim HTTP ${res.status}`);
        throw new Error(msg);
    }
    return data;
}

function formToStudent(form) {
    return {
        firstName: form.firstName.value.trim(),
        lastName: form.lastName.value.trim(),
        email: form.email.value.trim(),
        program: form.program.value.trim(),
        enrollmentYear: parseInt(form.enrollmentYear.value, 10)
    };
}

function fillForm(form, student) {
    const idInput = form.querySelector('[name="id"]');
    if (idInput) idInput.value = student.id;
    form.firstName.value = student.firstName;
    form.lastName.value = student.lastName;
    form.email.value = student.email;
    form.program.value = student.program;
    form.enrollmentYear.value = student.enrollmentYear;
}

// 1. LIST
async function loadStudents() {
    const tbody = document.getElementById('students-tbody');
    try {
        const students = await api(API);
        if (!students.length) {
            tbody.innerHTML = '<tr><td colspan="7" class="empty">Nuk ka studentë. Shto një të ri më poshtë.</td></tr>';
            return;
        }
        tbody.innerHTML = students.map(s => `
            <tr>
                <td>${s.id}</td>
                <td>${escapeHtml(s.firstName)}</td>
                <td>${escapeHtml(s.lastName)}</td>
                <td>${escapeHtml(s.email)}</td>
                <td>${escapeHtml(s.program)}</td>
                <td>${s.enrollmentYear}</td>
                <td class="actions-cell">
                    <button type="button" class="btn btn-secondary btn-sm" data-edit="${s.id}">Edito</button>
                    <button type="button" class="btn btn-danger btn-sm" data-delete="${s.id}">Fshi</button>
                </td>
            </tr>
        `).join('');

        tbody.querySelectorAll('[data-edit]').forEach(btn => {
            btn.addEventListener('click', () => startEdit(parseInt(btn.dataset.edit, 10)));
        });
        tbody.querySelectorAll('[data-delete]').forEach(btn => {
            btn.addEventListener('click', () => deleteStudent(parseInt(btn.dataset.delete, 10)));
        });
    } catch (e) {
        tbody.innerHTML = `<tr><td colspan="7" class="empty">Gabim: ${escapeHtml(e.message)}</td></tr>`;
    }
}

function escapeHtml(str) {
    const div = document.createElement('div');
    div.textContent = str;
    return div.innerHTML;
}

// 2. CREATE
document.getElementById('form-create').addEventListener('submit', async (e) => {
    e.preventDefault();
    const form = e.target;
    try {
        const created = await api(API, {
            method: 'POST',
            body: JSON.stringify(formToStudent(form))
        });
        showToast(`Student u shtua: ${created.firstName} ${created.lastName} (ID ${created.id})`);
        form.reset();
        await loadStudents();
    } catch (err) {
        showToast(err.message, 'error');
    }
});

// 3. UPDATE
async function startEdit(id) {
    try {
        const student = await api(`${API}/${id}`);
        const form = document.getElementById('form-update');
        fillForm(form, student);
        document.getElementById('delete-id').value = id;
        document.getElementById('section-update').scrollIntoView({ behavior: 'smooth' });
        showToast(`Student ID ${id} u ngarkua për editim`);
    } catch (err) {
        showToast(err.message, 'error');
    }
}

document.getElementById('btn-load-for-edit').addEventListener('click', async () => {
    const id = parseInt(document.getElementById('update-id').value, 10);
    if (!id) {
        showToast('Shkruaj një ID valid', 'error');
        return;
    }
    await startEdit(id);
});

document.getElementById('form-update').addEventListener('submit', async (e) => {
    e.preventDefault();
    const form = e.target;
    const id = parseInt(form.querySelector('[name="id"]').value, 10);
    try {
        const updated = await api(`${API}/${id}`, {
            method: 'PUT',
            body: JSON.stringify(formToStudent(form))
        });
        showToast(`Student u përditësua: ${updated.firstName} ${updated.lastName}`);
        await loadStudents();
    } catch (err) {
        showToast(err.message, 'error');
    }
});

// 4. DELETE
async function deleteStudent(id) {
    if (!confirm(`A je i sigurt që do të fshish studentin ID ${id}?`)) return;
    try {
        await api(`${API}/${id}`, { method: 'DELETE' });
        showToast(`Student ID ${id} u fshi`);
        document.getElementById('form-delete').reset();
        await loadStudents();
    } catch (err) {
        showToast(err.message, 'error');
    }
}

document.getElementById('form-delete').addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = parseInt(document.getElementById('delete-id').value, 10);
    await deleteStudent(id);
});

// 5. EXCEL EXPORT
document.getElementById('btn-export-excel').addEventListener('click', async () => {
    try {
        const res = await fetch(`${API}/export/excel`);
        if (!res.ok) throw new Error('Eksporti dështoi');
        const blob = await res.blob();
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'students.xlsx';
        a.click();
        URL.revokeObjectURL(url);
        showToast('Skedari students.xlsx u shkarkua');
    } catch (err) {
        showToast(err.message, 'error');
    }
});

document.getElementById('btn-refresh').addEventListener('click', loadStudents);

loadStudents();
