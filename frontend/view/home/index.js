import './style.scss'

console.log('home')



new Vue({
  el: '#app',
  data: {
    message: 'Hello Vue!'
  },
  mounted: () => {

  },
  methods: {
    imgOnErrorHandle: function (e) {
      e.target.src = 'static/assets/images/no-image200x200.jpeg'
    }
  }
})