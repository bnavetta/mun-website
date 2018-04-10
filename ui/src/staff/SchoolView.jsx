import React from "react";
import { connect } from "react-redux";
import "regenerator-runtime/runtime"; // for some reason this doesn't get picked up
import { Form } from "react-form";

import ApplicationForm from "../registration/ApplicationForm";

import { selectSchool } from "./state";

const mapStateToProps = (state, props) => ({
    school: selectSchool(parseInt(props.match.params.id), state)
});

function SchoolView({ school }) {
    return (
        <div>
            <h1>{ school.name }</h1>

            <dl className="prop-list">
                <dt>ID</dt>
                <dd>{ school.id }</dd>
                <dt>Registration Code</dt>
                <dd>{ school.registrationCode }</dd>
                <dt>Accepted</dt>
                <dd>{ school.accepted ? 'Yes' : 'No' }</dd>
            </dl>

            <h2>Application Responses</h2>

            <Form defaultValues={school} render={formApi => (
                <form className="form" onSubmit={formApi.handleSubmit}>
                    <ApplicationForm formApi={formApi} readOnly/>
                </form>
            )}/>
        </div>
    );
}

export default connect(mapStateToProps)(SchoolView);