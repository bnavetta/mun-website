import React from 'react';
import { HOC } from 'formsy-react';
import classNames from 'classnames';

class Checkbox extends React.Component {
	constructor(props, context) {
		super(props, context);

		this.handleChange = this.handleChange.bind(this);
		this.handleFocus = this.handleFocus.bind(this);
		this.handleBlur = this.handleBlur.bind(this);

		this.state = {
			focused: false,
		};
	}

	handleChange(e) {
		this.props.setValue(e.target.checked);
	}

	handleFocus() {
		this.setState({ focused: true });
	}

	handleBlur() {
		this.setState({ focused: false });
	}

	renderErrors() {
		// console.log(this.props.getErrorMessages());
		return this.props.getErrorMessages().map((message, i) => {
			return <div key={i} className="form-control-feedback">{message}</div>
		});
	}

	render() {
		const showError = (this.props.showRequired() || this.props.showError()) && !this.state.focused && !this.props.isPristine();

		return (
			<div className={classNames('form-check', { 'has-danger': showError })}>
				<label className="form-check-label">
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
		)
	}
}

Checkbox.propTypes = {
	label: React.PropTypes.oneOfType([React.PropTypes.string, React.PropTypes.node]).isRequired,
};

export default HOC(Checkbox);