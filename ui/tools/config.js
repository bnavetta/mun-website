let config = {
    port: process.env.WEBPACK_PORT ? parseInt(process.env.WEBPACK_PORT) : 9090,
    env: process.env.NODE_ENV,
};

config.isDev = config.env === "development";

export default config;
