import React from 'react';
import { HOC } from 'formsy-react';
import classNames from 'classnames';

class Textarea extends React.Component {
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
		this.props.setValue(e.target.value);
	}

	handleFocus() {
		this.setState({ focused: true });
	}

	handleBlur() {
		this.setState({ focused: false });
	}

	renderErrors() {
		return this.props.getErrorMessages().map((message, i) => {
			return <div key={i} className="form-control-feedback">{message}</div>
		});
	}

	render() {
		const showError = (this.props.showRequired() || this.props.showError()) && !this.state.focused && !this.props.isPristine();

		return (
			<div className={classNames('form-group', { 'has-danger': showError })}>
				{this.props.label &&
				<label
					className="form-control-label"
					htmlFor={this.props.name}>
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
		)
	}
}

Textarea.propTypes = {
	label: React.PropTypes.oneOfType([React.PropTypes.string, React.PropTypes.node]),
};

export default HOC(Textarea);