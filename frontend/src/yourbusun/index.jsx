// @flow
import React from 'react';
import ReactDOM from 'react-dom';
import { AppContainer } from 'react-hot-loader';

import './index.scss';

import AddAdvisorsForm from './AddAdvisorsForm';

document.addEventListener('DOMContentLoaded', () => {
    const addAdvisorsRoot = document.getElementById('add-advisors');
    if (addAdvisorsRoot) {
        const idMeta = document.querySelector('meta[name=schoolId]');
        const schoolId = (idMeta && Number.parseInt(idMeta.getAttribute('value') || '', 10)) || -1;

        const render = () => {
            ReactDOM.render(
                <AppContainer>
                    <AddAdvisorsForm schoolId={schoolId} />
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
