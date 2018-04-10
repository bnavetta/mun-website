import React from "react";
import {BrowserRouter as Router, NavLink, Route, Switch} from "react-router-dom";
import {Provider} from "react-redux";
import {hot} from "react-hot-loader";

import SchoolList from "./SchoolList";
import SchoolView from "./SchoolView";
import { fetchSchools } from "./api";
import { loadSchools } from "./state";

class StaffDashboard extends React.Component {
    componentDidMount() {
        fetchSchools()
            .then(schools => this.props.store.dispatch(loadSchools(schools)))
            .catch(e => console.error('Error loading schools', e));
    }

    render() {
        return(
            <Provider store={this.props.store}>
                <Router basename="/staff">
                    <div className="dashboard-root">
                        <ul className="dashboard-nav">
                            <li><NavLink exact to="/schools">Schools</NavLink></li>
                        </ul>

                        <div className="dashboard-main">
                            <Switch>
                                <Route exact path="/schools" component={SchoolList}/>
                                <Route path="/schools/:id" component={SchoolView} />
                            </Switch>
                        </div>
                    </div>
                </Router>
            </Provider>
        );
    }
}

export default hot(module)(StaffDashboard);
