import React from "react";
import PropTypes from "prop-types";
import { hot } from "react-hot-loader";
import { RingLoader } from "react-spinners";
import Raven from "raven-js";

import LoadingPage from "../lib/components/LoadingPage";
import ErrorPage from "../lib/components/ErrorPage";
import { displayError, getVariable } from "../lib/util";

import { fetchDelegates, setDelegateName } from "./api";

/**
 * React component to display a table of the school's delegates.
 */
class Delegation extends React.PureComponent {
    constructor(props, context) {
        super(props, context);

        this.state = {
            loading: true,
            error: null,
            delegates: [],
        };
    }

    componentDidMount() {
        this.loadDelegates();
    }

    async loadDelegates() {
        try {
            const delegates = await fetchDelegates();

            this.setState({
                delegates,
                loading: false,
                error: null,
            });
        } catch (error) {
            Raven.captureException(error);
            displayError(`Unable to get delegates: ${error}`);
            this.setState({
                delegates: [],
                loading: false,
                error,
            });
        }
    }

    async setDelegateName(index, id, name) {
        try {
            // Use ... to make a shallow copy of the array values
            let delegates = [...this.state.delegates];
            delegates[index].isChanging = true;
            await setDelegateName(id, name);

            delegates = [...this.state.delegates];
            delegates[index].name = name;
            delegates[index].isChanging = false;
            this.setState({ delegates });
        } catch (error) {
            Raven.captureException(error);
            displayError(`Unable to change name: ${error}`);

            const delegates = [...this.state.delegates];
            delegates[index].isChanging = false;
            this.setState({ delegates });
        }
    }

    renderDelegates(delegates) {
        return (
            <table className="standard-table">
                <thead>
                    <tr>
                        <th>Delegate Name</th>
                        <th>Position</th>
                        <th>Committee</th>
                        <th>Link to Gatherly</th>
                    </tr>
                </thead>
                <tbody>
                    {delegates.map((d, idx) => (
                        <tr key={d.id}>
                            <td>
                                <input
                                    type="text"
                                    value={d.name || ""}
                                    onChange={e =>
                                        this.setDelegateName(
                                            idx,
                                            d.id,
                                            e.target.value
                                        )
                                    }
                                />
                                {d.isChanging && (
                                    <RingLoader
                                        color={getVariable("--color-primary")}
                                        size={1.2}
                                        sizeUnit="rem"
                                        loading={true}
                                        // Emotion does something weird where className works more like style
                                        className={{
                                            display: "inline-block",
                                            verticalAlign: "middle",
                                            marginLeft: "5px",
                                        }}
                                    />
                                )}
                            </td>
                            <td>{d.positionName || "Unassigned"}</td>
                            <td>{d.committeeName || "Unassigned"}</td>
                            <td>{<a href={d.gatherlyLink}>Click Here</a> || "Link Not Found"}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        );
    }

    render() {
        const { loading, error, delegates } = this.state;

        if (loading) {
            return <LoadingPage />;
        } else if (error) {
            return <ErrorPage error={error} />;
        } else {
            return this.renderDelegates(delegates);
        }
    }
}

Delegation.propTypes = {
    school: PropTypes.object.isRequired,
};

export default hot(module)(Delegation);
