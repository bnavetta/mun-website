import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import format from "date-fns/format";

import { displayError } from "../../lib/util";
import { mergeRequest, selectPrintQueue, selectActivePrintQueue } from "../state/print";
import PrintQueueClient from "./client";

const mapStateToProps = state => ({
    printQueue: selectPrintQueue(state),
    activeQueue: selectActivePrintQueue(state),
});

function ActionButton({ request, onClaim, onComplete }) {
    switch (request.status) {
        case "PENDING":
            return <button className="button" onClick={() => onClaim(request.id)}>Claim</button>;
        case "CLAIMED":
            return <button className="button" onClick={() => onComplete(request.id)}>Complete</button>;
        default:
            return null;
    }
}

class PrintQueue extends React.Component {
    constructor(props, context) {
        super(props, context);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleClaim = this.handleClaim.bind(this);
        this.handleComplete = this.handleComplete.bind(this);
        this.toggleShowCompleted = this.toggleShowCompleted.bind(this);

        this.state = {
            showCompleted: false,
        }
    }

    componentDidMount() {
        this._client = new PrintQueueClient();
        this._client.onUpdate = update =>
            this.props.dispatch(mergeRequest(update));
    }

    componentWillUnmount() {
        this._client.close();
        this._client = null;
    }

    handleSubmit(e) {
        e.preventDefault();

        const form = e.target.elements;
        this._client
            .submitRequest(
                form["numCopies"].value,
                form["deliveryLocation"].value,
                form["source"].files[0]
            )
            .then(() => console.log("Submitted print request"))
            .catch(err => displayError(err));
    }

    handleClaim(id) {
        this._client.claim(id);
    }

    handleComplete(id) {
        this._client.complete(id);
    }

    render() {
        const queue = this.state.showCompleted ? this.props.printQueue : this.props.activeQueue;

        return (
            <div>
                <form onSubmit={this.handleSubmit} className="form inline">
                    <fieldset>
                        <label htmlFor="numCopies">Copies</label>
                        <input
                            type="number"
                            name="numCopies"
                            id="numCopies"
                            min={0}
                        />
                    </fieldset>
                    <fieldset>
                        <label htmlFor="deliveryLocation">Deliver To</label>
                        <input
                            type="text"
                            name="deliveryLocation"
                            id="deliveryLocation"
                        />
                    </fieldset>
                    <fieldset>
                        <label htmlFor="source">File</label>
                        <input type="file" name="source" id="source" />
                    </fieldset>
                    <div>
                        <button className="button" type="submit">Submit</button>
                    </div>
                </form>
                <table className="standard-table">
                    <thead>
                        <tr>
                            <th>Requested By</th>
                            <th>Requested At</th>
                            <th>Filename</th>
                            <th>Copies</th>
                            <th>Deliver To</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        {queue.map(req => (
                            <tr key={req.id}>
                                { this.renderPrintRequest(req) }
                            </tr>
                        ))}
                    </tbody>
                </table>

                <div>
                    <button className="button" onClick={this.toggleShowCompleted}>{this.state.showCompleted ? 'Hide completed' : 'Show completed'}</button>
                </div>
            </div>
        );
    }

    toggleShowCompleted() {
        this.setState(state => ({ showCompleted: !state.showCompleted }));
    }

    renderPrintRequest(req) {
        return (
            <>
                <td>{req.requester}</td>
                <td>{format(req.submissionTime, 'h:mm A [on] dddd')}</td>
                <td><a href={`/staff/print-system/download/${req.id}`}>{req.filename}</a></td>
                <td>{req.numCopies}</td>
                <td>{req.deliveryLocation}</td>
                <td><ActionButton request={req} onClaim={this.handleClaim} onComplete={this.handleComplete}/></td>
            </>
        );
    }
}

PrintQueue.propTypes = {
    printQueue: PropTypes.arrayOf(PropTypes.object).isRequired,
    dispatch: PropTypes.func.isRequired,
};

export default connect(mapStateToProps)(PrintQueue);
