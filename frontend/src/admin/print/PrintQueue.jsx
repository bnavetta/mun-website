import React from 'react';
import PropTypes from 'prop-types';
import classNames from 'classnames';
import moment from 'moment';

function StatusButton({ id, status, onClaim, onComplete }) {
    let buttonClass = false;
    let buttonText = status;
    let handler;
    if (status === 'PENDING') {
        buttonClass = 'btn-secondary';
        buttonText = 'Claim';
        handler = () => onClaim(id);
    } else if (status === 'CLAIMED') {
        buttonClass = 'btn-success';
        buttonText = 'Mark as Completed';
        handler = () => onComplete(id);
    }
    return (
        <button
            className={classNames('request-status', 'request-' + status.toLowerCase(), 'btn', buttonClass)}
            onClick={handler}
        >
            {buttonText}
        </button>
    )
}

export default function PrintQueue({ queue, onClaim, onComplete }) {
    return (
        <table className="table">
            <thead>
                <tr>
                    <th>File</th>
                    <th>Requester</th>
                    <th>Number of Copies</th>
                    <th>Delivery Location</th>
                    <th>Submission Time</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
            {
                queue.map(request => (
                    <tr key={request.id} className={`print-request request-${request.status.toLowerCase()}`}>
                        <td><a href={`/admin/print/dl/${request.id}`}>{request.filename}</a></td>
                        <td>{request.requester}</td>
                        <td className="text-right">{request.numCopies}</td>
                        <td>{request.deliveryLocation}</td>
                        <td>{moment(request.submissionTime * 1000).format('ddd M/D h:mm A')}</td>
                        <td><StatusButton status={request.status} id={request.id} onClaim={onClaim} onComplete={onComplete} /></td>
                    </tr>
                ))
            }
            </tbody>
        </table>
    )
}

PrintQueue.propTypes = {
    queue: PropTypes.arrayOf(PropTypes.object),
    onClaim: PropTypes.func,
    onComplete: PropTypes.func,
};