/**
 * @typedef {"object"|"array"|"string"|"number"|"boolean"|"null"} JsonType
 */

/**
 * @typedef {string|number|boolean|null|JsonData[]|{ [key: string]: JsonData }} JsonData
 */

const jsonArea = document.getElementById("json-area");

function loadProvidedJson() {
    const params = new URLSearchParams(document.location.search);
    const providedJson = params.json

    if (providedJson && typeof providedJson === "string") {

    }
}

/**
 * Converts a POJO into a tree of html nodes
 * @param {JsonData} json
 * @return {HTMLElement}
 */
function jsonToTree(json) {
    // type null === "object"
    if (json === null) {
        return createValue("null")
    }
    // type [] === "object"
    if (Array.isArray(json)) {
        const node = createValue("array")
        for (const item of json) {
            addToContainer(node, jsonToTree(item))
        }
        return node
    }
    switch (typeof json) {
        case "string":
            const node = createValue("string")
            node.querySelector("input").value = json
            return node
        case "number":
            const numberNode = createValue("number")
            numberNode.querySelector("input").value = json
            return numberNode
        case "boolean":
            const booleanNode = createValue("boolean")
            booleanNode.querySelector("input").checked = json
            return booleanNode
    }
}

/**
 * Converts a tree of html nodes into a POJO for serialization
 * @param {HTMLElement} tree
 * @return {JsonData}
 */
function treeToJson(tree) {
    switch (tree.dataset.type) {
        case "string":
            return tree.querySelector("input").value
        case "number":
            const number = tree.querySelector("input").valueAsNumber
            if (Number.isNaN(number)) return 0
            return number
        case "boolean":
            return tree.querySelector("input").checked
        case "null":
            return null
        case "array":
            return tree.querySelectorAll(".json-array-content > li > .json-element")
                .values()
                .map(treeToJson)
                .toArray()
    }
}

function loadNodeFromDrag(event) {
    // noinspection JSValidateTypes
    /**
     * @type {HTMLElement}
     */
    const node = event.dataTransfer.getData("application/x-moz-node")
    if (node instanceof HTMLElement) {
        return node
    }
    const id = event.dataTransfer.getData("application/x.stars-json-element-id")
    const nodeById = document.getElementById(id)
    if (nodeById) {
        return nodeById
    }

    const jsonData = event.dataTransfer.getData("application/json")
        || event.dataTransfer.getData("text/plain")
    try {
        const json = JSON.parse(jsonData)
        return jsonToTree(json)
    } catch (e) {
        console.warn("Failed to parse JSON data", e)
        return undefined
    }
}

/**
 * @param {HTMLElement} element
 */
function addDragHandlers(element) {
    element.draggable = true
    element.addEventListener("dragstart", event => {
        // noinspection JSCheckFunctionSignatures
        event.dataTransfer.setData("application/x-moz-node", element)
        event.dataTransfer.setData("application/x.stars-json-element-id", element.id)
        const jsonData = JSON.stringify(treeToJson(element))
        event.dataTransfer.setData("application/json", jsonData)
        event.dataTransfer.setData("text/plain", jsonData)
        event.dataTransfer.effectAllowed = "move"
        element.classList.add("dragging")
    })
    element.addEventListener("dragend", () => {
        element.classList.remove("dragging")
    })

    if (element.dataset.type === "array") {
        const dragZone = element.querySelector(".drag-info");
        dragZone.addEventListener("dragover", event => {
            event.preventDefault()
            event.dataTransfer.dropEffect = "move"
        })
        dragZone.addEventListener("drop", event => {
            event.preventDefault()
            const node = loadNodeFromDrag(event);
            if (node) {
                node.remove()
                element.querySelector(".json-array-content").appendChild(node)
            }
        })
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
    /**
     * @type {HTMLElement}
     */
    const node = template.content.cloneNode(true).children[0]
    node.id = "json-element-" + Math.floor(Math.random() * 10000000)
    addDragHandlers(node)
    return node
}

/**
 * @param {HTMLElement} container
 * @param {HTMLElement} node
 */
function addToContainer(container, node) {
    const type = container.dataset.type
    if (type === "array") {
        const content = container.querySelector(".json-array-content")
        if (!content) {
            return
        }
        const li = document.createElement("li")
        li.appendChild(node)
        content.appendChild(li)
    }
}

// Setup buttons
for (const button of document.querySelectorAll(".add-element-button")) {
    /**
     * @type {JsonType}
     */
    const type = button.dataset.type
    button.addEventListener("click", () => jsonArea.appendChild(createValue(type)))
    button.addEventListener("dragstart", event => {
        let jsonData;
        switch (type) {
            case "null":
                jsonData = "null"
                break
            case "boolean":
                jsonData = "false"
                break
            case "string":
                jsonData = "\"\""
                break
            case "number":
                jsonData = "0"
                break
            case "object":
                jsonData = "{}"
                break
            case "array":
                jsonData = "[]"
                break
        }
        // Clear default dragging from button + image
        event.dataTransfer.items.clear()

        event.dataTransfer.setData("application/json", jsonData)
        event.dataTransfer.setData("text/plain", jsonData)
    })
}

// Test json
const array = createValue("array")
jsonArea.appendChild(array);
addToContainer(array, createValue("number"))
