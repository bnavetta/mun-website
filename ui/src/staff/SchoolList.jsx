import React from "react";
import { connect } from "react-redux";
import { Link } from "react-router-dom";

import { selectSchools } from "./state";
import { yesNo } from "../lib/util";

const mapStateToProps = state => ({ schools: selectSchools(state) });

function SchoolList({ match, schools }) {
    return (
        <table className="standard-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Registration Code</th>
                    <th>Accepted</th>
                </tr>
            </thead>
            <tbody>
                { schools.map(school => (
                    <tr key={school.id}>
                        <td>{ school.id }</td>
                        <td>
                            <Link to={`${match.path}/${school.id}`}>{ school.name}</Link>
                        </td>
                        <td>{ school.registrationCode }</td>
                        <td>{ yesNo(school.accepted) }</td>
                    </tr>
                )) }
            </tbody>
        </table>
    );
}

// This, along with our mapStateToProps, connects React and Redux. Our SchoolList component will receive the props
// produced by mapStateToProps, which uses the selectSchools function to get a sorted list of schools
export default connect(mapStateToProps)(SchoolList);