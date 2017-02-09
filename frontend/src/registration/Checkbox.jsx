// @flow

import React from 'react';
import { HOC } from 'formsy-react';
import classNames from 'classnames';

type Props = {
    getErrorMessages: () => Array<string>,
    getValue: () => boolean,
    isPristine: () => boolean,
    label: string | React.Element<any>,
    name: string,
    setValue: (boolean) => void,
    showError: () => boolean,
    showRequired: () => boolean,
}

class Checkbox extends React.Component {
    constructor(props: Props, context: Object) {
        super(props, context);

        this.state = {
            focused: false,
        };

        this.handleChange = (e) => {
            this.props.setValue(e.target.checked);
        };

        this.handleFocus = () => {
            this.setState({ focused: true });
        };

        this.handleBlur = () => {
            this.setState({ focused: false });
        };
    }

    state: {
        focused: boolean,
    }

    props: Props

    handleChange: (SyntheticInputEvent) => void
    handleFocus: () => void
    handleBlur: () => void

    renderErrors() {
        // console.log(this.props.getErrorMessages());
        return this.props.getErrorMessages().map(message => <div key={message} className="form-control-feedback">{message}</div>);
    }

    render() {
        const showError = (this.props.showRequired() || this.props.showError())
            && !this.state.focused && !this.props.isPristine();

        return (
            <div className={classNames('form-check', { 'has-danger': showError })}>
                <label className="form-check-label" htmlFor={this.props.name}>
                    <input
                      className="form-check-input"
                      type="checkbox"
                      name={this.props.name}
                      checked={this.props.getValue()}
                      onChange={this.handleChange}
                      onFocus={this.handleFocus}
                      onBlur={this.handleBlur}
                    />
                    <span>{this.props.label}</span>
                </label>
                { showError && this.renderErrors() }
            </div>
        );
    }
}

Checkbox.propTypes = {
    getErrorMessages: React.PropTypes.func.isRequired,
    getValue: React.PropTypes.func.isRequired,
    isPristine: React.PropTypes.func.isRequired,
    label: React.PropTypes.oneOfType([React.PropTypes.string, React.PropTypes.node]).isRequired,
    name: React.PropTypes.string.isRequired,
    setValue: React.PropTypes.func.isRequired,
    showError: React.PropTypes.func.isRequired,
    showRequired: React.PropTypes.func.isRequired,
};

export default HOC(Checkbox);
