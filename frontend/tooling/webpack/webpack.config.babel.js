import webpack from 'webpack';
import compose from 'lodash/fp/compose';
import partial from 'lodash/fp/partial';
import map from 'lodash/fp/map';
import { createVariants } from 'parallel-webpack';

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

function createConfig(options) {
    const referenceDlls = map(partial(referenceDll, [options.conference]), Object.keys(config.dlls));
    const partials = [hot, uglify, env, babel, sass(options.conference), css, stats('asset-manifest.json'), ...referenceDlls, out(options.conference)];

    return compose(partials)({
        entry: config.entry,

        devtool: 'cheap-module-source-map',

        output: {
            filename: '[name].[hash].js',
            chunkFilename: '[name].[hash].chunk.js'
        },

        resolve: {
            extensions: ['.jsx', '.js'],
        },

        plugins: [
            new webpack.NamedModulesPlugin(),
            new webpack.NoEmitOnErrorsPlugin(),
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

}

export default createVariants({}, config.buildVariants, createConfig);