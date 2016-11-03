const express = require('express');
const path = require('path');

const isDev = process.env.NODE_ENV !== 'production';
const port = process.env.PORT || 8080;

const app = express();

if (isDev) {
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
} else {
	app.use(express.static(path.resolve('build')));
}

app.listen(port, (err) => {
	if (err) {
		console.error(err.message);
		return;
	}
	console.log(`App started on http://localhost:${port}`);
});