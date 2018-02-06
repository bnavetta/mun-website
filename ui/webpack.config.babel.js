import os from "os";
import path from "path";

import CleanWebpackPlugin from "clean-webpack-plugin";
import ExtractTextPlugin from "extract-text-webpack-plugin";
import ManifestPlugin from "webpack-manifest-plugin";
import StylelintWebpackPlugin from "stylelint-webpack-plugin";
import UglifyJsPlugin from "uglifyjs-webpack-plugin";
import webpack from "webpack";
import _ from "lodash";

import config from "./tools/config";

function entryPoint(...files) {
    return config.isDev
        ? [...files, "webpack-hot-middleware/client?dynamicPublicPath=true"]
        : files;
}

console.log(`Building for ${config.env}`);
const outputDir = path.resolve(path.join("build", config.env));

const webpackConfig = {
    cache: true,
    devtool: config.isDev ? "eval" : undefined,

    entry: {
        main: entryPoint(path.resolve("src/main/index.js")),
        home: entryPoint(path.resolve("src/home/index.js")),
        committees: entryPoint(path.resolve("src/committees/index.js")),
        "info-page": entryPoint(path.resolve("src/info-page/index.js")),
        vendor: ["react", "react-dom", "lodash"],
    },

    output: {
        path: outputDir,
        publicPath: config.isDev
            ? `http://localhost:${config.port}/assets/`
            : "/assets/",
        filename: `[name].[${config.isDev ? "hash" : "chunkhash"}].js`,
        chunkFilename: "[name].[chunkhash].chunk.js",
    },

    module: {
        rules: [
            {
                test: /.jsx?/,
                exclude: /node_modules/,
                use: [
                    { loader: "cache-loader" },
                    {
                        loader: "thread-loader",
                        options: {
                            workers: os.cpus().length - 2,
                        },
                    },
                    {
                        loader: "babel-loader",
                        options: {
                            cacheDirectory: true,
                        },
                    },
                ],
            },
            {
                test: /.css/,
                exclude: /node_modules/,
                use: config.isDev
                    ? [
                          { loader: "cache-loader" },
                          { loader: "style-loader" },
                          {
                              loader: "css-loader",
                              options: {
                                  importLoaders: 1,
                              },
                          },
                          { loader: "postcss-loader" },
                      ]
                    : ExtractTextPlugin.extract({
                          fallback: "style-loader",
                          use: [
                              {
                                  loader: "css-loader",
                                  options: {
                                      importLoaders: 1,
                                  },
                              },
                              { loader: "postcss-loader" },
                          ],
                      }),
            },
        ],
    },

    resolve: {
        extensions: [".jsx", ".js"],
    },

    plugins: _.compact([
        new StylelintWebpackPlugin(),

        new ManifestPlugin({
            writeToFileEmit: config.isDev,
            generate: (seed, files) => {
                return _.chain(files)
                    .groupBy(f => f.chunk.name)
                    .mapValues(entries => entries.map(_.property("path")));
            },
        }),

        ...(config.isDev
            ? [
                  // Dev plugins
                  new CleanWebpackPlugin([outputDir]),
                  new webpack.NamedModulesPlugin(),
                  new webpack.HotModuleReplacementPlugin(),
              ]
            : [
                  // Prod plugins
                  new webpack.HashedModuleIdsPlugin(),
                  new UglifyJsPlugin(),
                  new ExtractTextPlugin("[name].[chunkhash].css"),
              ]),

        // Dependencies
        new webpack.optimize.CommonsChunkPlugin({
            name: "vendor",
        }),

        // Webpack boilerplate and manifest
        new webpack.optimize.CommonsChunkPlugin({
            name: "manifest",
        }),
    ]),
};

export default webpackConfig;
