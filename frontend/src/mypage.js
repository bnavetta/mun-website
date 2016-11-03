import answer from './common';
import React from 'react';

const node = new HTMLDivElement();
node.textContent = `Hello, World! The answer is ${answer()}`;

document.body.appendChild(node);

console.log(React);

