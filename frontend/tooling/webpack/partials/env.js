import webpack from 'webpack';
import plugin from 'webpack-partial/plugin';
import compose from 'lodash/fp/compose';

import config from '../../config';

const nodeEnv = plugin(new webpack.EnvironmentPlugin({
    // These are defaults, not fixed values
    NODE_ENV: 'development',
    DEBUG: true
}));

const webpackOpts = plugin(new webpack.LoaderOptionsPlugin({
    minimize: config.production,
    debug: !config.production,
}));

export default compose(nodeEnv, webpackOpts);