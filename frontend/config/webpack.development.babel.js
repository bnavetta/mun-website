import webpack from 'webpack';
import Config from 'webpack-config';

import entrypoints from './entrypoints';

export default new Config().extend('config/webpack.base.babel.js').merge({
	devtool: 'cheap-module-eval-sourcemap',

	// Need to override path to not include extra slash
	// publicPath has to end with a slash or other things break
	entry: entrypoints(['react-hot-loader/patch', 'webpack-hot-middleware/client?dynamicPublicPath=true&path=__webpack_hmr']),

	output: {
		filename: '[name].js',
		chunkFilename: '[name].chunk.js',
		publicPath: 'http://localhost:8000/',
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