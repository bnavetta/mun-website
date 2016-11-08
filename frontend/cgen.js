const materialColors = require('material-colors');

let colors = ':root {\n';

for (const color of Object.keys(materialColors)) {
	colors += `    /* ${color} */\n`;
	for (const level of Object.keys(materialColors[color])) {
		colors += `    --color-${color}-${level}: ${materialColors[color][level]};\n`;
	}
	colors += '\n';
}

colors += '}\n';

console.log(colors);