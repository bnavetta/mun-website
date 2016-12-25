// @flow

import React from 'react';
import { HOC } from 'formsy-react';
import classNames from 'classnames';

type Props = {
    getErrorMessages: () => [string],
    getValue: () => string,
    help: string | React.Element<*>,
    isPristine: () => boolean,
    isRequired: () => boolean,
    label: string,
    name: string,
    setValue: (string) => void,
    showError: () => boolean,
    showRequired: () => boolean,
    type: string,
};

class Input extends React.Component {
    constructor(props: Props, context: Object) {
        super(props, context);

        this.state = {
            focused: false,
        };

        this.handleChange = (e) => {
            this.props.setValue(e.target.value);
        };

        this.handleFocus = () => {
            this.setState({ focused: true });
        };

        this.handleBlur = () => {
            this.setState({ focused: true });
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
        return this.props.getErrorMessages().map((message, i) => <div key={i} className="form-control-feedback">{message}</div>);
    }

    render() {
        const showError = (this.props.showRequired() || this.props.showError())
            && !this.state.focused && !this.props.isPristine();

        return (
            <div className={classNames('form-group', { 'has-danger': showError })}>
                {this.props.label &&
                <label
                  className="form-control-label"
                  htmlFor={this.props.name}
                >
                    {this.props.label + (this.props.isRequired ? ' *' : '')}
                </label>
                }
                <input
                  className={classNames('form-control', { 'form-control-danger': showError })}
                  id={this.props.name}
                  name={this.props.name}
                  type={this.props.type}
                  value={this.props.getValue()}
                  onChange={this.handleChange}
                  onFocus={this.handleFocus}
                  onBlur={this.handleBlur}
                />
                { showError && this.renderErrors() }
                { this.props.help && <p className="form-text text-muted">{this.props.help}</p> }
            </div>
        );
    }
}

Input.propTypes = {
    getErrorMessages: React.PropTypes.func,
    getValue: React.PropTypes.func,
    help: React.PropTypes.oneOfType([React.PropTypes.string, React.PropTypes.node]),
    isPristine: React.PropTypes.func,
    isRequired: React.PropTypes.func,
    label: React.PropTypes.oneOfType([React.PropTypes.string, React.PropTypes.node]),
    name: React.PropTypes.string.isRequired,
    setValue: React.PropTypes.func,
    showError: React.PropTypes.func,
    showRequired: React.PropTypes.func,
    type: React.PropTypes.string,
};

export default HOC(Input);
