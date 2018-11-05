import React from "react";
import PropTypes from "prop-types";
import * as R from "ramda";

const byType = R.groupBy(R.prop("type"));

function PositionSelect({ positions, selectedId, onSelect }) {
    return (
        <select
            onChange={event => onSelect(event.target.value)}
            value={selectedId || ""}
        >
            <option value={""}>--</option>
            {positions.map(position => (
                <option key={position.id.toString()} value={position.id}>
                    {position.name}
                </option>
            ))}
        </select>
    );
}

PositionSelect.propTypes = {
    positions: PropTypes.arrayOf(PropTypes.object).isRequired,
    selectedId: PropTypes.number,
    onSelect: PropTypes.func.isRequired,
};

export default function Awards({ committee, onChangeAward }) {
    const awards = byType(committee.awards || []);

    return (
        <dl className="prop-list">
            <dt>Best Delegate</dt>
            {(awards["BEST_DELEGATE"] || []).map(award => (
                <dd key={award.id.toString()}>
                    <PositionSelect
                        positions={committee.positions || []}
                        selectedId={award.positionId}
                        onSelect={position => onChangeAward(award.id, position)}
                    />
                </dd>
            ))}
            <dt>Outstanding Delegate</dt>
            {(awards["OUTSTANDING_DELEGATE"] || []).map(award => (
                <dd key={award.id.toString()}>
                    <PositionSelect
                        positions={committee.positions || []}
                        selectedId={award.positionId}
                        onSelect={position => onChangeAward(award.id, position)}
                    />
                </dd>
            ))}
            <dt>Honorable Delegate</dt>
            {(awards["HONORABLE_DELEGATE"] || []).map(award => (
                <dd key={award.id.toString()}>
                    <PositionSelect
                        positions={committee.positions || []}
                        selectedId={award.positionId}
                        onSelect={position => onChangeAward(award.id, position)}
                    />
                </dd>
            ))}
        </dl>
    );
}

Awards.propTypes = {
    committee: PropTypes.object.isRequired,
    onChangeAward: PropTypes.func.isRequired,
};