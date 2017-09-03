import plugin from 'webpack-partial/plugin';
import update from 'lodash/fp/update';
import compose from 'lodash/fp/compose';
import ExtractTextPlugin from 'extract-text-webpack-plugin';

import config from '../../config';

const baseLoaders = [
    'css-loader?sourceMap&importLoaders=1',
    {
        loader: 'sass-loader',
        options: {
            data: '$conference: ' + config.conference + ';'
        }
    }
];

let loader = [
    'style-loader',
    ...baseLoaders
];

if (config.production) {
    loader = ExtractTextPlugin.extract({
        fallback: 'style-loader',
        use: baseLoaders
    })
}

const sassLoader = update(['module', 'rules'], (rules = []) => ([
    ...rules,
    {
        test: /\.scss/,
        use: loader,
    }
]));

const partial = config.production ? compose(sassLoader, plugin(new ExtractTextPlugin('[name].[hash].css'))) : sassLoader;

export default partial;
