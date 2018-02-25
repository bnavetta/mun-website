import React from "react";
import PropTypes from "prop-types";
import { hot } from "react-hot-loader";

function CommitteeList({ committees }) {
    return (
        <ul className="committees-list">
            {
                committees.map(c => <li className="committees-list__committee" key={c.id}>
                    <img className="committees-list__committee__image" src="https://placebear.com/600/400" />
                    <p>{c.name}</p>
                </li>)
            }
        </ul>
    );
}

CommitteeList.propTypes = {
    committees: PropTypes.arrayOf(PropTypes.shape({
        id: PropTypes.number.isRequired,
        name: PropTypes.string.isRequired,
    }))
};

export default hot(module)(CommitteeList);