import webpack from 'webpack';
import compose from 'lodash/fp/compose';
import map from 'lodash/fp/map';

import config from '../config';
import out from './partials/out';
import hot from './partials/hot';
import babel from './partials/babel';
import css from './partials/css';
import sass from './partials/sass';
import referenceDll from './partials/reference-dll';
import uglify from './partials/uglify';
import env from './partials/env';
import stats from './partials/stats';

import HardSourceWebpackPlugin from 'hard-source-webpack-plugin';

const partials = [out, hot, uglify, env, babel, sass, css, stats('asset-manifest.json'), ...map(referenceDll, Object.keys(config.dlls))];

export default compose(partials)({
    entry: config.entry,

    devtool: 'cheap-module-source-map',

    output: {
        filename: '[name].[hash].js',
        chunkFilename: '[name].[hash].chunk.js',
    },

    resolve: {
        extensions: ['.jsx', '.js'],
    },

    plugins: [
        new webpack.NamedModulesPlugin(),
        new webpack.NoEmitOnErrorsPlugin(),
        new HardSourceWebpackPlugin(),
        new webpack.optimize.CommonsChunkPlugin({
            name: 'admin',
            chunks: ['admin', 'committeeAdmin', 'schoolAdmin'],
            minChunks: 2
        }),
        new webpack.ProvidePlugin({
            'Tether': 'tether'
        })

    ]
});
