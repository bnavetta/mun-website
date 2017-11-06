import output from 'webpack-partial/output';

import config from '../../config';

export default output({
    publicPath: config.production ? '/' : `http://localhost:${process.env.PORT || 8000}/`,
    path: config.paths.dist
});