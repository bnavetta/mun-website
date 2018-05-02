import Raven from 'raven-js';

import "./styles.css";

console.log("Hello, Model UN!");

Raven.config('https://fbad7b818c08467ea13f9d7f9b94a103@sentry.io/1198759').install();