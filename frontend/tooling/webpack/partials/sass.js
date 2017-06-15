import plugin from 'webpack-partial/plugin';
import update from 'lodash/fp/update';
import compose from 'lodash/fp/compose';
import ExtractTextPlugin from 'extract-text-webpack-plugin';

import config from '../../config';

let loader = [
    'style-loader',
    'css-loader?sourceMap&importLoaders=1',
    'sass-loader'
];

if (config.production) {
    loader = ExtractTextPlugin.extract({
        fallback: 'style-loader',
        use: [
            'css-loader?importLoaders=1',
            'sass-loader'
        ]
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
