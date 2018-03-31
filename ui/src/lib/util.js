import { mergeDeepLeft } from "ramda";

/**
 * Look up a global CSS variable value
 * @param varName the name of the variable, including the leading `--`
 * @returns {string} the variable value
 */
export function getVariable(varName) {
    const root = document.querySelector(':root');
    return getComputedStyle(root).getPropertyValue(varName);
}

/**
 * Headers to include for CSRF protection.
 */
export let csrfHeaders = {};

window.addEventListener('DOMContentLoaded', () => {
    const csrfHeader = document.querySelector('meta[name=_csrf_header]').getAttribute('content');
    const csrfToken = document.querySelector('meta[name=_csrf]').getAttribute('content');
    csrfHeaders[csrfHeader] = csrfToken;
});

export async function request(url, options = {}) {
    const response = await fetch(url, mergeDeepLeft(options, {
        headers: csrfHeaders,
        credentials: 'same-origin'
    }));

    if (!response.headers.get('Content-Type').startsWith('application/json')) {
        const body = await response.text();
        throw new Error(`Not JSON: ${body}`);
    }

    const body = await response.json();

    if (response.ok)  {
        return body;
    } else {
        throw new Error(body.error);
    }
}