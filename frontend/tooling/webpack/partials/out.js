import output from 'webpack-partial/output';

import config from '../../config';

export default output({
    publicPath: '/',
    path: config.paths.dist,
});
