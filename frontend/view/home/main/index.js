import './style.scss'
import showImageToNodeById from '@/jsUtils/showImageToNodeById'

$('#homepageID').ready(function () {

  $('#inputImageFileID').on('change', event => {
    const imageFile = event.target.files[0]
    showImageToNodeById(imageFile, '#addMessageViewImage')

  })

})
