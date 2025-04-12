const alue = document.getElementById('alue')
const file = document.getElementById('file')
const statusText = document.getElementById('status')
file.onchange = event => {
    const tied = event.target.files[0]
    if (!tied) return
    const fr = new FileReader()
    fr.onload = () => {
        while (statusText.firstChild) statusText.removeChild(statusText.firstChild)
        statusText.appendChild(document.createTextNode('Valmis '))
        const valmisButton = document.createElement('button')
        valmisButton.onclick = () => navigator.clipboard.writeText(fr.result)
        valmisButton.appendChild(document.createTextNode('kopioi'))
        statusText.appendChild(valmisButton)
        while (alue.firstChild) alue.removeChild(alue.firstChild)
        alue.appendChild(document.createTextNode(fr.result))
        alue.style.display = 'block'
    }
    fr.readAsDataURL(tied)
    while (statusText.firstChild) statusText.removeChild(statusText.firstChild)
    statusText.appendChild(document.createTextNode('Odota...'))
}