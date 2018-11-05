import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { Link } from "react-router-dom";

import { selectUserCommittees } from "../state/committee";

const mapStateToProps = state => ({
    committees: selectUserCommittees(state),
});

function CommitteeList({ match, committees }) {
    return (
        <table className="standard-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                </tr>
            </thead>
            <tbody>
                {committees.map(committee => (
                    <tr key={committee.id.toString()}>
                        <td>{committee.id}</td>
                        <td>
                            <Link to={`${match.path}/${committee.id}`}>
                                {committee.name}
                            </Link>
                        </td>
                        <td>
                            <a href={"mailto:" + committee.email}>
                                {committee.email}
                            </a>
                        </td>
                    </tr>
                ))}
            </tbody>
        </table>
    );
}

CommitteeList.propTypes = {
    match: PropTypes.object.isRequired,
    committees: PropTypes.arrayOf(PropTypes.object).isRequired,
};

export default connect(mapStateToProps)(CommitteeList);