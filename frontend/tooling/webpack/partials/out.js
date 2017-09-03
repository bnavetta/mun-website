import output from 'webpack-partial/output';

import config from '../../config';

export default (conference) => {
    return output({
        publicPath: '/',
        path: config.distPath(conference)
    });
}
