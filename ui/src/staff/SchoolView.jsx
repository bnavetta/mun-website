import React from "react";
import { connect } from "react-redux";
import "regenerator-runtime/runtime"; // for some reason this doesn't get picked up
import { Form } from "react-form";
import Noty from "noty";

import { yesNo } from "../lib/util";
import LoadingPage from "../lib/components/LoadingPage";
import ApplicationForm from "../registration/ApplicationForm";

import SupplementalInfo from "./SupplementalInfo";
import { authenticateAs } from "./api";
import { selectSchool, selectAdvisors } from "./state";

const mapStateToProps = (state, props) => {
    const id = parseInt(props.match.params.id);
    return {
        school: selectSchool(id, state),
        advisors: selectAdvisors(id, state),
    };
};

const ghostLogin = async id => {
    try {
        await authenticateAs(id);
        window.location.assign('/your-mun');
    } catch (e) {
        console.log(`Error authenticating as advisor ${id}: ${e}`);
        new Noty({
            text: `Ghost login failed: ${e}`,
            animation: {
                open: 'animated bounceInRight',
                close: 'animated bounceOutRight'
            }
        }).show();
    }
};

function SchoolView({ school, advisors }) {
    if (!school) {
        return <LoadingPage />;
    }

    return (
        <div>
            <h1>{school.name}</h1>

            <dl className="prop-list">
                <dt>ID</dt>
                <dd>{school.id}</dd>
                <dt>Registration Code</dt>
                <dd>{school.registrationCode}</dd>
                <dt>Accepted</dt>
                <dd>{yesNo(school.accepted)}</dd>
            </dl>

            <h2>Advisors</h2>

            <table className="standard-table">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Phone Number</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {advisors &&
                        advisors.map(advisor => (
                            <tr key={advisor.id}>
                                <td>{advisor.name}</td>
                                <td>{advisor.email}</td>
                                <td>{advisor.phoneNumber}</td>
                                <td>
                                    <button className="button" onClick={() => ghostLogin(advisor.id)}>
                                        Ghost Login
                                    </button>
                                </td>
                            </tr>
                        ))}
                </tbody>
            </table>

            <h2>Application Responses</h2>

            <Form
                defaultValues={school}
                render={formApi => (
                    <form className="form" onSubmit={formApi.handleSubmit}>
                        <ApplicationForm formApi={formApi} readOnly />
                    </form>
                )}
            />

            <h2>Supplemental Information</h2>

            <SupplementalInfo id={school.id} />
        </div>
    );
}

export default connect(mapStateToProps)(SchoolView);
