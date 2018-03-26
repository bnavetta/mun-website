const chalk = require('chalk');

module.exports = {
    plugins: [
        require('postcss-cssnext')({
            features: {
                // Rely on browser support
                customProperties: false
                // customProperties: {
                //     preserve: true,
                // }
            }
        }),
        require('postcss-font-family-system-ui')
    ]
};