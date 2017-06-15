import plugin from 'webpack-partial/plugin';
// import { StatsWriterPlugin } from 'webpack-stats-plugin';
import AssetManifestPlugin from './AssetManifestPlugin';

export default (filename) => plugin(new AssetManifestPlugin({
    filename,
}));