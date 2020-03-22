
/*

TO DO : Call this method on main layout.


*/


export default function () {
  $('[id]').each(function () {
    const ids = $('[id="' + this.id + '"]');
    if (ids.length > 1 && ids[0] == this)
      console.error('Multiple IDs #' + this.id);
  });
}