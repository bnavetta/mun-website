// @flow

import React from 'react';
import { HOC } from 'formsy-react';
import classNames from 'classnames';

type Props = {
    getErrorMessages: () => Array<string>,
    getValue: () => string,
    isPristine: () => boolean,
    isRequired: () => boolean,
    help: string | React.Element<*>,
    label: string,
    name: string,
    options: Array<{ label: string, value: string }>,
    setValue: (string) => void,
    showError: () => boolean,
    showRequired: () => boolean,
}

class Select extends React.Component {
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
        // console.log(this.props.getErrorMessages());
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
                <select
                  className={classNames('form-control', { 'form-control-danger': showError })}
                  id={this.props.name}
                  name={this.props.name}
                  onChange={this.handleChange}
                  onFocus={this.handleFocus}
                  onBlur={this.handleBlur}
                  value={this.props.getValue()}
                >
                    { this.props.options.map(
                        ({ label, value }) => <option value={value} key={value}>{label}</option>,
                    ) }
                </select>
                { showError && this.renderErrors() }
                { this.props.help && <p className="form-text text-muted">{this.props.help}</p> }
            </div>
        );
    }
}

Select.propTypes = {
    getErrorMessages: React.PropTypes.func,
    getValue: React.PropTypes.func,
    isPristine: React.PropTypes.func,
    isRequired: React.PropTypes.func,
    help: React.PropTypes.oneOfType([React.PropTypes.string, React.PropTypes.node]),
    label: React.PropTypes.oneOfType([React.PropTypes.string, React.PropTypes.node]),
    name: React.PropTypes.string.isRequired,
    options: React.PropTypes.arrayOf(React.PropTypes.shape({
        label: React.PropTypes.string.isRequired,
        value: React.PropTypes.string.isRequired,
    })),
    setValue: React.PropTypes.func,
    showError: React.PropTypes.func,
    showRequired: React.PropTypes.func,
};

export default HOC(Select);