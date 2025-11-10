// ================== HERO CAROUSEL (nếu đang dùng) ==================
document.addEventListener('DOMContentLoaded', function () {
  const el = document.querySelector('#heroCarousel');
  if (el) {
    el.querySelectorAll('.carousel-item')
      .forEach(i => i.setAttribute('data-bs-interval', '4000'));

    const carousel = new bootstrap.Carousel(el, {
      interval: 4000,
      ride: 'carousel',
      wrap: true,
      pause: false,
      keyboard: true,
      touch: true
    });

    el.addEventListener('slid.bs.carousel', (e) => {
      const items = el.querySelectorAll('.carousel-item');
      const activeIndex = [...items].indexOf(e.relatedTarget);
      console.log('activeIndex:', activeIndex, '/', items.length);
    });
  }

  // ================== SECTION "CHÚNG TÔI CÓ TẤT CẢ..." ==================
  const fadeItems = document.querySelectorAll('.fade-item');

  if (!('IntersectionObserver' in window)) {
    // fallback: nếu browser cũ, show luôn
    fadeItems.forEach(item => item.classList.add('show'));
    return;
  }

  const observer = new IntersectionObserver((entries, ob) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.classList.add('show');
        ob.unobserve(entry.target);
      }
    });
  }, { threshold: 0.15 });

  fadeItems.forEach(item => observer.observe(item));
});
