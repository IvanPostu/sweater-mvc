



$('#login_form_id').ready(function () {
  const activationAccountMessage = $('#activationAccountMessage')
  if (activationAccountMessage.length) {
    setTimeout(() => {
      activationAccountMessage.remove()
    }, 5500)
  }
})