import React from 'react';
import request from 'superagent';

export default class AddPositions extends React.Component {
	constructor(props) {
		super(props);
		this.state = {positions: [], newPosition: ''};

		this.handleNewPositionKeyDown = this.handleNewPositionKeyDown.bind(this);
		this.handleNewPositionChange = this.handleNewPositionChange.bind(this);
		this.handleSavePositions = this.handleSavePositions.bind(this);
	}

	handleNewPositionKeyDown(event) {
		if (event.keyCode === 13) {
			this.setState({
				positions: [...this.state.positions, this.state.newPosition],
				newPosition: '',
			});
		}
	}

	handleNewPositionChange(event) {
		this.setState({newPosition: event.target.value});
	}

	handleSavePositions() {
		// TODO: absolute hard-coded URLs like this probably not great
		request
			.post(`/admin/committee/${window.committeeId}/add-positions`)
			.send(this.state.positions)
			.end(err => {
				if (err) {
					console.error(err); // TODO: show to user
				} else {
					// TODO: more bad hard-coded URLs
					window.location = `/admin/committee/${window.committeeId}`;
				}
			});
	}

	render() {
		const positions = this.state.positions.map(position => {
			return <li key={position}>{position}</li>;
		});

		return (
			<div>
				<input
					placeholder="Position Name"
					value={this.state.newPosition}
					onKeyDown={this.handleNewPositionKeyDown}
					onChange={this.handleNewPositionChange}
					/>
				<ul>
					{positions}
				</ul>
				<button className="btn btn-primary" onClick={this.handleSavePositions}>Save Positions</button>
			</div>
		);
	}
}
