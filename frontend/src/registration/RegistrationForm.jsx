// @flow

import React from 'react';
import Formsy from 'formsy-react';
import request from '../util/superagent';

import Input from './Input';
import Textarea from './Textarea';
import Checkbox from './Checkbox';
import Select from './Select';
import PreferenceSlider from './PreferenceSlider';
import '../util/formsy-rules';

async function loadHotels() {
    const res = await request.get('/api/hotel').accept('json');
    if (!res.ok) {
        throw new Error(res.body.message);
    }

    return res.body.map(({ id, name }) => ({ label: name, value: id.toString() }));
}

export default class RegistrationForm extends React.Component {
    constructor() {
        super();

        this.state = {
            submitEnabled: false,
            values: {},
            hotels: [],
            countries: [
                { label: 'United States', value: 'us' },
                { label: 'Israel', value: 'is' },
                { label: 'France', value: 'fr' },
                { label: 'Canada', value: 'ca' },
            ],
        };

        this.handleSubmit = (model, resetForm, invalidateForm) => {
            this.setState({ values: model });
            request.post('').send(model).accept('json')
                .then(res => res.body)
                .then((result) => {
                    if (result.success) {
                        resetForm();
                        window.location.href = '/yourbusun/';
                    } else {
                        invalidateForm(result.errors);
                    }
                })
                .catch((e) => {
                    if (e.response.body.errors) {
                        invalidateForm(e.response.body.errors);
                    } else {
                        console.error(e); // eslint-disable-line no-console
                    }
                });
        };

        this.handleChange = (values) => {
            this.setState({ values });
        };

        this.enableSubmit = () => {
            this.setState({ submitEnabled: true });
        };

        this.disableSubmit = () => {
            this.setState({ submitEnabled: false });
        };
    }

    state: {
        submitEnabled: boolean,
        values: Object,
        hotels: Array<Object>,
        countries: Array<{ label: string, value: string }>,
    }

    componentDidMount() {
        loadHotels()
            .then((hotels) => { this.setState({ hotels }); })
            .catch((e) => { console.error(e); }); // eslint-disable-line no-console
    }

    handleSubmit: (model: Object,
                   resetForm: () => void,
                   invalidateForm: (Array<any>) => void) => void
    handleChange: (Object) => void
    enableSubmit: () => void
    disableSubmit: () => void

    renderFinancialAidInfo() {
        if (this.state.values.applyingForFinancialAid) {
            return (
                <fieldset>
                    <Input
                      name="financialAidPercent"
                      type="number"
                      label="Percentage of students on financial aid"
                      help="Please give the school-wide percentage of students in the free and reduced lunch program at your school (for public schools) or the percentage of students on the MUN team receiving financial aid (for private schools)"
                      value=""
                    />

                    <Textarea
                      name="financialAidJustification"
                      label="Please include a short justification of your delegation's need for finanical aid"
                      value=""
                    />
                </fieldset>
            );
        }

        return undefined;
    }

    renderHotelInfo() {
        if (this.state.values.busunHotel) {
            return (
                <Select
                  name="hotelSelection"
                  label="Tentative Hotel Selection"
                  options={this.state.hotels}
                />
            );
        }

        return undefined;
    }

    render() {
        return (
            <Formsy.Form
              onValidSubmit={this.handleSubmit}
              onValid={this.enableSubmit}
              onInvalid={this.disableSubmit}
              onChange={this.handleChange}
            >
                <fieldset>
                    <h3>Advisor Information</h3>
                    <Input
                      name="advisorName"
                      type="text"
                      label="Advisor Name"
                      placeholder="Your Name"
                      value=""
                      required
                      validations="isExisty"
                      validationErrors={{
                          isDefaultRequiredValue: 'Must provide a name',
                      }}
                    />
                    <Input
                      name="advisorEmail"
                      type="email"
                      label="Email Address"
                      value=""
                      required
                      validations="isEmail"
                      validationErrors={{
                          isEmail: 'Must be a valid email address',
                          isDefaultRequiredValue: 'Must provide an email address',
                      }}
                    />
                    <Input
                      name="advisorPassword"
                      type="password"
                      label="Password"
                      value=""
                      required
                      validationErrors={{
                          isDefaultRequiredValue: 'Must provide a password',
                      }}
                    />
                    <Input
                      name="advisorPasswordConfirm"
                      type="password"
                      label="Confirm Password"
                      value=""
                      required
                      validations="equalsField:advisorPassword"
                      validationErrors={{
                          isDefaultRequiredValue: 'Must provide a password',
                          equalsField: 'Passwords must match',
                      }}
                    />
                    <Input
                      name="advisorPhoneNumber"
                      type="tel"
                      label="Advisor Phone Number"
                      value=""
                      required
                      validationErrors={{
                          isDefaultRequiredValue: 'Must provide a phone number',
                      }}
                    />
                </fieldset>

                <fieldset>
                    <h3>School Information</h3>

                    <Input
                      name="schoolName"
                      type="text"
                      label="School Name"
                      value=""
                      required
                      validationErrors={{
                          isDefaultRequiredValue: 'Must provide a school name',
                      }}
                    />

                    <Input
                      name="schoolAddress.country"
                      type="text"
                      label="Country"
                      value=""
                      required
                      validationErrors={{
                          isDefaultRequiredValue: 'Must provide a country',
                      }}
                    />

                    <Input
                      name="schoolAddress.streetAddress"
                      type="text"
                      label="Street Address"
                      value=""
                      required
                      validationErrors={{
                          isDefaultRequiredValue: 'Must provide a street address',
                      }}
                    />

                    <Input
                      name="schoolAddress.city"
                      type="text"
                      label="City"
                      value=""
                      required
                      validationErrors={{
                          isDefaultRequiredValue: 'Must provide a city',
                      }}
                    />

                    <Input
                      name="schoolAddress.state"
                      type="text"
                      label="State"
                      value=""
                      required={false}
                    />

                    <Input
                      name="schoolAddress.postalCode"
                      type="text"
                      label="Postal Code"
                      value=""
                      required={false}
                    />

                    <Input
                      name="schoolPhoneNumber"
                      type="tel"
                      label="School Phone Number"
                      value=""
                      required
                      validationErrors={{
                          isDefaultRequiredValue: 'Must provide a school phone number',
                      }}
                    />
                </fieldset>

                <fieldset>
                    <h3>Delegation Information</h3>

                    <Input
                      name="estimatedDelegates"
                      type="number"
                      label="Estimated number of delegates"
                      value="10"
                      required
                      validations="range:1-35"
                      validationErrors={{
                          isDefaultRequiredValue: 'Must provide a delegate count',
                          range: 'Delegate count must be between 1 and 35',
                      }}
                    />

                    <Input
                      name="estimatedChaperones"
                      type="number"
                      label="Estimated number of chaperones"
                      value="2"
                      required
                      validations="range:1-35"
                      validationErrors={{
                          isDefaultRequiredValue: 'Must provide a chaperone count',
                          range: 'Chaperone count must be between 1 and 35',
                      }}
                    />

                    <Input
                      name="numberOfYearsAttended"
                      type="number"
                      label="How many times has your school attended BUSUN in the past?"
                      value=""
                      validations="range:0-21"
                      validationErrors={{
                          range: 'BUSUN has not been around that long',
                      }}
                    />

                    <Input
                      name="yearsAttended"
                      type="text"
                      label="Which years did your school attend BUSUN?"
                      value=""
                    />

                    <Textarea
                      name="aboutSchool"
                      label="Please tell us about your MUN program"
                      value=""
                    />

                    <Checkbox
                      name="applyingForFinancialAid"
                      label="I would like my delegation to be considered for financial aid"
                      value={false}
                    />
                    {this.renderFinancialAidInfo()}
                </fieldset>

                <fieldset>
                    <h3>Logistical Information</h3>

                    <Checkbox
                      name="busunHotel"
                      label="We will be staying at a BUSUN-sponsored hotel"
                      value={false}
                    />
                    {/* TODO: add link to sponsored hotel list */}
                    {this.renderHotelInfo()}

                    <Checkbox
                      name="busunShuttles"
                      label="We plan to use BUSUN shuttles"
                      value={false}
                    />
                    {/* TODO: link to shuttle page */}
                </fieldset>

                <fieldset>
                    <h3>Conference Preferences</h3>

                    <PreferenceSlider
                      name="committeePreferences"
                      label="Committee Type Preferences"
                      value={{ general: 33, specialized: 33, crisis: 34 }}
                    />

                    <fieldset>
                        <label htmlFor="countryChoice1">Country Preferences</label>
                        <Select
                          name="countryChoice1"
                          value=""
                          options={this.state.countries}
                        />
                        <Select
                          name="countryChoice2"
                          value=""
                          options={this.state.countries}
                        />
                        <Select
                          name="countryChoice3"
                          value=""
                          options={this.state.countries}
                        />
                        <Select
                          name="countryChoice4"
                          value=""
                          options={this.state.countries}
                        />
                        <Select
                          name="countryChoice5"
                          value=""
                          options={this.state.countries}
                        />
                        <p className="form-text text-muted">
                            Please select the top five countries you would like your delegates to
                            represent in General Assembly committees. List them in descending
                            order of preference.
                        </p>
                    </fieldset>
                </fieldset>

                <button className="btn btn-primary" type="submit" disabled={!this.state.submitEnabled}>Register</button>
            </Formsy.Form>
        );
    }
}
