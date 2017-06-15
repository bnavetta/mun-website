import plugin from 'webpack-partial/plugin';
import update from 'lodash/fp/update';
import compose from 'lodash/fp/compose';
import ExtractTextPlugin from 'extract-text-webpack-plugin';

import config from '../../config';

let loader = [
    'style-loader',
    'css-loader?sourceMap&',
];

if (config.production) {
    loader = ExtractTextPlugin.extract({
        fallback: 'style-loader',
        use: [
            'css-loader',
        ]
    })
}

const cssLoader = update(['module', 'rules'], (rules = []) => ([
    ...rules,
    {
        test: /\.css$/,
        use: loader,
    }
]));

const partial = config.production ? compose(cssLoader, plugin(new ExtractTextPlugin('[name].[hash].css'))) : cssLoader;

export default partial;
