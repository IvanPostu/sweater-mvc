

new Vue({
  el: '#activationPageID',
  data: {
    redirectTime: 10,
    timer: null
  },
  mounted: function () {
    this.showTimer()
    this.setRedirectTimeout()

  },
  methods: {
    showTimer: function () {
      $('#redirectTimeID').removeClass('d-none')
    },

    setRedirectTimeout: function () {

      this.timer = setInterval(() => {
        this.redirectTime--;

        if (this.redirectTime === 0) {
          this.timer = clearInterval(this.timer)
          $('#loginSubmitBtnId_').click()
        }

      }, 999)

    }


  }
})