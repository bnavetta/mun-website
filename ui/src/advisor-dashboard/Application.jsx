import React from "react";
import { Form } from "react-form";
import ApplicationForm from "../registration/ApplicationForm";

import { updateApplication } from "./api";

export default class Application extends React.PureComponent {
    constructor(props, context) {
        super(props, context);

        this.state = {
            editing: false,
            error: null,
        };

        this.startEditing = () => this.setState({ editing: true });
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(values, event, formApi) {
        console.log("Saving changes to application...");
        const { school } = this.props;
        this.setState({ error: null, editing: false });
        updateApplication({ ...values, id: school.id, name: school.name })
            .then(() => {
                console.log("Updated application!");
                this.props.setSchool({ ...school, ...values });
            })
            .catch(error =>
                this.setState({ error: error.message, editing: true })
            );
    }

    render() {
        const { school } = this.props;
        const { editing, error } = this.state;

        return (
            <Form
                onSubmit={this.handleSubmit}
                defaultValues={school}
                pure={false}
                render={formApi => (
                    <form className="form" onSubmit={formApi.submitForm}>
                        {error && (
                            <div className="alert alert-error">{error}</div>
                        )}

                        {!editing && (
                            <button
                                className="button"
                                onClick={this.startEditing}
                            >
                                Edit Application
                            </button>
                        )}

                        <ApplicationForm
                            formApi={formApi}
                            readOnly={!editing}
                        />

                        {editing && (
                            <button className="button" type="submit">
                                Save Changes
                            </button>
                        )}
                    </form>
                )}
            />
        );
    }
}
