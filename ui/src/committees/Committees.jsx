import React from "react";
import { hot } from "react-hot-loader";
import { Tab, Tabs, TabList, TabPanel } from "react-tabs";

import LoadingPage from "../lib/components/LoadingPage";

import CommitteeMap from "./CommitteeMap";
import CommitteeList from "./CommitteeList";
import JointCrisisList from "./JointCrisisList";

async function fetchCommittees() {
    const response = await fetch("/api/committee");
    if (!response.ok) {
        throw new Error("Failed to load committees");
    }

    return await response.json();
}

const locations = {
    4: { lat: 40, lng: 40 },
    5: { lat: -30, lng: 20 },
};

class Committees extends React.Component {
    constructor(props, context) {
        super(props, context);

        this.state = {
            loading: true,
            error: null,
            committees: null,
        };
    }

    componentDidMount() {
        fetchCommittees()
            .then(committees => this.setState({ committees, loading: false }))
            .catch(error => this.setState({ error, loading: false }));
    }

    render() {
        var msg = "<p>Please scroll under each committee to access the descriptions and link for the background guides.</p><br><p>This year, delegates in crisis committees will be participating in two separate committees: one on Saturday, and another on Sunday. Your position assignments listed on the advisor portal reflect this. The first position listed is the delegate\'s committee and position on Saturday, and the position listed in parentheses is the delegate\'s committee and position on Sunday. Please email info@busun.org with any questions regarding this change.</p>";
        if (this.state.loading) {
            return <LoadingPage />;
        } else if (this.state.error) {
            // TODO: error page
            return null;
        } else {
            return (
                // <p>Coming soon!</p>
                <Tabs>
                    <div className="content" dangerouslySetInnerHTML={{ __html: msg }}></div>
                    <TabList>
                        <Tab>General</Tab>
                        <Tab>Specialized and Historical</Tab>
                        <Tab>Crisis</Tab>
                        <Tab>Joint Crisis</Tab>
                    </TabList>

                    {/* General */}
                    <TabPanel>
                        <CommitteeList
                            committees={this.state.committees.general}
                        />
                    </TabPanel>

                    {/* Specialized */}
                    <TabPanel>
                        <CommitteeList
                            committees={this.state.committees.specialized}
                        />
                    </TabPanel>
                    {/* <TabPanel>
                            <CommitteeMap
                                committees={this.state.committees.specialized}
                                locations={locations}
                            />
                        </TabPanel> */}

                    {/* Crisis */}
                    <TabPanel>
                        <CommitteeList
                            committees={this.state.committees.crisis}
                        />
                    </TabPanel>

                    {/* Joint crisis */}
                    <TabPanel>
                        <CommitteeList
                            committees={this.state.committees.jointCrises}
                        />
                    </TabPanel>
                    {/* <TabPanel>
                            <JointCrisisList
                                jointCrises={this.state.committees.jointCrises}
                                rooms={this.state.committees.jointCrisisRooms}
                            />
                        </TabPanel> */}
                </Tabs>
            );
        }
    }
}

export default hot(module)(Committees);
