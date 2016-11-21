const express = require('express');
const fs = require('fs');
const path = require('path');
require('babel-register');

const isDev = process.env.NODE_ENV !== 'production';
const port = process.env.PORT || 8000;

const app = express();

if (isDev) {
	if (!fs.existsSync('dist')) {
		fs.mkdirSync('dist');
	}

	const webpack = require('webpack');
	const devMiddleware = require('webpack-dev-middleware');
	const hotMiddleware = require('webpack-hot-middleware');
	const DashboardPlugin = require('webpack-dashboard/plugin');

	const config = require('./webpack.config.babel').default;

	const compiler = webpack(config);
	compiler.apply(new DashboardPlugin());

	const middleware = devMiddleware(compiler, {
		publicPath: config.output.publicPath,
		reporter({ state, stats, options }) {
			if (state) {
				options.log(stats.toString(options.stats));

				options.log("webpack: bundle is now VALID.");

				const jsonStats = stats.toJson();
				fs.writeFileSync('dist/stats.json', JSON.stringify({ assetsByChunkName: jsonStats.assetsByChunkName }));
			} else {
				options.log("webpack: bundle is now INVALID.");
			}
		},
		headers: {
			'Access-Control-Allow-Origin': '*'
		},
	});

	app.use(middleware);
	app.use(hotMiddleware(compiler));
} else {
	app.use(express.static(path.resolve('build')));
}

// app.use(proxy({ target: 'http://localhost:8080' }))

app.listen(port, (err) => {
	if (err) {
		console.error(err.message);
		return;
	}
	console.log(`App started on http://localhost:${port}`);
});