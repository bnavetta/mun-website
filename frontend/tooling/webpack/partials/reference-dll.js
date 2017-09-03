import fs from 'fs';
import path from 'path';
import webpack from 'webpack';
import plugin from 'webpack-partial/plugin';

import config from '../../config';

export default function referenceDll(conference, dllName) {
    const manifestFile = path.join(config.distPath(conference), `${dllName}.dll.json`);
    if (!fs.existsSync(manifestFile)) {
        throw new Error(`DLL ${dllName} does not have a manifest (missing ${manifestFile})`);
    }

    const referencePlugin = new webpack.DllReferencePlugin({
        context: '.',
        manifest: require(manifestFile),
    });

    return plugin(referencePlugin);
}