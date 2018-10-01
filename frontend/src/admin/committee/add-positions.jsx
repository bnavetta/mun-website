// @flow

import React from 'react';
import request from '../../util/superagent';

export default class AddPositions extends React.Component {
    constructor() {
        super();
        this.state = { positions: [], newPosition: '' };

        this.handleNewPositionKeyDown = (event) => {
            if (event.keyCode === 13) {
                this.setState({
                    positions: [...this.state.positions, this.state.newPosition],
                    newPosition: '',
                });
            }
        };

        this.handleNewPositionChange = (event) => {
            this.setState({ newPosition: event.target.value });
        };

        this.handleSavePositions = () => {
            // TODO: absolute hard-coded URLs like this probably not great
            request
                .post(`/admin/committee/${window.committeeId}/add-positions`)
                .send(this.state.positions)
                .end((err) => {
                    if (err) {
                        // TODO: show to user
                        console.error(err); // eslint-disable-line no-console
                    } else {
                        // TODO: more bad hard-coded URLs
                        window.location = `/admin/committee/${window.committeeId}`;
                    }
                });
        };
    }

	render() {
        const positions = this.state.positions.map(position => <li key={position}>{position}</li>);

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
