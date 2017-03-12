#!/usr/bin/env node
const juice = require('juice');
const fs = require('fs');
const path = require('path');

const cssDir = path.resolve('./dist');

let siteCss = '';
for (let file of fs.readdirSync(cssDir, 'utf-8')) {
    if (file.endsWith('.css')) {
        console.log('Using CSS from file', file);
        siteCss += fs.readFileSync(path.join(cssDir, file), 'utf-8');
        siteCss += '\n\n';
    }
}

const emailDir = path.resolve('../core/src/main/resources/emails');

for (var file of fs.readdirSync(emailDir, 'utf-8')) {
    if (!file.endsWith('.html')) {
        continue;
    }
    console.log('Processing', file);
    var html = fs.readFileSync(path.join(emailDir, file), 'utf-8');
    var inlined = juice(html, {
        extraCss: siteCss,
        preserveMediaQueries: false,
        preserveFontFaces: false,
    });

    var output = path.join(emailDir, file.substring(0, file.length - path.extname(file).length)) + '.email';
    fs.writeFileSync(output, inlined);
}