import bytesToImg from '@/jsUtils/byteArrayToImgSrc.js'

// new Vue({
//   el: '#registration_form_id',
//   mounted: function () {
//     this.generateCaptchaImage()

//   },
//   methods: {



//   }
// })





$('#registration_form_id').ready(function () {

  generateCaptchaImage()

  $('#resetCaptchaBtnID').on('click', generateCaptchaImage)

  function generateCaptchaImage() {
    $.ajax({
      url: '/api/registration/registration_captcha',
      type: 'GET',
      success: function (data) {
        const captchaImageSrc = bytesToImg(data['captcha'])
        const captchaImageNode = $('#captchaImgID')
        captchaImageNode.attr('src', captchaImageSrc)

        captchaImageNode.on('load', () => {
          captchaImageNode.removeClass('d-none')
        })
      },
      error: function () {

      }
    });
  }

})