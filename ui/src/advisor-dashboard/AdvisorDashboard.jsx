import React from "react";
import { hot } from "react-hot-loader";
import { NavLink, Switch, Route, BrowserRouter } from "react-router-dom";

import LoadingPage from "../lib/components/LoadingPage";
import ErrorPage from "../lib/components/ErrorPage";

import { fetchAdvisor, fetchSchool } from "./api";
import Home from "./Home";
import "./dashboard.css";
import Application from "./Application";
import SupplementalInfo from "./SupplementalInfo";
import ChangePassword from "./ChangePassword";
import Delegation from "./Delegation";

function DashboardContent({ advisor, school, setSchool }) {
    return (
        <div className="dashboard-root">
            <ul className="dashboard-nav">
                <li>
                    <NavLink exact to="/">
                        Home
                    </NavLink>
                </li>
                {school.accepted && (
                    <li>
                        <NavLink exact to="/delegation">
                            Delegation
                        </NavLink>
                    </li>
                )}
                <li>
                    <NavLink exact to="/application">
                        Application
                    </NavLink>
                </li>
                {school.accepted && (
                    <li>
                        <NavLink exact to="/supplemental">
                            Supplemental Information
                        </NavLink>
                    </li>
                )}
                <li>
                    <NavLink exact to="/change-password">
                        Change Password
                    </NavLink>
                </li>
            </ul>

            <div className="dashboard-main">
                <Switch>
                    <Route
                        path="/application"
                        render={props => (
                            <Application
                                {...props}
                                school={school}
                                setSchool={setSchool}
                            />
                        )}
                    />
                    {school.accepted && (
                        <Route
                            path="/delegation"
                            render={props => (
                                <Delegation {...props} school={school} />
                            )}
                        />
                    )}
                    {school.accepted && (
                        <Route
                            path="/supplemental"
                            render={props => (
                                <SupplementalInfo {...props} school={school} />
                            )}
                        />
                    )}
                    <Route
                        exact
                        path="/change-password"
                        component={ChangePassword}
                    />
                    <Route
                        exact
                        path="/"
                        render={props => (
                            <Home
                                {...props}
                                school={school}
                                advisor={advisor}
                            />
                        )}
                    />
                </Switch>
            </div>
        </div>
    );
}

class AdvisorDashboard extends React.PureComponent {
    constructor(props, context) {
        super(props, context);

        this.state = {
            loading: true,
            error: null,
            advisor: null,
            school: null,
        };

        this.setSchool = this.setSchool.bind(this);
    }

    componentDidMount() {
        Promise.all([fetchAdvisor(), fetchSchool()])
            .then(([advisor, school]) => {
                this.setState({ advisor, school, loading: false });
            })
            .catch(error => this.setState({ error, loading: false }));
    }

    setSchool(school) {
        this.setState({ school });
    }

    render() {
        const { loading, error, advisor, school } = this.state;
        if (loading) {
            return <LoadingPage />;
        } else if (error) {
            return <ErrorPage error={error} />;
        } else {
            return (
                <BrowserRouter basename="/your-mun/">
                    <DashboardContent
                        advisor={advisor}
                        school={school}
                        setSchool={this.setSchool}
                    />
                </BrowserRouter>
            );
        }
    }
}

export default hot(module)(AdvisorDashboard);
