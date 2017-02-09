// @flow

import React from 'react';
import { HOC } from 'formsy-react';
import classNames from 'classnames';

type Props = {
    getErrorMessages: () => Array<string>,
    getValue: () => string,
    isPristine: () => boolean,
    isRequired: () => boolean,
    label: string,
    name: string,
    setValue: (string) => void,
    showError: () => boolean,
    showRequired: () => boolean,
}

class Textarea extends React.Component {
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
        return this.props.getErrorMessages().map(message => <div key={message} className="form-control-feedback">{message}</div>);
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
                <textarea
                  className={classNames('form-control', { 'form-control-danger': showError })}
                  id={this.props.name}
                  name={this.props.name}
                  value={this.props.getValue()}
                  onChange={this.handleChange}
                  onFocus={this.handleFocus}
                  onBlur={this.handleBlur}
                />
                { showError && this.renderErrors() }
            </div>
        );
    }
}

Textarea.propTypes = {
    getErrorMessages: React.PropTypes.func.isRequired,
    getValue: React.PropTypes.func.isRequired,
    isPristine: React.PropTypes.func.isRequired,
    isRequired: React.PropTypes.func.isRequired,
    label: React.PropTypes.oneOfType([React.PropTypes.string, React.PropTypes.node]).isRequired,
    name: React.PropTypes.string.isRequired,
    setValue: React.PropTypes.func.isRequired,
    showError: React.PropTypes.func.isRequired,
    showRequired: React.PropTypes.func.isRequired,
};

export default HOC(Textarea);
