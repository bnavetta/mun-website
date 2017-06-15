import path from 'path';

export default {
    production: process.env.NODE_ENV == 'production',
    paths: {
        dist: path.resolve(path.join('dist', process.env.NODE_ENV)),
    },
    entry: {
        bootstrap: [path.resolve('./src/bootstrap/index.js')],

        registration: ['babel-polyfill', path.resolve('./src/registration/index.jsx')],
        yourbusun: ['babel-polyfill', path.resolve('./src/yourbusun/index.jsx')],

        public: [path.resolve('./src/public/index.js')],

        admin: ['babel-polyfill', path.resolve('./src/admin/index.js')],
        committeeAdmin: ['babel-polyfill', path.resolve('./src/admin/committee/index.jsx')],
        schoolAdmin: ['babel-polyfill', path.resolve('./src/admin/school/index.jsx')],
    },
    dllStatsFile: 'dll.json',
    dlls: {
        react: ['react', 'react-dom', 'html-entities', 'strip-ansi']
    }
};