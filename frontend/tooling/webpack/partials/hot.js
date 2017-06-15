import webpack from 'webpack';
import entry from 'webpack-partial/entry';
import plugin from 'webpack-partial/plugin';
import compose from 'lodash/fp/compose';
import identity from 'lodash/fp/identity';

import config from '../../config';

const entryPoint = entry.prepend(['react-hot-loader/patch', 'webpack-hot-middleware/client']);
const hotPlugin = plugin(new webpack.HotModuleReplacementPlugin());

export default config.production ? identity : compose(entryPoint, hotPlugin);