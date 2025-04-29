/**
 * @typedef {"object"|"array"|"string"|"number"|"boolean"|"null"} JsonType
 */

const jsonArea = document.getElementById("json-area");

function loadProvidedJson() {
    const params = new URLSearchParams(document.location.search);
    const providedJson = params.json

    if (providedJson && typeof providedJson === "string") {

    }
}

/**
 * @param {JsonType} type
 */
function createValue(type) {
    /**
     * @type {HTMLTemplateElement}
     */
    const template = document.getElementById(`json-${type}-template`)
    const node = template.content.cloneNode(true)
    jsonArea.appendChild(node)
}

for (const button of document.querySelectorAll(".add-element-button")) {
    const type = button.dataset.type
    button.addEventListener("click", () => createValue(type))
}