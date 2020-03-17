// import './style.scss'




new Vue({
  el: '#homepageID',
  // data: {
  //   message: 'Hello Vue!'
  // },
  mounted: () => {

  },
  methods: {
    imgOnErrorHandle: e => {
      e.target.src = 'static/assets/images/no-image200x200.jpeg'
    }
  }
})