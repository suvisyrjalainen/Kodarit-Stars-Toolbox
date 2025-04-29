/**
 * @typedef {"object"|"array"|"string"|"number"|"boolean"|"null"} JsonType
 */

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
    switch (type) {
        case "string":
            return template.content.cloneNode(true)
    }
}

const s = createValue("string")
document.getElementById("json-area")
    .appendChild(s)