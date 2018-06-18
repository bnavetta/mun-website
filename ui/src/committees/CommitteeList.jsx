import React from "react";
import PropTypes from "prop-types";
import { hot } from "react-hot-loader";

function Committee({ committee }) {
    return (
        <React.Fragment>
            <div className="committees-list__committee-overview">
                <img
                    className="committees-list__committee__image"
                    src={committee.image}
                />
                <p className="committees-list__committee__name">
                    {committee.name}
                </p>
            </div>
            <div className="committees-list__committee-details">
                <p>{committee.description}</p>
                <ul>
                    {committee.topic1 && <li>{committee.topic1}</li>}
                    {committee.topic2 && <li>{committee.topic2}</li>}
                    {committee.topic3 && <li>{committee.topic3}</li>}
                    {committee.topic4 && <li>{committee.topic4}</li>}
                </ul>
            </div>
        </React.Fragment>
    );
}

const CommitteeType = PropTypes.shape({
    id: PropTypes.number.isRequired,
    name: PropTypes.string.isRequired,
    image: PropTypes.string.isRequired,
    description: PropTypes.string.isRequired,
    topic1: PropTypes.string,
    topic2: PropTypes.string,
    topic3: PropTypes.string,
    topic4: PropTypes.string,
});

Committee.propTypes = {
    committee: CommitteeType,
};

function CommitteeList({ committees }) {
    return (
        <ul className="committees-list">
            {committees.map(c => (
                <li key={c.id} className="committees-list__committee">
                    <Committee committee={c} />
                </li>
            ))}
        </ul>
    );
}

CommitteeList.propTypes = {
    committees: PropTypes.arrayOf(CommitteeType),
};

export default hot(module)(CommitteeList);
