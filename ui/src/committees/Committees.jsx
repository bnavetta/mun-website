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
        if (this.state.loading) {
            return <LoadingPage />;
        } else if (this.state.error) {
            // TODO: error page
            return null;
        } else {
            return (
                <Tabs>
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
                        <CommitteeMap
                            committees={this.state.committees.specialized}
                            locations={locations}
                        />
                    </TabPanel>

                    {/* Crisis */}
                    <TabPanel>
                        <CommitteeList
                            committees={this.state.committees.crisis}
                        />
                    </TabPanel>

                    {/* Joint crisis */}
                    <TabPanel>
                        <JointCrisisList
                            jointCrises={this.state.committees.jointCrises}
                            rooms={this.state.committees.jointCrisisRooms}
                        />
                    </TabPanel>
                </Tabs>
            );
        }
    }
}

export default hot(module)(Committees);
