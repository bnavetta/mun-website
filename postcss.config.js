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
        require('postcss-font-family-system-ui'),
        require('doiuse')({
            browsers: [
                "last 2 versions",
                "> 1%",
                "Firefox ESR",
                "not Baidu >= 0",
                "not ie >= 0",
                "not ie_mob >= 0",
                "not OperaMobile >= 0",
                "not OperaMini >= 0",
                "not UCAndroid >= 0",
                "not Edge < 16",
                "not BlackBerry >= 0"
            ],
            onFeatureUsage(usageInfo) {
                console.log(chalk.red('⚠️  ' + usageInfo.message));
            }
        })
    ]
};