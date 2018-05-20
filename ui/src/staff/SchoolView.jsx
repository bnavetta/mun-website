import React from "react";
import { connect } from "react-redux";
import "regenerator-runtime/runtime"; // for some reason this doesn't get picked up
import { Form } from "react-form";
import { yesNo } from "../lib/util";

import ApplicationForm from "../registration/ApplicationForm";
import SupplementalInfo from "./SupplementalInfo";

import { selectSchool, selectAdvisors } from "./state";
import LoadingPage from "../lib/components/LoadingPage";

const mapStateToProps = (state, props) => {
    const id = parseInt(props.match.params.id);
    return {
      school: selectSchool(id, state),
      advisors: selectAdvisors(id, state),
    };
} ;

function SchoolView({ school, advisors }) {
    if (!school) {
        return <LoadingPage />;
    }

    return (
        <div>
            <h1>{ school.name }</h1>

            <dl className="prop-list">
                <dt>ID</dt>
                <dd>{ school.id }</dd>
                <dt>Registration Code</dt>
                <dd>{ school.registrationCode }</dd>
                <dt>Accepted</dt>
                <dd>{ yesNo(school.accepted) }</dd>
            </dl>

            <h2>Advisors</h2>

            <table className="standard-table">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Phone Number</th>
                    </tr>
                </thead>
                <tbody>
                    { advisors && advisors.map(advisor => (
                        <tr key={advisor.id}>
                            <td>{advisor.name}</td>
                            <td>{advisor.email}</td>
                            <td>{advisor.phoneNumber}</td>
                        </tr>
                    ))}
                </tbody>
            </table>

            <h2>Application Responses</h2>

            <Form defaultValues={school} render={formApi => (
                <form className="form" onSubmit={formApi.handleSubmit}>
                    <ApplicationForm formApi={formApi} readOnly/>
                </form>
            )}/>

            <h2>Supplemental Information</h2>

            <SupplementalInfo id={school.id}/>
        </div>
    );
}

export default connect(mapStateToProps)(SchoolView);