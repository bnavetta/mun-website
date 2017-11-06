import path from 'path';

const config = {
    production: process.env.NODE_ENV == 'production',
    conference: process.env.CONFERENCE,
    entry: {
        bootstrap: [path.resolve('./src/bootstrap/index.js')],

        registration: ['babel-polyfill', path.resolve('./src/registration/index.jsx')],
        yourbusun: ['babel-polyfill', path.resolve('./src/yourbusun/index.jsx')],

        public: [path.resolve('./src/public/index.js')],

        admin: ['babel-polyfill', path.resolve('./src/admin/index.js')],
        print: ['babel-polyfill', path.resolve('./src/admin/print/index.jsx')],
        attendance: ['babel-polyfill', path.resolve('./src/admin/attendance/index.jsx')],
        committeeAdmin: ['babel-polyfill', path.resolve('./src/admin/committee/index.jsx')],
        schoolAdmin: ['babel-polyfill', path.resolve('./src/admin/school/index.jsx')],
    },

    paths: {
        dist: path.resolve(path.join('dist', process.env.CONFERENCE, process.env.NODE_ENV))
    },
    dllStatsFile: 'dll.json',
    dlls: {
        react: ['react', 'react-dom', 'html-entities', 'strip-ansi']
    }
};

console.log(`Building for ${config.conference} into ${config.paths.dist} in ${process.env.NODE_ENV} env`);

export default config;