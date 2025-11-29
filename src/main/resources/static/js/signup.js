function togglePassword(id, btn){
  const input = document.getElementById(id);
  const icon = btn.querySelector('i');
  const isHidden = input.type === 'password';
  input.type = isHidden ? 'text' : 'password';
  icon.classList.toggle('bi-eye', !isHidden);
  icon.classList.toggle('bi-eye-slash', isHidden);
}

document.addEventListener('DOMContentLoaded', () => {
  const forms = document.querySelectorAll('.needs-validation');
  const MIN_LENGTH = 6;

  forms.forEach(form => {
    form.reset();
    form.classList.remove('was-validated');
    form.querySelectorAll('.invalid-feedback').forEach(div => div.classList.remove('active'));
  });

  const setFieldValidity = (input, message) => {
    if (!input) return;
    input.setCustomValidity(message || '');
    const feedback = input.closest('.control-narrow')?.querySelector('.invalid-feedback');
    if (feedback) {
      const text = message || feedback.dataset.default || '';
      feedback.textContent = text;
      feedback.classList.toggle('active', !!message);
    }
  };

  const validateRequiredField = (input, emptyMessage) => {
    if (!input) return;
    const value = input.value.trim();
    input.value = value;
    setFieldValidity(input, value ? '' : emptyMessage);
  };

  const validateEmail = (input) => {
    if (!input) return;
    const value = input.value.trim();
    input.value = value;
    let message = '';
    if (!value) {
      message = 'Email không được để trống.';
    } else if (input.validity.typeMismatch) {
      message = 'Email không đúng định dạng.';
    }
    setFieldValidity(input, message);
  };

  const validatePasswordLength = (input, emptyMessage = 'Mật khẩu không được để trống.') => {
    if (!input) return;
    const value = input.value.trim();
    input.value = value;
    let message = '';
    if (!value) {
      message = emptyMessage;
    } else if (value.length < MIN_LENGTH) {
      message = `Mật khẩu phải có ít nhất ${MIN_LENGTH} ký tự.`;
    }
    setFieldValidity(input, message);
  };

  const validatePasswordMatch = (passwordInput, confirmInput) => {
    if (!(confirmInput && passwordInput)) return;
    const password = passwordInput.value;
    const confirm = confirmInput.value;
    let message = '';
    if (!confirm.trim()) {
      message = 'Vui lòng xác nhận lại mật khẩu.';
    } else if (password !== confirm) {
      message = 'Mật khẩu xác nhận phải khớp.';
    }
    setFieldValidity(confirmInput, message);
  };

  forms.forEach(form => {
    const fullNameInput = form.querySelector('#fullName');
    const emailInput = form.querySelector('#email');
    const passwordInput = form.querySelector('#password');
    const confirmInput = form.querySelector('#confirmPassword');

    const clearServerError = (field) => {
      const serverError = form.querySelector(`[data-error-field=\"${field}\"]`);
      if (serverError) {
        serverError.style.display = 'none';
        serverError.textContent = '';
      }
    };

    const runCustomValidations = () => {
      validateRequiredField(fullNameInput, 'Vui lòng nhập họ và tên của bạn.');
      validateEmail(emailInput);
      validatePasswordLength(passwordInput);
      validatePasswordLength(confirmInput, 'Vui lòng xác nhận lại mật khẩu.');
      validatePasswordMatch(passwordInput, confirmInput);
    };

    fullNameInput?.addEventListener('input', () => {
      if (!form.classList.contains('was-validated')) return;
      validateRequiredField(fullNameInput, 'Vui lòng nhập họ và tên của bạn.');
      fullNameInput.reportValidity();
    });

    emailInput?.addEventListener('input', () => {
      clearServerError('email');
      if (!form.classList.contains('was-validated')) return;
      validateEmail(emailInput);
      emailInput.reportValidity();
    });

    passwordInput?.addEventListener('input', () => {
      if (!form.classList.contains('was-validated')) return;
      validatePasswordLength(passwordInput);
      validatePasswordMatch(passwordInput, confirmInput);
      passwordInput.reportValidity();
    });

    confirmInput?.addEventListener('input', () => {
      if (!form.classList.contains('was-validated')) return;
      validatePasswordLength(confirmInput, 'Vui lòng xác nhận lại mật khẩu.');
      validatePasswordMatch(passwordInput, confirmInput);
      confirmInput.reportValidity();
    });

    form.addEventListener('submit', event => {
      runCustomValidations();
      if (!form.checkValidity()) {
        event.preventDefault();
        event.stopPropagation();
      }
      form.classList.add('was-validated');
    }, false);
  });
});
