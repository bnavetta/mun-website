import React from "react";
import { Form } from "react-form";
import { hot } from "react-hot-loader";

import ValidatedText from "./ValidatedText";
import { csrfHeaders } from "../lib/util";
import ApplicationForm from "./ApplicationForm";

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
            },
            credentials: 'same-origin',
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

                <p>
                    Thank you so much for your application to BUSUN XXII. Due to the demand for spots in our conference,
                    we request that you fill out the supplemental questions below as part of your application. Please
                    fill out the questions below by April 23rd at midnight. Respond to all of the prompts in no more
                    than 250 words - the quality of these answers will be valued over length. We look forward to reading
                    your responses, and we thank you for your interest in attending BUSUN XXII. Please email any
                    questions, comments, or concerns to <a href="mailto:info@busun.org">info@busun.org</a> .
                </p>

                <fieldset>
                    <label htmlFor="schoolName">What is your school's name?</label>
                    <ValidatedText placeholder="School Name" field="schoolName" id="schoolName" validate={requiredValidator("School name is required")} />
                </fieldset>

                <fieldset>
                    <label htmlFor="advisorName">What is your name?</label>
                    <ValidatedText placeholder="Advisor Name" field="advisorName" id="advisorName" validate={requiredValidator("Advisor name is required")} />
                </fieldset>

                <fieldset>
                    <label htmlFor="advisorEmail">What is your email address?</label>
                    <ValidatedText placeholder="Advisor Email" field="advisorEmail" id="advisorEmail" validate={requiredValidator("Advisor email is required")} type="email" />
                </fieldset>

                <fieldset>
                    <label htmlFor="advisorPhoneNumber">What is your phone number?</label>
                    <ValidatedText placeholder="Advisor Phone Number" field="advisorPhoneNumber" id="advisorPhoneNumber" validate={requiredValidator("Phone number is required")} type="tel" />
                </fieldset>

                <fieldset>
                    <label htmlFor="advisorPassword">Please choose a password.</label>
                    <ValidatedText field="advisorPassword" id="advisorPassword" validate={requiredValidator("Advisor password is required")} type="password" />
                </fieldset>

                <p>You will use this password with your email address to log into your BUSUN account.</p>
                
                <ApplicationForm formApi={formApi}/>

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
