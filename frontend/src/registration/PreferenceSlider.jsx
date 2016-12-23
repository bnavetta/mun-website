import React from 'react';
import { HOC } from 'formsy-react';
import Slider from 'rc-slider';
import classNames from 'classnames';

import 'rc-slider/assets/index.css';

class PreferenceSlider extends React.Component {
	constructor(props, context) {
		super(props, context);

		this.handleChange = this.handleChange.bind(this);
		this.handleFocus = this.handleFocus.bind(this);
		this.handleBlur = this.handleBlur.bind(this);
		this.formatTooltip = this.formatTooltip.bind(this);

		this.state = {
			focused: false,
		};
	}

	handleChange([low, high]) {
		this.props.setValue({
			general: low,
			specialized: high - low,
			crisis: 100 - high,
		});
	}

	handleFocus() {
		this.setState({ focused: true });
	}

	handleBlur() {
		this.setState({ focused: false });
	}

	formatTooltip(v, index) {
		const value = this.props.getValue();
		if (index === 0) {
			return `General: ${value.general}%`;
		} else if (index === 1) {
			return `Specialized and Historical: ${value.specialized}%`;
		} else {
			return value;
		}
	}

	renderErrors() {
		// console.log(this.props.getErrorMessages());
		return this.props.getErrorMessages().map((message, i) => {
			return <div key={i} className="form-control-feedback">{message}</div>
		});
	}

	render() {
		const showError = (this.props.showRequired() || this.props.showError()) && !this.state.focused && !this.props.isPristine();

		// className={classNames('form-control', { 'form-control-danger': showError })}

		const value = this.props.getValue();
		const lowValue = value.general;
		const highValue = value.specialized + lowValue;

		return (
			<div className={classNames('form-group', { 'has-danger': showError })}>
				{this.props.label &&
				<label
					className="form-control-label"
					htmlFor={this.props.name}>
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
		)
	}
}

export default HOC(PreferenceSlider);