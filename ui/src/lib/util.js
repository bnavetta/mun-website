/**
 * Look up a global CSS variable value
 * @param varName the name of the variable, including the leading `--`
 * @returns {string} the variable value
 */
export function getVariable(varName) {
    const root = document.querySelector(':root');
    return getComputedStyle(root).getPropertyValue(varName);
}