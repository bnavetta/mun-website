import path from 'path';
import express from 'express';
import webpack from 'webpack';
import webpackDevMiddleware from 'webpack-dev-middleware';
import webpackHotMiddleware from 'webpack-hot-middleware';
import DashboardPlugin from 'webpack-dashboard/plugin';

import webpackConfig from './webpack/webpack.config.babel';
import config from './config';

const compiler = webpack(webpackConfig);
compiler.apply(new DashboardPlugin());

const app = express();

const devMiddleware = webpackDevMiddleware(compiler, {
    noInfo: true,
    publicPath: webpackConfig.output.publicPath,
})

app.use(devMiddleware);

const fs = devMiddleware.fileSystem;

app.use(webpackHotMiddleware(compiler));

app.use(express.static(config.paths.dist));

app.get('*', (req, res) => {
    fs.readFile(path.join(compiler.outputPath, 'index.html'), (err, file) => {
        if (err) {
            res.sendStatus(404);
        } else {
            res.set('Content-Type', 'text/html');
            res.send(file)
        }
    });
})

app.listen(process.env.PORT || 8000);