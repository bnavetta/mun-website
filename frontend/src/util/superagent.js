// @flow

import superagent from 'superagent';
import superagentUse from 'superagent-use';

const agent = superagentUse(superagent);

const csrf = (request) => {
    const csrfHeaderMeta = document.querySelector('meta[name=csrf-header]');
    const csrfTokenMeta = document.querySelector('meta[name=csrf-token]');

    const csrfHeader = csrfHeaderMeta ? csrfHeaderMeta.getAttribute('content') : null;
    const csrfToken = csrfTokenMeta ? csrfTokenMeta.getAttribute('content') : null;

    if (csrfHeader && csrfToken) {
        request.set(csrfHeader, csrfToken);
    }
};

agent.use(csrf);

export default agent;
