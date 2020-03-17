




new Vue({
  el: '#headerNavbar',
  data: {
  },
  mounted: () => {

  },
  methods: {
    logoutEvent: function (/*e*/) {
      const hiddenLogoutFormButton = document.getElementById('logoutSubmitBtnId')
      hiddenLogoutFormButton.click()
    }
  }
})