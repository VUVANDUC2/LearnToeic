document.addEventListener("DOMContentLoaded", function () {
  const heroSwiper = new Swiper(".hero-swiper", {
    // core
    loop: true,
    slidesPerView: 1,
    speed: 700,
    autoplay: { delay: 4000, disableOnInteraction: false },

    // fade needs a bit more help when looping
    // effect: "fade",
    // fadeEffect: { crossFade: true },
    loopAdditionalSlides: 3,   // 👈 thêm dòng này (hoặc dùng loopedSlides: 3)
    // loopedSlides: 3,

    preloadImages: true,
    updateOnImagesReady: true,

    pagination: { el: ".hero .swiper-pagination", clickable: true },
    navigation: {
      nextEl: ".hero .swiper-button-next",
      prevEl: ".hero .swiper-button-prev",
    },

    // debug nhanh (xem Swiper có sang index 2 không)
    on: {
      slideChange(swiper) {
        console.log("realIndex:", swiper.realIndex, "/", swiper.slides.length);
      },
    },
  });
});
