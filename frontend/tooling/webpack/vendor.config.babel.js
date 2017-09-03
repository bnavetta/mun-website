import path from 'path';
import webpack from 'webpack';
import compose from 'lodash/fp/compose';

import config from '../config';
import stats from './partials/stats';
import out from './partials/out';
import env from './partials/env';
import uglify from './partials/uglify';

export default compose(env, uglify, stats(config.dllStatsFile), out)({
    entry: config.dlls,

    output: {
        filename: '[name].[chunkhash].bundle.js',
        library: '[name]_lib'
    },

    plugins: [
        new webpack.DllPlugin({
            path: path.join(config.paths.dist, '[name].dll.json'),
            name: '[name]_lib'
        }),
    ],
});
