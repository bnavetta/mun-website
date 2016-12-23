import React from 'react';
import { HOC } from 'formsy-react';
import classNames from 'classnames';

class Select extends React.Component {
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
		// console.log(this.props.getErrorMessages());
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
				<select
					className={classNames('form-control', { 'form-control-danger': showError })}
					id={this.props.name}
					name={this.props.name}
					onChange={this.handleChange}
					onFocus={this.handleFocus}
					onBlur={this.handleBlur}
					value={this.props.getValue()}
				>
					{ this.props.options.map(({ label, value}) => <option value={value} key={value}>{label}</option> ) }
				</select>
				{ showError && this.renderErrors() }
				{ this.props.help && <p className="form-text text-muted">{this.props.help}</p> }
			</div>
		)
	}
}

Select.propTypes = {
	help: React.PropTypes.oneOfType([React.PropTypes.string, React.PropTypes.node]),
	label: React.PropTypes.oneOfType([React.PropTypes.string, React.PropTypes.node]),
	options: React.PropTypes.arrayOf(React.PropTypes.shape({
		label: React.PropTypes.string.isRequired,
		value: React.PropTypes.string.isRequired
	})),
};

export default HOC(Select);