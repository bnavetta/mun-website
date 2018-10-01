// @flow

import React from 'react';
import request from 'superagent';

import SchoolTable from './SchoolTable';

async function loadSchools() {
    const res = await request.get('/admin/school/list.json').accept('json');
    if (!res.ok) {
        throw new Error(res.body.message);
    }

    return res.body;
}

export default class SchoolDisplay extends React.Component {
    constructor() {
        super();

        this.state = {
            fetching: true,
            schools: [],
            error: null,
        };
    }

    componentDidMount() {
        loadSchools()
            .then((schools) => {
                this.setState({ schools, fetching: false });
            })
            .catch(err => this.setState({ error: err.message, fetching: false }));
    }

    render() {
        const { schools, error, fetching } = this.state;
        if (fetching) {
            return <div>Loading...</div>;
        } else if (error) {
            return <div className="alert alert-danger" role="alert">{error}</div>;
        }

        return <SchoolTable schools={schools} />;
    }
}
