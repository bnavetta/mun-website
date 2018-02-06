import express from "express";
import cors from "cors";
import webpack from "webpack";
import webpackDevMiddleware from "webpack-dev-middleware";
import webpackHotMiddleware from "webpack-hot-middleware";

import config from "./config";
import webpackConfig from "../webpack.config.babel";

const compiler = webpack(webpackConfig);

const app = express();

const corsMod = cors({
    options: (origin, callback) => callback(null, true),
});

app.use(corsMod);
app.options("*", corsMod);

console.log(`Public path: ${webpackConfig.output.publicPath}`);

const devMiddleware = webpackDevMiddleware(compiler, {
    logLevel: "trace",
    publicPath: webpackConfig.output.publicPath,
});

app.use(devMiddleware);
app.use(webpackHotMiddleware(compiler));

// Webpack Hot Middleware with assets on a different host from the page is a PITA
app.get("/assets//__webpack_hmr", function(req, res) {
    return res.redirect(301, "/__webpack_hmr");
});

app.listen(config.port, () => console.log(`Listening on port ${config.port}`));
