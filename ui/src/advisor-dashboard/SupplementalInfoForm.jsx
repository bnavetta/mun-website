import React from "react";
import PropTypes from "prop-types";
import { Radio, RadioGroup, Checkbox, Select } from "react-form";

import ValidatedText from "../registration/ValidatedText";
import ValidatedTextArea from "../registration/ValidatedTextArea";

export default function SupplementalInfoForm({
    readOnly = false,
    formApi,
    busunHotels,
}) {
    return (
        <React.Fragment>
            <fieldset>
                <label htmlFor="phoneNumber">
                    On-Call Chaperone Phone Number
                </label>
                <ValidatedText
                    disabled={readOnly}
                    field="phoneNumber"
                    id="phoneNumber"
                    type="tel"
                />
            </fieldset>

            {/* <fieldset>
                <p>What is your school's address?</p>

                <ValidatedText
                    disabled={readOnly}
                    field="streetAddress"
                    id="streetAddress"
                    placeholder="Street Address"
                />
                <ValidatedText
                    disabled={readOnly}
                    field="city"
                    id="city"
                    placeholder="City"
                />
                <ValidatedText
                    disabled={readOnly}
                    field="state"
                    placeholder="State"
                />
                <ValidatedText
                    disabled={readOnly}
                    field="postal Code"
                    placeholder="Postal Code"
                />
                <ValidatedText
                    disabled={readOnly}
                    field="country"
                    placeholder="Country"
                />
            </fieldset> */}

            {/* <fieldset>
                <label htmlFor="financialAid">
                    Is your delegation applying for financial aid?
                </label>
                <RadioGroup field="financialAid" id="financialAid">
                    <div className="option-pair">
                        <Radio
                            disabled={readOnly}
                            value={true}
                            id="financialAid-yes"
                        />
                        <label htmlFor="financialAid-yes">Yes</label>
                    </div>
                    <div className="option-pair">
                        <Radio
                            disabled={readOnly}
                            value={false}
                            id="financialAid-no"
                        />
                        <label htmlFor="financialAid-no">No</label>
                    </div>
                </RadioGroup>

                {formApi.values.financialAid && (
                    <React.Fragment>
                        <label htmlFor="financialAidAmount">
                            How much financial aid is your delegation applying
                            for?
                        </label>
                        <ValidatedText
                            disabled={readOnly}
                            field="financialAidAmount"
                            type="number"
                        />

                        <label htmlFor="financialAidDocumentation">
                            {" "}
                            Please document your delegation's need for financial
                            aid. Include the percentage of students in a
                            subsidized lunch program (for U.S. public schools)
                            and any expenses that pose a burden to attending
                            BUSUN.{" "}
                        </label>
                        <ValidatedTextArea
                            disabled={readOnly}
                            field="financialAidDocumentation"
                        />

                        <p>
                            {" "}
                            We will try to provide the requested amount, but
                            cannot guarantee providing it in full.
                        </p>
                    </React.Fragment>
                )}
            </fieldset> */}

            {/* <fieldset>
                <label htmlFor="busunHotel">
                    Is your school using a BUSUN hotel?
                </label>
                <Select
                    disabled={readOnly}
                    field="busunHotel"
                    id="busunHotel"
                    options={busunHotels.map(h => ({
                        label: h.name,
                        value: h.id,
                    }))}
                />

                <label htmlFor="nonBusunHotel">
                    If not, which hotel will your school be using?
                </label>
                <ValidatedText
                    disabled={readOnly}
                    field="nonBusunHotel"
                    id="nonBusunHotel"
                />
            </fieldset> */}

            {/* <fieldset>
                <p>On which days will your school need shuttle service?</p>

                <div className="option-pair">
                    <Checkbox
                        field="shuttleFridayAfternoon"
                        id="shuttleFridayAfternoon"
                    />
                    <label htmlFor="shuttleFridayAfternoon">
                        Friday Afternoon
                    </label>
                </div>

                <div className="option-pair">
                    <Checkbox
                        field="shuttleFridayNight"
                        id="shuttleFridayNight"
                    />
                    <label htmlFor="shuttleFridayNight">Friday Night</label>
                </div>

                <div className="option-pair">
                    <Checkbox field="shuttleSaturday" id="shuttleSaturday" />
                    <label htmlFor="shuttleSaturday">Saturday</label>
                </div>

                <div className="option-pair">
                    <Checkbox field="shuttleSunday" id="shuttleSunday" />
                    <label htmlFor="shuttleSunday">Sunday</label>
                </div>
            </fieldset> */}


            {/* <fieldset>
                <label htmlFor="commuting">Is your school commuting?</label>
                <RadioGroup field="commuting" id="commuting">
                    <div className="option-pair">
                        <Radio
                            disabled={readOnly}
                            value={true}
                            id="commuting-yes"
                        />
                        <label htmlFor="commuting-yes">Yes</label>
                    </div>

                    <div className="option-pair">
                        <Radio
                            disabled={readOnly}
                            value={false}
                            id="commuting-no"
                        />
                        <label htmlFor="commuting-no">No</label>
                    </div>
                </RadioGroup>

                {formApi.values.commuting && (
                    <React.Fragment>
                        <label htmlFor="carsParking">
                            How many cars will you need parking passes for?
                        </label>
                        <ValidatedText
                            disabled={readOnly}
                            field="carsParking"
                            id="carsParking"
                            type="number"
                        />

                        <label htmlFor="carParkingDays">
                            How many days will you need car parking passes for?
                        </label>
                        <ValidatedText
                            disabled={readOnly}
                            field="carParkingDays"
                            id="carParkingDays"
                            type="number"
                        />

                        <label htmlFor="busParking">
                            How many buses will you need parking passes for?
                        </label>
                        <ValidatedText
                            disabled={readOnly}
                            field="busParking"
                            id="busParking"
                            type="number"
                        />

                        <label htmlFor="busParkingDays">
                            How many days will you need bus parking passes for?
                        </label>
                        <ValidatedText
                            disabled={readOnly}
                            field="busParkingDays"
                            id="busParkingDays"
                            type="number"
                        />
                    </React.Fragment>
                )}
            </fieldset> */}

            {/* <fieldset>
                <label htmlFor="arrivalTime">
                    When do you plan to arrive on Friday
                </label>
                <ValidatedText
                    disabled={readOnly}
                    field="arrivalTime"
                    id="arrivalTime"
                />
            </fieldset>

            <fieldset>
                <label htmlFor="luggageStorage">
                    When will you need your luggage stored on-campus?
                </label>
                <Select
                    disabled={readOnly}
                    field="luggageStorage"
                    id="luggageStorage"
                    options={[
                        { label: "Friday", value: "FRIDAY" },
                        { label: "Sunday", value: "SUNDAY" },
                        { label: "Both days", value: "BOTH" },
                        { label: "Neither day", value: "NONE" },
                    ]}
                />
            </fieldset>

            <fieldset>
                <label htmlFor="delegateSocialNeedAdvisor">
                    Which wristband system would you like to use for the
                    delegate social?
                </label>
                <RadioGroup
                    field="delegateSocialNeedAdvisor"
                    id="delegateSocialNeedAdvisor"
                >
                    <div className="option-pair">
                        <Radio
                            disabled={readOnly}
                            value={true}
                            id="delegateSocialNeedAdvisor-red"
                        />
                        <label htmlFor="delegateSocialNeedAdvisor-red">
                            Red - delegates may only leave with an advisor
                        </label>
                    </div>

                    <div className="option-pair">
                        <Radio
                            disabled={readOnly}
                            value={false}
                            id="delegateSocialNeedAdvisor-yellow"
                        />
                        <label htmlFor="delegateSocialNeedAdvisor-yellow">
                            Yellow - delegates may leave when accompanied by a
                            staff member
                        </label>
                    </div>
                </RadioGroup>
            </fieldset> */}

            <fieldset>
                <label htmlFor="delegateCount">
                    Number of Accepted Spots
                </label>
                <ValidatedText
                    disabled={readOnly}
                    field="delegateCount"
                    id="delegateCount"
                    type="number"
                />
            </fieldset>

            {/* <fieldset>
                <label htmlFor="chaperoneCount">
                    How many chaperones will you be bringing?
                </label>
                <ValidatedText
                    disabled={readOnly}
                    field="chaperoneCount"
                    id="chaperoneCount"
                    type="number"
                />
            </fieldset>

            <fieldset>
                <label htmlFor="chaperoneInfo">
                    What are the names and cell phone numbers of your
                    delegation's advisors and chaperones?
                </label>
                <ValidatedTextArea
                    disabled={readOnly}
                    field="chaperoneInfo"
                    id="chaperoneInfo"
                />
            </fieldset>

            <fieldset>
                <label htmlFor="parliProTrainingCount">
                    How many delegates plan on attending the parliamentary
                    procedure training session on Friday?
                </label>
                <ValidatedText
                    disabled={readOnly}
                    field="parliProTrainingCount"
                    id="parliProTrainingCount"
                    type="number"
                />
            </fieldset>

            <fieldset>
                <label htmlFor="crisisTrainingCount">
                    How many delegates plan on attending the crisis committee
                    procedure training session on Friday?
                </label>
                <ValidatedText
                    disabled={readOnly}
                    field="crisisTrainingCount"
                    id="crisisTrainingCount"
                    type="number"
                />
            </fieldset>

            <fieldset>
                <label htmlFor="tourCount">
                    How many delegates plan on attending a campus tour on
                    Friday?
                </label>
                <ValidatedText
                    disabled={readOnly}
                    field="tourCount"
                    id="tourCount"
                    type="number"
                />
            </fieldset>

            <fieldset>
                <label htmlFor="infoSessionCount">
                    How many delegates plan on attending a Brown information
                    session on Friday?
                </label>
                <ValidatedText
                    disabled={readOnly}
                    field="infoSessionCount"
                    id="infoSessionCount"
                    type="number"
                />
            </fieldset> */}
        </React.Fragment>
    );
}

SupplementalInfoForm.propTypes = {
    readOnly: PropTypes.bool,
    formApi: PropTypes.object.isRequired,
    busunHotels: PropTypes.arrayOf(PropTypes.object),
};
