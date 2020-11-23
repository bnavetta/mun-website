import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";

import ErrorPage from "../../lib/components/ErrorPage";
import LoadingPage from "../../lib/components/LoadingPage";
import { fetchDelegates } from "../api";
import { loadDelegates, selectDelegates } from "../state/school";

function DelegatesTable({ delegates }) {
    return (
        <table className="standard-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Delegate Name</th>
                    <th>Position</th>
                    <th>Committee</th>
                    <th>Gatherly Link</th>
                </tr>
            </thead>
            <tbody>
                {delegates.map(d => (
                    <tr key={d.id}>
                        <td>{d.positionID}</td>
                        <td>{d.name || "Unset"}</td>
                        <td>{d.positionName || "Unassigned"}</td>
                        <td>{d.committeeName || "Unassigned"}</td>
                        <td>{d.gatherlyLink || "No Link Assigned"}</td>
                    </tr>
                ))}
            </tbody>
        </table>
    );
}

class Delegation extends React.Component {
    constructor(props, context) {
        super(props, context);

        this.state = {
            error: null,
        };
    }

    componentDidMount() {
        this.loadDelegates();
    }

    async loadDelegates() {
        console.log(`Loading delegates for ${this.props.id}`);
        try {
            const delegates = await fetchDelegates(this.props.id);
            this.props.dispatch(loadDelegates(this.props.id, delegates));
        } catch (error) {
            this.setState({ error });
        }
    }

    render() {
        const { error } = this.state;
        const { delegates } = this.props;

        if (error) {
            return <ErrorPage error={error} />;
        } else if (delegates) {
            return <DelegatesTable delegates={delegates} />;
        } else {
            return <LoadingPage />;
        }
    }
}

Delegation.propTypes = {
    id: PropTypes.number.isRequired,
    dispatch: PropTypes.func,
    delegates: PropTypes.arrayOf(PropTypes.object),
};

const mapStateToProps = (state, props) => ({
    delegates: selectDelegates(props.id, state),
});

export default connect(mapStateToProps)(Delegation);
