/**
 * Look up a global CSS variable value
 * @param varName the name of the variable, including the leading `--`
 * @returns {string} the variable value
 */
export function getVariable(varName) {
    const root = document.querySelector(':root');
    return getComputedStyle(root).getPropertyValue(varName);
}

export let csrfHeaders = {};

window.addEventListener('DOMContentLoaded', () => {
    const csrfHeader = document.querySelector('meta[name=_csrf_header]').getAttribute('content');
    const csrfToken = document.querySelector('meta[name=_csrf]').getAttribute('content');
    csrfHeaders[csrfHeader] = csrfToken;
});