import update from 'lodash/fp/update';

export default update(['module', 'rules'], (rules = []) => ([
    ...rules,
    {
        enforce: 'pre',
        test: /\.jsx?/,
        loader: 'source-map-loader'
    },
    {
        enforce: 'pre',
        test: /\.tsx?/,
        loader: 'source-map-loader'
    },
]));