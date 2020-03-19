export default function (byteArray) {
  const TYPED_ARRAY = new Uint8Array(byteArray)
  const str = TYPED_ARRAY.reduce((acc, cur) => acc + String.fromCharCode(cur), '')
  const base64String = btoa(str)
  return 'data:image/jpg;base64,' + base64String
}