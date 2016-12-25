// @flow

import React from 'react';
import ReactDOM from 'react-dom';
import { AppContainer } from 'react-hot-loader';

import RegistrationForm from './RegistrationForm';

document.addEventListener('DOMContentLoaded', () => {
    const root = document.getElementById('registration-form');

    const renderForm = () => {
        ReactDOM.render(
            <AppContainer>
                <RegistrationForm />
            </AppContainer>,
            root,
        );
    };

    renderForm();

    if (module.hot) {
        module.hot.accept('./RegistrationForm', renderForm);
    }
});
