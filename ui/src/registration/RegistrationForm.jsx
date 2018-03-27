import React from "react";
import { Form, Radio, RadioGroup } from "react-form";
import { hot } from "react-hot-loader";

import ValidatedText from "./ValidatedText";
import ValidatedTextArea from "./ValidatedTextArea";
import { csrfHeaders } from "../lib/util";

function requiredValidator(message) {
    return value => value ? null : message;
}

class RegistrationForm extends React.PureComponent {
    constructor(props, context) {
        super(props, context);

        this.handleSubmit = this.handleSubmit.bind(this);
        this.renderForm = this.renderForm.bind(this);
    }

    handleSubmit(values, event, formApi) {
        console.log('Submitted form', values);

        fetch('/registration/register', {
            method: 'POST',
            body: JSON.stringify(values),
            headers: {
                'Content-Type': 'application/json',
                ...csrfHeaders,
            }
        }).then(async res => {
            if (res.ok) {
                const result = await res.json();
                console.log('Registered with result', result);
                window.location.href = result.dashboard;
            } else {
                const errors = await res.json();
                for (let field of Object.keys(errors)) {
                    formApi.setError(field, errors[field][0]);
                }
            }
        }).catch(e => {
            console.error(`Unable to submit application: ${e}`);
        });
    }

    renderForm(formApi) {
        return (
            <form onSubmit={formApi.submitForm} className="registration-form form">

                <h1>Apply to BUSUN XXII</h1>

                <fieldset>
                    <label htmlFor="schoolName">School Name</label>
                    <ValidatedText field="schoolName" id="schoolName" validate={requiredValidator("School name is required")} />
                </fieldset>

                <fieldset>
                    <label htmlFor="advisorName">Advisor Name</label>
                    <ValidatedText field="advisorName" id="advisorName" validate={requiredValidator("Advisor name is required")} />
                </fieldset>

                <fieldset>
                    <label htmlFor="advisorEmail">Advisor Email Address</label>
                    <ValidatedText field="advisorEmail" id="advisorEmail" validate={requiredValidator("Advisor email is required")} type="email" />
                </fieldset>

                <fieldset>
                    <label htmlFor="advisorPhoneNumber">Advisor Phone Number</label>
                    <ValidatedText field="advisorPhoneNumber" id="advisorPhoneNumber" validate={requiredValidator("Phone number is required")} type="tel" />
                </fieldset>

                <fieldset>
                    <label htmlFor="advisorPassword">Advisor Password</label>
                    <ValidatedText field="advisorPassword" id="advisorPassword" validate={requiredValidator("Advisor password is required")} type="password" />
                </fieldset>

                <fieldset>
                    <label htmlFor="hasAttended">Have you attended BUSUN before?</label>
                    <RadioGroup field="hasAttended" id="hasAttended">
                        <label htmlFor="hasAttended-yes">Yes</label>
                        <Radio value={true} id="hasAttended-yes" />
                        <label htmlFor="hasAttended-no">No</label>
                        <Radio value={false} id="hasAttended-no" />
                    </RadioGroup>

                    { formApi.values.hasAttended && (<React.Fragment>
                        <label htmlFor="yearsAttended">Which years have you attended?</label>
                        <ValidatedText field="yearsAttended" id="yearsAttended" />
                    </React.Fragment>) }
                </fieldset>

                <fieldset>
                    <label htmlFor="aboutSchool">Tell us about your school</label>
                    <ValidatedTextArea field="aboutSchool" id="aboutSchool"/>
                </fieldset>

                <fieldset>
                    <label htmlFor="aboutMunProgram">Briefly describe your Model UN program</label>
                    <ValidatedTextArea field="aboutMunProgram" id="aboutMunProgram" />
                </fieldset>

                <fieldset>
                    <label htmlFor="delegationGoals">What are your delegation's goals at BUSUN?</label>
                    <ValidatedTextArea field="delegationGoals" id="delegationGoals"/>
                </fieldset>

                <fieldset>
                    <label htmlFor="whyApply">What drew you to apply to BUSUN?</label>
                    <ValidatedTextArea field="whyApply" id="whyApply"/>
                </fieldset>

                <button className="button" type="submit">Apply!</button>
            </form>
        );
    }

    render() {
        return (
            <Form render={this.renderForm} onSubmit={this.handleSubmit}/>
        )
    }
}

export default hot(module)(RegistrationForm);
