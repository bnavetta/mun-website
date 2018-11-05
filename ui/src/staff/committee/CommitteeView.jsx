import React from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";

import { displayError } from "../../lib/util";

import {fetchAwards, fetchPositions, grantAward, updateAttendance} from "../api";
import {
    loadAwards,
    loadPositions,
    selectCommittee,
    updateAttendance as updateAttendanceAction,
} from "../state/committee";

import "./CommitteeView.css";
import "./Awards";
import Awards from "./Awards";

function AttendanceCheckbox({ position, session, submitAttendance}) {
    return (
        <input
            type="checkbox"
            checked={position[session]}
            disabled={!position.schoolName}
            onChange={event => submitAttendance(position.id, session, event.target.checked)}
        />
    );
}

AttendanceCheckbox.propTypes = {
    position: PropTypes.object.isRequired,
    session: PropTypes.string.isRequired,
    submitAttendance: PropTypes.func.isRequired,
};

const mapStateToProps = (state, props) => {
    const id = parseInt(props.match.params.id);
    return {
        committee: selectCommittee(id, state),
    };
};

class CommitteeView extends React.Component {
    constructor(props, context) {
        super(props, context);

        this.submitAttendance = this.submitAttendance.bind(this);
        this.handleChangeAward = this.handleChangeAward.bind(this);
    }

    componentDidMount() {
        const committeeId = this.props.committee.id;
        fetchPositions(committeeId)
            .then(positions =>
                this.props.dispatch(loadPositions(committeeId, positions))
            )
            .catch(e => {
                console.log("Error loading committee positions", e);
                displayError("Unable to load positions");
            });

        fetchAwards(committeeId)
            .then(awards =>
                this.props.dispatch(loadAwards(committeeId, awards))
            )
            .catch(e => {
                console.log("Error loading committee awards", e);
                displayError("Unable to load awards");
            });
    }

    async submitAttendance(position, session, present) {
        try {
            await updateAttendance(
                this.props.committee.id,
                position,
                session,
                present
            );

            this.props.dispatch(
                updateAttendanceAction(
                    this.props.committee.id,
                    position,
                    session,
                    present
                )
            );
        } catch (e) {
            console.error(
                `Error submitting attendance for ${position} in ${session}`,
                e
            );
            displayError("Error saving attendance");
        }
    }

    async handleChangeAward(awardId, positionId) {
        try {
            const committeeId = this.props.committee.id;
            await grantAward(committeeId, awardId, positionId);
            const awards = await fetchAwards(committeeId);
            this.props.dispatch(loadAwards(committeeId, awards));
        } catch (e) {
            console.error(
                `Error granting award ${awardId} to ${positionId}`,
                e
            );
            displayError("Error saving award");
        }
    }

    render() {
        const { committee } = this.props;

        return (
            <div>
                <h1>{committee.name}</h1>

                <h2>Attendance</h2>

                <table className="standard-table">
                    <thead>
                        <tr>
                            <th>Position</th>
                            <th>Delegate</th>
                            <th>School</th>
                            <th colSpan={6}>Attendance</th>
                        </tr>
                        <tr>
                            <th colSpan={3} />
                            <th>Position Paper</th>
                            <th>Session I</th>
                            <th>Session II</th>
                            <th>Session III</th>
                            <th>Session IV</th>
                            <th>Session V</th>
                        </tr>
                    </thead>
                    <tbody>
                        {(committee.positions || []).map(position => (
                            <tr key={position.id}>
                                <td>{position.name}</td>
                                <td>{position.delegateName}</td>
                                <td>{position.schoolName}</td>
                                <td className="CommitteeView--attendance">
                                    <AttendanceCheckbox
                                        position={position}
                                        session="positionPaper"
                                        submitAttendance={this.submitAttendance}
                                    />
                                </td>
                                <td className="CommitteeView--attendance">
                                    <AttendanceCheckbox
                                        position={position}
                                        session="session1"
                                        submitAttendance={this.submitAttendance}
                                    />
                                </td>
                                <td className="CommitteeView--attendance">
                                    <AttendanceCheckbox
                                        position={position}
                                        session="session2"
                                        submitAttendance={this.submitAttendance}
                                    />
                                </td>
                                <td className="CommitteeView--attendance">
                                    <AttendanceCheckbox
                                        position={position}
                                        session="session3"
                                        submitAttendance={this.submitAttendance}
                                    />
                                </td>
                                <td className="CommitteeView--attendance">
                                    <AttendanceCheckbox
                                        position={position}
                                        session="session4"
                                        submitAttendance={this.submitAttendance}
                                    />
                                </td>
                                <td className="CommitteeView--attendance">
                                    <AttendanceCheckbox
                                        position={position}
                                        session="session5"
                                        submitAttendance={this.submitAttendance}
                                    />
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>

                <h2>Awards</h2>

                <Awards committee={committee} onChangeAward={this.handleChangeAward} />
            </div>
        );
    }
}
CommitteeView.propTypes = {
    committee: PropTypes.object,
    dispatch: PropTypes.func.isRequired,
};

export default connect(mapStateToProps)(CommitteeView);
