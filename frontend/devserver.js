const express = require('express');
const fs = require('fs');
const path = require('path');
const proxy = require('http-proxy-middleware');
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
	});

	app.use(middleware);
	app.use(hotMiddleware(compiler));

	const mfs = middleware.fileSystem;
	app.use((req, res, next) => {
		fs.writeFileSync('dist/stats.json', mfs.readFileSync(path.join(compiler.outputPath, 'stats.json')));
		next();
	});
} else {
	app.use(express.static(path.resolve('build')));
}

app.use(proxy({ target: 'http://localhost:8080' }))

app.listen(port, (err) => {
	if (err) {
		console.error(err.message);
		return;
	}
	console.log(`App started on http://localhost:${port}`);
});