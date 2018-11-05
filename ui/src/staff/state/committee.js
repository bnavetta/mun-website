import * as R from "ramda";

import { selectUserInfo } from "./user";

const LOAD_COMMITTEES = "committee:load";
const LOAD_POSITIONS = "committee:load-positions";
const UPDATE_ATTENDANCE = "committee:update-attendance";

export function committeesReducer(state = {}, action) {
    switch (action.type) {
        case LOAD_COMMITTEES:
            return R.fromPairs(R.map(c => [c.id, c], action.payload));
        case LOAD_POSITIONS:
            return {
                ...state,
                [action.committeeId]: {
                    ...state[action.committeeId],
                    positions: action.positions,
                },
            };
        case UPDATE_ATTENDANCE: {
            if (!state[action.committeeId]) {
                return state;
            }
            const posIdx = R.findIndex(R.propEq("id", action.positionId))(
                state[action.committeeId].positions || []
            );
            if (posIdx === -1) {
                return state;
            }

            return {
                ...state,
                [action.committeeId]: {
                    ...state[action.committeeId],
                    positions: [
                        ...state[action.committeeId].positions.slice(0, posIdx),
                        {
                            ...state[action.committeeId].positions[posIdx],
                            [action.session]: action.present,
                        },
                        ...state[action.committeeId].positions.slice(
                            posIdx + 1
                        ),
                    ],
                },
            };
        }
        default:
            return state;
    }
}

export function loadCommittees(committees) {
    return { type: LOAD_COMMITTEES, payload: committees };
}

export function loadPositions(committeeId, positions) {
    return { type: LOAD_POSITIONS, committeeId, positions };
}

export function updateAttendance(committeeId, positionId, session, present) {
    return {
        type: UPDATE_ATTENDANCE,
        committeeId,
        positionId,
        session,
        present,
    };
}

export const selectAllCommittees = state =>
    R.sortBy(R.prop("name"), R.values(state.committees));

export const selectUserCommittees = state => {
    const userInfo = selectUserInfo(state);
    if (userInfo.committees.length > 0) {
        return R.pipe(
            R.values,
            R.filter(c => R.contains(c.id, userInfo.committees)),
            R.sortBy(R.prop("name"))
        );
    } else {
        return selectAllCommittees(state);
    }
};

export const selectCommittee = (id, state) => state.committees[id];
