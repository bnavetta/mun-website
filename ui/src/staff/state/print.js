import * as R from "ramda";

const MERGE_REQUEST = "print:merge-request";

const generateQueue = R.pipe(
    R.values,
    R.sortBy(R.prop("submissionTime")),
    R.map(R.prop("id"))
);

export function printReducer(state = { queue: [], requests: {} }, action) {
    switch (action.type) {
        case MERGE_REQUEST: {
            const newRequests = {
                ...state.requests,
                [action.payload.id]: action.payload,
            };

            return {
                requests: newRequests,
                queue: generateQueue(newRequests),
            };
        }
        default:
            return state;
    }
}

export function mergeRequest(request) {
    return { type: MERGE_REQUEST, payload: request };
}

export const selectPrintQueue = state =>
    R.map(R.prop(R.__, state.print.requests), state.print.queue);

export const selectActivePrintQueue = state =>
    R.filter(req => req.status !== "COMPLETED", selectPrintQueue(state));
