// @flow
import WebFont from 'webfontloader';

import 'bootstrap/dist/js/bootstrap';

import './index.scss';

WebFont.load({
    typekit: { id: 'afh4xoi' },
});

document.querySelectorAll('[data-toggle="tooltip"]').forEach((tooltip) => $(tooltip).tooltip());