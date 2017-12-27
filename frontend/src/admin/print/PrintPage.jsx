import React from 'react';
import PropTypes from 'prop-types';

import PrintQueue from './PrintQueue';
import RequestForm from './RequestForm';

import './print.scss';

import { loadQueue, subscribeToQueue, submitRequest, claimRequest, completeRequest } from './client';

export default class PrintPage extends React.Component {
    constructor(props, context) {
        super(props, context);

        this.state = {
            queue: [],
            error: null
        };

        this.handleUpload = this.handleUpload.bind(this);
        this.handleQueueUpdate = this.handleQueueUpdate.bind(this);
    }

    componentDidMount() {
        loadQueue().then(queue => this.setState({ queue })).catch(error => this.setState({ error }));
        this._disconnectQueue = subscribeToQueue(this.handleQueueUpdate);
    }

    componentWillUnmount() {
        if (!!this._disconnectQueue) {
            this._disconnectQueue();
        }
    }

    handleQueueUpdate(queue) {
        this.setState({ queue });
    }

    handleUpload(data) {
        submitRequest(data).then(() => {
            // TODO: clear form
            console.log('Submitted print request');
        }).catch(e => {
            // TODO: show error
            console.error('Error submitting print request', e);
            this.setState({ error: e.message });
        });
    }

    render() {
        const { staffEmail } = this.props;
        return (
            <div>
                <RequestForm staffEmail={staffEmail} onSubmit={this.handleUpload}/>
                {/*{ this.state.error && <div className="alert alert-danger">{ this.state.error }</div> }*/}
                <PrintQueue queue={this.state.queue} onClaim={claimRequest} onComplete={completeRequest} />
            </div>
        )
    }
}

PrintPage.propTypes = {
    staffEmail: PropTypes.string,
};