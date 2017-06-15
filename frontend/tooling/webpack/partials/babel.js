import update from 'lodash/fp/update';

export default update(['module', 'rules'], (rules = []) => ([
    ...rules,
    {
        test: /\.jsx?/,
        use: ['babel-loader'],
        exclude: /node_modules/,
    },
]));