// TODO: read alphabet from field
const alphabet = "0123456789abcdef"

const base1Element = document.getElementById("input-base");
const base2Element = document.getElementById("output-base");
const number1Element = document.getElementById("input-box");
const number2Element = document.getElementById("output-box");

function showError(error) {
    // TODO: nicer error message
    alert(error)
}

function parseNumber(input, base) {
    base = BigInt(base);

    const firstChar = input.charAt(0);
    const negative = firstChar === "-"
    if (firstChar === "-" || firstChar === "+") {
        input = input.substring(1)
    }

    let value = 0n;
    for (let i = 0; i < input.length; i++) {
        const digit = BigInt(alphabet.indexOf(input.charAt(i)));
        if (digit === -1n || digit >= base) {
            showError("Unexpected digit: " + input.charAt(i))
            return
        }
        value = value * base + digit
    }

    if (negative) value = -value
    return value;
}

function printNumber(number, base) {
    number = BigInt(number);
    base = BigInt(base);

    if (number === 0n) return "0"
    const negative = number < 0
    if (negative) {
        number = -number
    }

    let value = "";
    while (number !== 0n) {
        value = alphabet.charAt(Number(number % base)) + value
        number /= base
    }

    if (negative) {
        value = "-" + value;
    }
    return value;
}

document.getElementById("convert-button").onclick = () => {
    const parsed = parseNumber(
        number1Element.value.toLowerCase().trim(),
        base1Element.value
    );
    number2Element.value = printNumber(
        parsed,
        base2Element.value
    );
}

document.getElementById("reverse-convert-button").onclick = () => {
    const parsed = parseNumber(
        number2Element.value.toLowerCase().trim(),
        base2Element.value
    );
    number1Element.value = printNumber(
        parsed,
        base1Element.value
    );
}