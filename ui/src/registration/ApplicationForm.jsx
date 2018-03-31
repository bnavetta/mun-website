import React from "react";
import PropTypes from "prop-types";
import { Radio, RadioGroup } from "react-form";

import ValidatedText from "./ValidatedText";
import ValidatedTextArea from "./ValidatedTextArea";

/**
 * React component to display the school application fields for the registration form. This renders the form fields
 * inside a fragment, so they can be reused
 * @param readOnly if true, disable all inputs so the form is read-only
 * @param formApi the react-form formApi
 */
export default function ApplicationForm({ readOnly = false, formApi }) {
    return (
        <React.Fragment>
            <fieldset>
                <label htmlFor="hasAttended">Have you attended BUSUN before?</label>
                <RadioGroup field="hasAttended" id="hasAttended">
                    <label htmlFor="hasAttended-yes">Yes</label>
                    <Radio disabled={readOnly} value={true} id="hasAttended-yes" />
                    <label htmlFor="hasAttended-no">No</label>
                    <Radio disabled={readOnly} value={false} id="hasAttended-no" />
                </RadioGroup>

                { formApi.values.hasAttended && (<React.Fragment>
                    <label htmlFor="yearsAttended">Which years have you attended?</label>
                    <ValidatedText disabled={readOnly} field="yearsAttended" id="yearsAttended" />
                </React.Fragment>) }
            </fieldset>

            <fieldset>
                <label htmlFor="aboutSchool">Tell us about your school</label>
                <ValidatedTextArea disabled={readOnly} field="aboutSchool" id="aboutSchool"/>
            </fieldset>

            <fieldset>
                <label htmlFor="aboutMunProgram">Briefly describe your Model UN program</label>
                <ValidatedTextArea disabled={readOnly} field="aboutMunProgram" id="aboutMunProgram" />
            </fieldset>

            <fieldset>
                <label htmlFor="delegationGoals">What are your delegation's goals at BUSUN?</label>
                <ValidatedTextArea disabled={readOnly} field="delegationGoals" id="delegationGoals"/>
            </fieldset>

            <fieldset>
                <label htmlFor="whyApplied">What drew you to apply to BUSUN?</label>
                <ValidatedTextArea disabled={readOnly} field="whyApplied" id="whyApplied"/>
            </fieldset>
        </React.Fragment>
    );
}

ApplicationForm.propTypes = {
    readOnly: PropTypes.bool,
    formApi: PropTypes.object.isRequired,
};
