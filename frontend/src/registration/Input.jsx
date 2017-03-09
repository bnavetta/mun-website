// @flow

import React from 'react';
import { HOC } from 'formsy-react';
import classNames from 'classnames';

type Props = {
    getErrorMessages: () => [string],
    getValue: () => string,
    help: ?string | ?React.Element<*>,
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
    static defaultProps: {
        help: ?string | ?React.Element<*>,
    }

    constructor(props: Props, context: Object) {
        super(props, context);

        this.handleChange = (e) => {
            this.props.setValue(e.target.value);
        };
    }

    props: Props

    handleChange: (SyntheticInputEvent) => void

    renderErrors() {
        return this.props.getErrorMessages().map(message => <div key={message} className="form-control-feedback">{message}</div>);
    }

    render() {
        const showError = (this.props.showRequired() || this.props.showError())
            && !this.props.isPristine();

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
                />
                { showError && this.renderErrors() }
                { this.props.help && <p className="form-text text-muted">{this.props.help}</p> }
            </div>
        );
    }
}

Input.defaultProps = {
    help: null,
};

Input.propTypes = {
    getErrorMessages: React.PropTypes.func.isRequired,
    getValue: React.PropTypes.func.isRequired,
    help: React.PropTypes.oneOfType([React.PropTypes.string, React.PropTypes.node]),
    isPristine: React.PropTypes.func.isRequired,
    isRequired: React.PropTypes.func.isRequired,
    label: React.PropTypes.oneOfType([React.PropTypes.string, React.PropTypes.node]).isRequired,
    name: React.PropTypes.string.isRequired,
    setValue: React.PropTypes.func.isRequired,
    showError: React.PropTypes.func.isRequired,
    showRequired: React.PropTypes.func.isRequired,
    type: React.PropTypes.string.isRequired,
};

export default HOC(Input);
