import React from "react";
import { hot } from "react-hot-loader";

import { changePassword } from "./api";

class ChangePassword extends React.PureComponent {
    constructor(props, context) {
        super(props, context);

        this.state = {
            password: '',
            confirm: '',
            errorMessage: null,
            successMessage: null,
        };

        this.handleSubmit = this.handleSubmit.bind(this);

        this.handlePasswordChange = e => this.setState({ password: e.target.value });
        this.handleConfirmChange = e => this.setState({ confirm: e.target.value });
    }

    handleSubmit(e) {
        e.preventDefault();

        changePassword(this.state.password, this.state.confirm)
            .then(message => this.setState({ successMessage: message, errorMessage: null }))
            .catch(error => this.setState({ successMessage: null, errorMessage: error.message }))
    }

    render() {
        const { password, confirm, errorMessage, successMessage } = this.state;

        return (
            <div>
                { successMessage && <p className="alert alert-success">{ successMessage }</p> }
                { errorMessage && <p className="alert alert-error">{ errorMessage }</p> }
                <form onSubmit={this.handleSubmit} className="form">
                    <fieldset>
                        <label htmlFor="password">Password</label>
                        <input type="password" value={password} onChange={this.handlePasswordChange} id="password" name="password" />
                    </fieldset>

                    <fieldset>
                        <label htmlFor="confirm">Confirm Password</label>
                        <input type="password" value={confirm} onChange={this.handleConfirmChange} id="confirm" name="confirm" />
                    </fieldset>

                    <button className="button" type="submit">Change Password</button>
                </form>
            </div>
        )
    }
}

export default hot(module)(ChangePassword);