import React from "react";
import PropTypes from "prop-types";
import {
    BrowserRouter as Router,
    NavLink,
    Route,
    Switch,
} from "react-router-dom";
import { Provider } from "react-redux";
import { hot } from "react-hot-loader";

import { displayError } from "../lib/util";
import LoadingPage from "../lib/components/LoadingPage";

import SchoolList from "./school/SchoolList";
import SchoolView from "./school/SchoolView";
import PrintQueue from "./printing/PrintQueue";
import CommitteeList from "./committee/CommitteeList";
import CommitteeView from "./committee/CommitteeView";
import { fetchSchools, fetchAdvisors, fetchCommittees } from "./api";
import { loadSchools, loadAdvisors } from "./state/school";
import { loadCommittees } from "./state/committee";

class StaffDashboard extends React.Component {
    constructor(props, context) {
        super(props, context);

        this.state = {
            loadedSchools: false,
            loadedAdvisors: false,
            loadedCommittees: false,
        };
    }

    componentDidMount() {
        fetchSchools()
            .then(schools => {
                this.props.store.dispatch(loadSchools(schools));
                this.setState({ loadedSchools: true });
            })
            .catch(e => {
                console.error("Error loading schools", e);
                displayError("Unable to load schools");
            });

        fetchAdvisors()
            .then(advisors => {
                this.props.store.dispatch(loadAdvisors(advisors));
                this.setState({ loadedAdvisors: true });
            })
            .catch(e => {
                console.error("Error loading advisors", e);
                displayError("Unable to load advisors");
            });

        fetchCommittees()
            .then(committees => {
                this.props.store.dispatch(loadCommittees(committees));
                this.setState({ loadedCommittees: true });
            })
            .catch(e => {
                console.error("Error loading committees", e);
                displayError("Unable to load committees");
            });
    }

    render() {
        if (
            !this.state.loadedSchools ||
            !this.state.loadedCommittees ||
            !this.state.loadedAdvisors
        ) {
            return <LoadingPage />;
        }

        return (
            <Provider store={this.props.store}>
                <Router basename="/staff">
                    <div className="dashboard-root">
                        <ul className="dashboard-nav">
                            <li>
                                <NavLink exact to="/schools">
                                    Schools
                                </NavLink>
                            </li>
                            <li>
                                <NavLink exact to="/committees">
                                    Committees
                                </NavLink>
                            </li>
                            <li>
                                <NavLink exact to="/print">
                                    Printing
                                </NavLink>
                            </li>
                        </ul>

                        <div className="dashboard-main">
                            <Switch>
                                <Route
                                    exact
                                    path="/schools"
                                    component={SchoolList}
                                />
                                <Route
                                    path="/schools/:id"
                                    component={SchoolView}
                                />
                                <Route
                                    exact
                                    path="/committees"
                                    component={CommitteeList}
                                />
                                <Route
                                    path="/committees/:id"
                                    component={CommitteeView}
                                />
                                <Route path="/print" component={PrintQueue} />
                            </Switch>
                        </div>
                    </div>
                </Router>
            </Provider>
        );
    }
}

StaffDashboard.propTypes = {
    store: PropTypes.object.isRequired,
};

export default hot(module)(StaffDashboard);
