import { createStore, combineReducers } from "redux";
import * as R from "ramda";

// Action for loading schools into the store
const LOAD_SCHOOLS = "schools:load";

// Action for loading advisors into the store
const LOAD_ADVISORS = "advisors:load";

// Action for loading a particular school's supplemental information
const LOAD_SUPPLEMENTAL_INFO = "supplemental_info:load";

// Schools reducer - handles a [school id -> school] map
function schoolsReducer(state = {}, action) {
    switch (action.type) {
        // The LOAD_SCHOOLS action just replaces our school map completely
        case LOAD_SCHOOLS:
            const schools = {};
            for (let school of action.schools) {
                schools[school.id] = school;
            }
            return schools;
        case LOAD_SUPPLEMENTAL_INFO:
            return {
                ...state,
                [action.id]: {
                    ...state[action.id],
                    supplementalInfo: action.supplementalInfo,
                },
            };
        default:
            return state;
    }
}

// Advisors reducer - handles a [school id -> list of advisors] map
function advisorsReducer(state = {}, action) {
    switch (action.type) {
        case LOAD_ADVISORS:
            return R.groupBy(R.prop('schoolId'), action.advisors);
        default:
            return state;
    }
}

// Action creator for loading schools
// It's good practice to wrap actions in these creator functions to encapsulate the internal structure that the
// reducer uses.
export const loadSchools = (schools) => ({ type: LOAD_SCHOOLS, schools });

export const loadAdvisors = (advisors) => ({ type: LOAD_ADVISORS, advisors });

export const loadSupplementalInfo = (id, supplementalInfo) => ({ type: LOAD_SUPPLEMENTAL_INFO, id, supplementalInfo });

// Selectors like this are a handy Redux pattern so you don't have to change a bunch of things if your state
// structure changes a bit
export const selectSchool = (id, state) => state.schools[id];

export const selectSchools = (state) => R.sortBy(R.prop('name'), R.values(state.schools));

export const selectAdvisors = (schoolId, state) => state.advisors[schoolId];

export const selectSupplementalInfo = (schoolId, state) => state.schools[schoolId].supplementalInfo;

export default function configureStore() {
    return createStore(combineReducers({
        schools: schoolsReducer,
        advisors: advisorsReducer,
    }), window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__());

    // There's a browser extension you can install (https://github.com/zalmoxisus/redux-devtools-extension) to get
    // Redux tools within your browser developer tools.
}