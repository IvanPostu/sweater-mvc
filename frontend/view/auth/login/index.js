

new Vue({
  el: '#login_form_id',
  // data: {
  //   message: 'Hello Vue!'
  // },
  mounted: function() {
    this.activationMessageTimeoutIfExist()

  },
  methods: {
    activationMessageTimeoutIfExist: () => {
      const activationAccountMessage = $('#activationAccountMessage')
      if (activationAccountMessage.length) {
        setTimeout(() => {
          activationAccountMessage.remove()
        }, 5500)
      }
    }
  }
})