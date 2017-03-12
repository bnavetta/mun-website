const path = require('path');
const CommonsChunkPlugin = require('webpack/lib/optimize/CommonsChunkPlugin');
const ProvidePlugin = require('webpack/lib/ProvidePlugin');

module.exports = {
    entry: {
        bootstrap: [path.resolve('./src/bootstrap/index.js')],

        registration: ['babel-polyfill', path.resolve('./src/registration/index.jsx')],
        yourbusun: ['babel-polyfill', path.resolve('./src/yourbusun/index.jsx')],

        public: [path.resolve('./src/public/index.js')],

        admin: ['babel-polyfill', path.resolve('./src/admin/index.js')],
        committeeAdmin: ['babel-polyfill', path.resolve('./src/admin/committee/index.jsx')],
        schoolAdmin: ['babel-polyfill', path.resolve('./src/admin/school/index.jsx')],
    },
    features: {
        cssModules: false,
        sass: true
    },
    assetPatterns: [/\.otf$/],
    customizeWebpack: function (config) {
        config.plugins.push(new CommonsChunkPlugin({
            name: 'admin',
            chunks: ['admin', 'committeeAdmin', 'schoolAdmin'],
            minChunks: 2
        }));

        config.plugins.push(new ProvidePlugin({
            'Tether': 'tether'
        }));

        return config;
    }
};
