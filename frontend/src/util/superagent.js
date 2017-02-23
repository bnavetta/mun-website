// @flow

import superagent from 'superagent';
import superagentUse from 'superagent-use';

const agent = superagentUse(superagent);

const csrf = (request) => {
    if (window.csrf) {
        request.set(window.csrf.header, window.csrf.token);
    }
};

agent.use(csrf);

export default agent;
