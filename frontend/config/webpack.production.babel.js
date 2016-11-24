import webpack from 'webpack';
import Config from 'webpack-config';

import entrypoints from './entrypoints';

export default new Config().extend('config/webpack.base.babel.js').merge({
	entry: entrypoints([]),
	output: {
		filename: '[name].[chunkhash].js',
		chunkFilename: '[name].chunkhash.js'
	},
	plugins: [
		new webpack.LoaderOptionsPlugin({
			minimize: true,
			debug: false,
		}),
		new webpack.optimize.AggressiveMergingPlugin(),
		new webpack.optimize.DedupePlugin(),
		new webpack.optimize.UglifyJsPlugin()
	]
});