document.addEventListener('DOMContentLoaded', () => {
  // Nút hiện/ẩn mật khẩu
  const pwd = document.getElementById('password');
  const btn = document.getElementById('togglePassword');
  const icon = document.getElementById('togglePasswordIcon');

  if (btn && pwd && icon) {
    btn.addEventListener('click', () => {
      const isHidden = pwd.type === 'password';
      pwd.type = isHidden ? 'text' : 'password';
      icon.classList.toggle('bi-eye', !isHidden);
      icon.classList.toggle('bi-eye-slash', isHidden);
    });
  }

  // Thông báo tự ẩn sau 3 giây
  const msg = document.getElementById('loginMessage');
  if (msg) {
    msg.style.transition = 'opacity 0.5s ease';
    msg.style.opacity = '1';

    setTimeout(() => {
      msg.style.opacity = '0';
      setTimeout(() => {
        msg.style.display = 'none';
      }, 500);
    }, 3000);
  }

  // Validate form (viền đỏ)
  const forms = document.querySelectorAll('.needs-validation');
  Array.from(forms).forEach((form) => {
    form.addEventListener(
      'submit',
      (event) => {
        if (!form.checkValidity()) {
          event.preventDefault();
          event.stopPropagation();
        }
        form.classList.add('was-validated');
      },
      false,
    );
  });
});
