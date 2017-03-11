// @flow
import React from 'react';
import ReactDOM from 'react-dom';
import { AppContainer } from 'react-hot-loader';

import AddAdvisorsForm from './AddAdvisorsForm';

document.addEventListener('DOMContentLoaded', () => {
    const addAdvisorsRoot = document.getElementById('add-advisors');
    if (addAdvisorsRoot) {
        const render = () => {
            ReactDOM.render(
                <AppContainer>
                    <AddAdvisorsForm schoolId={window.schoolId} />
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
