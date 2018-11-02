import { combineReducers, createStore } from "redux";

import { schoolsReducer, advisorsReducer } from "./school";
import { printReducer } from "./print";

export default function configureStore() {
    return createStore(
        combineReducers({
            schools: schoolsReducer,
            advisors: advisorsReducer,
            print: printReducer,
        }),
        window.__REDUX_DEVTOOLS_EXTENSION__ &&
            window.__REDUX_DEVTOOLS_EXTENSION__()
    );

    // There's a browser extension you can install (https://github.com/zalmoxisus/redux-devtools-extension) to get
    // Redux tools within your browser developer tools.
}
