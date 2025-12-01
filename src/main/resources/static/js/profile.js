document.addEventListener('DOMContentLoaded', function () {
  const btn = document.querySelector('.copy-email');
  if (btn) {
    btn.addEventListener('click', function () {
      const email = btn.getAttribute('data-email');
      if (!email) return;
      navigator.clipboard.writeText(email).then(() => {
        alert('Đã copy email vào clipboard');
      });
    });
  }

  const avatarInput = document.getElementById('avatarInput');
  const avatarStatus = document.getElementById('avatarFileStatus');
  if (avatarInput && avatarStatus) {
    const defaultMessage = 'Chưa có tệp nào được chọn.';
    const updateStatus = () => {
      if (avatarInput.files && avatarInput.files.length > 0) {
        avatarStatus.textContent = `Đã chọn: ${avatarInput.files[0].name}`;
      } else {
        avatarStatus.textContent = defaultMessage;
      }
    };
    updateStatus();
    avatarInput.addEventListener('change', updateStatus);
  }

  const autoAlerts = document.querySelectorAll('.alert[data-auto-dismiss]');
  autoAlerts.forEach(alert => {
    setTimeout(() => alert.remove(), 5000);
  });
});
