import './style.scss'




new Vue({
  el: '#homepageID',
  // data: {

  // },
  mounted: function () {


    $('#inputImageFileID').on('change', this.imgFileChanged.bind(this))

    // this.$refs.refExample.innerText = "adjfadjhf adlk fkdak f"

  },
  methods: {
    imgOnErrorHandle: e => {
      e.target.src = 'static/assets/images/no-image200x200.jpeg'
    },

    imgFileChanged: event => {
      console.log(event)
      const imageFile = event.target.files[0]

      const reader = new FileReader();

      const imgtag = $('#addMessageViewImage')
      imgtag.attr('title', imageFile.name)

      reader.onload = function (event) {
        imgtag.attr('src', event.target.result)
      };

      reader.readAsDataURL(imageFile);


    }
  }
})