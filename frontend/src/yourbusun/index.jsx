// @flow
import React from 'react';
import ReactDOM from 'react-dom';
import { AppContainer } from 'react-hot-loader';

import './index.scss';

import AddAdvisorsForm from './AddAdvisorsForm';

document.addEventListener('DOMContentLoaded', () => {
    const addAdvisorsRoot = document.getElementById('add-advisors');
    if (addAdvisorsRoot) {
        const render = () => {
            ReactDOM.render(
                <AppContainer>
                    <AddAdvisorsForm />
                </AppContainer>,
                addAdvisorsRoot,
            );
        };

        render();

        if (module.hot) {
            module.hot.accept('./AddAdvisorsForm', render);
        }
    }
});
