import path from 'path';
import webpack from 'webpack';
import compose from 'lodash/fp/compose';
import { createVariants } from 'parallel-webpack';

import config from '../config';
import stats from './partials/stats';
import out from './partials/out';
import env from './partials/env';
import uglify from './partials/uglify';

function createConfig(options) {
    return compose(env, uglify, stats(config.dllStatsFile), out(options.conference))({
        entry: config.dlls,

        output: {
            filename: '[name].[chunkhash].bundle.js',
            library: '[name]_lib'
        },

        plugins: [
            new webpack.DllPlugin({
                path: path.join(config.distPath(options.conference), '[name].dll.json'),
                name: '[name]_lib'
            }),
        ],
    });
}

export default createVariants({}, config.buildVariants, createConfig);
