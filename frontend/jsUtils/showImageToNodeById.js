export default function (imageFile, id) {
  const reader = new FileReader();
  const imgtag = $(id)
  // imgtag.attr('title', imageFile.name)
  reader.onload = function (event) {
    imgtag.attr('src', event.target.result)
  };
  reader.readAsDataURL(imageFile);
}