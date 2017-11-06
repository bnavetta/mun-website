import React from 'react';
import { Portal } from 'react-portal';
import { FadeLoader } from 'react-spinners';
import Moment from 'react-moment';
import moment from 'moment';
import request from '../../util/superagent';

import Attendance from './Attendance';

export default class AttendancePage extends React.Component {
    constructor(props, context) {
        super(props, context);

        this.state = {
            attendance: [],
            error: null,
            loading: false,
            lastSaved: null
        };

        this.handleAttendanceChange = this.handleAttendanceChange.bind(this);
    }

    componentDidMount() {
        this.setState({ loading: true });
        request.get('').accept('json').then(r => {
            if (!r.ok) {
                this.setState({ error: 'Unable to load positions', loading: false });
            } else {
                this.setState({ attendance: r.body, loading: false });
            }
        }).catch(e => {
            console.error('Error loading positions', e);
            this.setState({ error: 'Unable to load positions', loading: false });
        });
    }

    handleAttendanceChange(attendance) {
        this.setState({ loading: true });
        request.post('').send(attendance).then(r => {
            if (r.ok) {
                this.setState({
                    attendance: this.state.attendance.map(a => a.positionId === attendance.positionId ? attendance : a),
                    loading: false,
                    lastSaved: moment()
                });
            } else {
                this.setState({ error: r.body.message, loading: false });
            }
        }).catch(e => {
            console.error('Error updating attendance', e);
            this.setState({ error: 'Unable to update attendance', loading: false });
        });
    }

    render() {
        return (
            <div>
                <Portal node={document && document.getElementById('loading-container')}>
                    <FadeLoader color='#007f5c' loading={this.state.loading}/>
                </Portal>
                { this.state.error && <div className="alert alert-danger">{ this.state.error }</div> }
                { this.state.lastSaved && <p className="text-muted mx-auto text-center">Saved <Moment fromNow date={this.state.lastSaved}/></p> }
                <table className="table">
                    <thead>
                        <tr>
                            <th>Position</th>
                            <th className="text-center">Position Paper Submitted?</th>
                            <th className="text-center">Session I</th>
                            <th className="text-center">Session II</th>
                            <th className="text-center">Session III</th>
                            <th className="text-center">Session IV</th>
                        </tr>
                    </thead>
                    <tbody>
                        { this.state.attendance.map(a => <Attendance key={a.positionId} attendance={a} onChange={this.handleAttendanceChange} />) }
                    </tbody>
                </table>
            </div>
        )
    }
}