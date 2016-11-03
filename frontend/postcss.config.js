module.exports = (ctx) => ({
	plugins: {
		// 'postcss-import': { addDependencyTo: ctx.webpack },
		'postcss-smart-import': { addDependencyTo: ctx.webpack },
		'postcss-cssnext': {
			features: {
				customProperties: {
					variables: {}
				}
			}
		},
		'postcss-reporter': {},
		'postcss-browser-reporter': {}
	}
});