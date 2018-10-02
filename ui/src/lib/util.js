import Raven from "raven-js";
import { mergeDeepLeft } from "ramda";
import Noty from "noty";

/**
 * Look up a global CSS variable value
 * @param varName the name of the variable, including the leading `--`
 * @returns {string} the variable value
 */
export function getVariable(varName) {
    const root = document.querySelector(":root");
    return getComputedStyle(root).getPropertyValue(varName);
}

/**
 * Headers to include for CSRF protection.
 */
export let csrfHeaders = {};

window.addEventListener("DOMContentLoaded", () => {
    const csrfHeader = document
        .querySelector("meta[name=_csrf_header]")
        .getAttribute("content");
    const csrfToken = document
        .querySelector("meta[name=_csrf]")
        .getAttribute("content");
    csrfHeaders[csrfHeader] = csrfToken;
});

export async function request(url, options = {}) {
    const response = await fetch(
        url,
        mergeDeepLeft(options, {
            headers: csrfHeaders,
            credentials: "same-origin",
        })
    );

    // Support non-JSON responses
    const contentType = response.headers.get("Content-Type");
    if (contentType === null || !contentType.startsWith("application/json")) {
        const body = await response.text();
        if (response.ok) {
            return body;
        } else {
            throw new Error(body);
        }
    }

    const body = await response.json();

    if (response.ok) {
        return body;
    } else {
        const exception = new Error(body.error);
        Raven.captureException(exception, { extra: { url } });
        throw exception;
    }
}

export function yesNo(value) {
    return value ? "Yes" : "No";
}

/**
 * Display an error popup to the user.
 * @param message the error message
 */
export function displayError(message) {
    new Noty({
        text: message,
        type: "error",
        animation: {
            open: "animated bounceInRight",
            close: "animated bounceOutRight",
        },
    }).show();
}
