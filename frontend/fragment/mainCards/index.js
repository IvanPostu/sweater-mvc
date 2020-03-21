

$('#mainCardsID').ready(function () {

  $('#mainCardsID img.mainCardImg').each(function (index, node) {
    $('<img />').attr('src', $(node).attr('src')).one('error', function () {
      $(node).attr('src', '/static/assets/images/no-image200x200.jpeg')
    })
  })

})

