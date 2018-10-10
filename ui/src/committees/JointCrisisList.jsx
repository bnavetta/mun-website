import React from "react";
import PropTypes from "prop-types";
import { hot } from "react-hot-loader";

function JointCrisisList({ jointCrises, rooms }) {
    return (
        <ul className="committees-list">
            {jointCrises.map(jcc => (
                <li
                    className="committees-list__committee committees-list__joint_crisis"
                    key={jcc.id}
                >
                    <div className="committees-list__committee-overview">
                        <img
                            className="committees-list__committee__image"
                            src={jcc.image}
                        />
                        <p className="committees-list__committee__name">
                            {jcc.name}
                        </p>
                    </div>
                    <div className="committees-list__committee-details">
                        <p>{jcc.description}</p>
                        {jcc.email && <p>
                            Contact at <a href={"mailto:" + jcc.email}>{jcc.email}</a>
                        </p>}
                        <h3>Rooms:</h3>
                        <dl className="committees-jcc-room-list">
                            {(rooms[jcc.id] || []).map(room => (
                                <React.Fragment key={room.id}>
                                    <dt>{room.email ? <a href={"mailto:" + room.email}>{room.name}</a> : room.name}</dt>
                                    <dd>
                                        <p>{room.description}</p>
                                        {room.backgroundGuide && <p>
                                            <a href={room.backgroundGuide}>Background guide</a>
                                        </p>}
                                    </dd>
                                </React.Fragment>
                            ))}
                        </dl>
                    </div>
                </li>
            ))}
        </ul>
    );
}

const CommitteeType = PropTypes.shape({
    id: PropTypes.number.isRequired,
    name: PropTypes.string.isRequired,
});

JointCrisisList.propTypes = {
    jointCrises: PropTypes.arrayOf(CommitteeType),
    rooms: PropTypes.objectOf(CommitteeType),
};

export default hot(module)(JointCrisisList);
