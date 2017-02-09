const path = require('path');const CommonsChunkPlugin = require('webpack/lib/optimize/CommonsChunkPlugin');

module.exports = {
	entry: {
		bootstrap: [path.resolve('./src/bootstrap/index.js')],

		registration: ['babel-polyfill', path.resolve('./src/registration/index.jsx')],

		admin: ['babel-polyfill', path.resolve('./src/admin/index.js')],
		committeeAdmin: ['babel-polyfill', path.resolve('./src/admin/committee/index.jsx')],
		schoolAdmin: ['babel-polyfill', path.resolve('./src/admin/school/index.jsx')],
	},
	features: {
		cssModules: false,
		sass: true
	},
	customizeWebpack: function (config) {
		config.plugins.push(new CommonsChunkPlugin({
			name: 'admin',
			chunks: ['admin', 'committeeAdmin', 'schoolAdmin'],
			minChunks: 2
		}));

		return config;
	}
};
