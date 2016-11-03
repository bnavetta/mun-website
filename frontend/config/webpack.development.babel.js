import webpack from 'webpack';
import Config from 'webpack-config';

import entrypoints from './entrypoints';

export default new Config().extend('config/webpack.base.babel.js').merge({
	devtool: 'cheap-module-eval-sourcemap',

	entry: entrypoints(['react-hot-loader/patch', 'webpack-hot-middleware/client', /* 'babel-polyfill' */]),

	output: {
		filename: '[name].js',
		chunkFilename: '[name].chunk.js',
		publicPath: 'http://localhost:8080',
	},

	plugins: [
		new webpack.LoaderOptionsPlugin({
			minimize: false,
			debug: true,
		}),
		new webpack.NamedModulesPlugin(),
		new webpack.HotModuleReplacementPlugin(),
	],
});