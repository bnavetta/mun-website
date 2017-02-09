// @flow

import React from 'react';
import { HOC } from 'formsy-react';
import Slider from 'rc-slider';
import classNames from 'classnames';

import 'rc-slider/assets/index.css';

type CommitteePreferences = {
    general: number,
    specialized: number,
    crisis: number,
}

type Props = {
    getErrorMessages: () => Array<string>,
    getValue: () => CommitteePreferences,
    isPristine: () => boolean,
    isRequired: () => boolean,
    label: string,
    name: string,
    setValue: (CommitteePreferences) => void,
    showError: () => boolean,
    showRequired: () => boolean,
}

class PreferenceSlider extends React.Component {
    constructor(props: Props, context: Object) {
        super(props, context);

        this.state = {
            focused: false,
        };

        this.handleChange = ([low, high]) => {
            this.props.setValue({
                general: low,
                specialized: high - low,
                crisis: 100 - high,
            });
        };

        this.handleFocus = () => {
            this.setState({ focused: true });
        };

        this.handleBlur = () => {
            this.setState({ focused: false });
        };

        this.formatTooltip = (v, index) => {
            const value = this.props.getValue();
            if (index === 0) {
                return `General: ${value.general}%`;
            } else if (index === 1) {
                return `Specialized and Historical: ${value.specialized}%`;
            }

            return v.toString();
        };
    }

    state: {
        focused: boolean,
    }

    props: Props

    handleChange: (Array<number>) => void
    handleFocus: () => void
    handleBlur: () => void
    formatTooltip: (number, number) => string

    renderErrors() {
        // console.log(this.props.getErrorMessages());
        return this.props.getErrorMessages().map(message => <div key={message} className="form-control-feedback">{message}</div>);
    }

    render() {
        const showError = (this.props.showRequired() || this.props.showError())
            && !this.state.focused && !this.props.isPristine();

        const value = this.props.getValue();
        const lowValue = value.general;
        const highValue = value.specialized + lowValue;

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
                <Slider
                  id={this.props.name}
                  name={this.props.name}
                  onChange={this.handleChange}
                  onFocus={this.handleFocus}
                  onBlur={this.handleBlur}
                  value={[lowValue, highValue]}
                  range
                  tipFormatter={this.formatTooltip}
                />
                <span>
                    {`${value.general}% General Assembly, ${value.specialized}% Specialized and Historical, and ${value.crisis}% Crisis`}
                </span>
                { showError && this.renderErrors() }
            </div>
        );
    }
}

PreferenceSlider.propTypes = {
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

export default HOC(PreferenceSlider);
