import webpack from 'webpack';
import plugin from 'webpack-partial/plugin';
import identity from 'lodash/fp/identity';

import config from '../../config';

const uglify = config.production ? plugin(new webpack.optimize.UglifyJsPlugin({
    beautify: false,
    mangle: {
        screw_ie8: true,
        keep_fnames: true,
    },
    compress: {
        screw_ie8: true,
    },
    comments: false,
    sourceMap: true,
})) : identity;

export default uglify;