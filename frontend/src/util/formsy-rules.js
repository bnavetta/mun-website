// @flow

import Formsy from 'formsy-react';

Formsy.addValidationRule('range', (values, value, range) => {
    const [low, high] = range.split('-');
    return Number(value) >= Number(low) && Number(value) <= Number(high);
});
