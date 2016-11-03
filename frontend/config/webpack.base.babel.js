import path from 'path';
import webpack from 'webpack';
import Config from 'webpack-config';
import ExtractTextWebpackPlugin from 'extract-text-webpack-plugin';
import { StatsWriterPlugin } from 'webpack-stats-plugin';

export default new Config().merge({
	context: process.cwd(),

	output: {
		path: path.resolve('dist'),
	},

	module: {
		loaders: [
			{
				test: /\.css$/,
				loader: ExtractTextWebpackPlugin.extract({ fallbackLoader: 'style-loader', loader: 'css-loader?sourceMap' }),
				include: /node_modules/,
			},

			{
				test: /\.css$/,
				loader: ExtractTextWebpackPlugin.extract({
					fallbackLoader: 'style-loader',
					loader: 'css-loader?sourceMap&importLoaders=1!postcss-loader',
				}),
				exclude: /node_modules/,
			},

			{ test: /\.jsx?$/, loader: 'babel-loader', exclude: /node_modules/ },

				{ test: /.json$/, loader: 'json-loader' },
				{ test: /\.(png|jpeg|jpg|gif|svg|woff|woff2)$/, loader: 'url-loader?limit=10000' },
				{ test: /\.(eot|ttf|wav|mp3)$/, loader: 'file-loader' },
		],
	},

	plugins: [
		new webpack.optimize.CommonsChunkPlugin({
			name: 'common',
		}),
		new webpack.NoErrorsPlugin(),
		new webpack.DefinePlugin({
			'process.env.NODE_ENV': JSON.stringify(process.env.NODE_ENV)
		}),
		new ExtractTextWebpackPlugin('[name].[chunkhash].css'),
		new StatsWriterPlugin({
			filename: 'stats.json'
		}),
	],
});