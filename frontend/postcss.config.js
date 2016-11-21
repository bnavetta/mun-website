module.exports = (ctx) => ({
	plugins: {
		'autoprefixer': {
			browsers: ['last 2 versions']
		},
		'postcss-flexbugs-fixes': {},
		'postcss-reporter': {},
		'postcss-browser-reporter': {}
	}
});