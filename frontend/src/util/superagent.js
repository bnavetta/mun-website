// @flow

import superagent from 'superagent';
import superagentUse from 'superagent-use';

const agent = superagentUse(superagent);

const csrfHeader = document.querySelector('meta[name=csrf-header]').getAttribute('content');
const csrfToken = document.querySelector('meta[name=csrf-token]').getAttribute('content');

const csrf = (request) => {
    request.set(csrfHeader, csrfToken);
};

agent.use(csrf);

export default agent;
