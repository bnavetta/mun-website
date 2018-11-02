import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import format from "date-fns/format";

import { displayError } from "../../lib/util";
import { mergeRequest, selectPrintQueue } from "../state/print";
import PrintQueueClient from "./client";

const mapStateToProps = state => ({
    printQueue: selectPrintQueue(state),
});

class PrintQueue extends React.Component {
    constructor(props, context) {
        super(props, context);
        this.handleSubmit = this.handleSubmit.bind(this);
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
                        {this.props.printQueue.map(req => (
                            <tr key={req.id}>
                                { this.renderPrintRequest(req) }
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        );
    }

    renderPrintRequest(req) {
        const operation = req.status === 'PENDING' ?
            <button className="button" onClick={() => this.handleClaim(req.id)}>Claim</button> :
            <button className="button" onClick={() => this.handleComplete(req.id)}>Complete</button>;

        return (
            <>
                <td>{req.requester}</td>
                <td>{format(req.submissionTime, 'h:mm A [on] dddd')}</td>
                <td><a href={`/staff/print-system/download/${req.id}`}>{req.filename}</a></td>
                <td>{req.numCopies}</td>
                <td>{req.deliveryLocation}</td>
                <td>{operation}</td>
            </>
        );
    }
}

PrintQueue.propTypes = {
    printQueue: PropTypes.arrayOf(PropTypes.object).isRequired,
    dispatch: PropTypes.func.isRequired,
};

export default connect(mapStateToProps)(PrintQueue);
