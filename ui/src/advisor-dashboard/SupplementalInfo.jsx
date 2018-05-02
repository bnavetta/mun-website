import React from "react";
import { hot } from "react-hot-loader";
import { Form } from "react-form";
import Raven from "raven-js";

import {fetchHotels, fetchSupplementalInfo, updateSupplementalInfo} from "./api";
import LoadingPage from "../lib/components/LoadingPage";
import ErrorPage from "../lib/components/ErrorPage";
import SupplementalInfoForm from "./SupplementalInfoForm";

/**
 * Page for advisors to fill out the supplemental info form
 */
class SupplementalInfo extends React.PureComponent {
    constructor(props, context) {
        super(props, context);

        this.state = {
            loading: true,
            error: null,
            supplementalInfo: null,
            successMessage: null,
            errorMessage: null,
            hotels: [],
        };

        this.renderForm = this.renderForm.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    async componentDidMount() {
        try {
            const [info, hotels] = await Promise.all([fetchSupplementalInfo(), fetchHotels()]);
            this.setState({ supplementalInfo: info, loading: false, error: null, hotels });
        } catch (error) {
            Raven.captureException(error);
            this.setState({ supplementalInfo: null, loading: false, error })
        }
    }

    renderForm(formApi) {
        return (
            <form onSubmit={formApi.submitForm} className="supplemental-info-form form">
                <h2>Supplemental Delegation Information</h2>

                { this.state.successMessage && <p className="alert alert-success">{ this.state.successMessage }</p> }
                { this.state.errorMessage && <p className="alert alert-error">{ this.state.errorMessage }</p> }

                <SupplementalInfoForm formApi={formApi} busunHotels={this.state.hotels}/>

                <button className="button" type="submit">Save</button>
            </form>
        );
    }

    async handleSubmit(values, event, formApi) {
        try {
            const result = await updateSupplementalInfo(values);

            if (result.success) {
                this.setState({ successMessage: 'Supplemental information updated', errorMessage: null });
            } else {
                for (let field of Object.keys(result.errors)) {
                    formApi.setError(field, result.errors[field].join(', '));
                }

                if (result.errors._global) {
                    this.setState({ successMessage: null, errorMessage: result.errors._global.join(', ') });
                } else {
                    this.setState({ successMessage: null, errorMessage: 'Some fields were invalid' });
                }
            }

        } catch (error) {
            console.error('Error submitting supplemental info', error);
            Raven.captureException(error);
            this.setState({ successMessage: null, errorMessage: error.message });
        }
    }

    render() {
        const { loading, error, supplementalInfo } = this.state;
        
        if (loading) {
            return <LoadingPage/>;
        } else if (error) {
            return <ErrorPage error={error}/>
        } else {
            return <Form defaultValues={supplementalInfo} render={this.renderForm} onSubmit={this.handleSubmit}/>;
        }
    }
}

export default hot(module)(SupplementalInfo);