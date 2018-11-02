import React from "react";
import { filter, identity } from "ramda";
import { connect } from "react-redux";
import { fetchSupplementalInfo } from "../api";
import { loadSupplementalInfo, selectSupplementalInfo } from "../state/school";
import ErrorPage from "../../lib/components/ErrorPage";
import LoadingPage from "../../lib/components/LoadingPage";

function luggageStorage(storage) {
    switch (storage) {
        case "FRIDAY":
            return "Friday";
        case "SUNDAY":
            return "Sunday";
        case "BOTH":
            return "Both days";
        case "NONE":
            return "Neither day";
        default:
            return "Unknown";
    }
}

function SupplementalInfo({ info }) {
    let financialAid;
    if (info.financialAid) {
        financialAid = (
            <dl className="prop-list">
                <dt>Financial Aid Amount</dt>
                <dd>{`$${info.financialAidAmount}`}</dd>
                <dt>Aid Documentation</dt>
                <dd>{info.financialAidDocumentation}</dd>
            </dl>
        );
    } else {
        financialAid = <p>Not requesting financial aid.</p>;
    }

    const shuttleDays = filter(identity, [
        info.shuttleFridayAfternoon && "Friday Afternoon",
        info.shuttleFridayNight && "Friday Night",
        info.shuttleSaturday && "Saturday",
        info.shuttleSunday && "Sunday",
    ]);

    const commuting = info.commuting ? (
        <dl className="prop-list">
            <dt>Cars being parked</dt>
            <dd>{info.carsParking}</dd>
            <dt>Days parking cars</dt>
            <dd>{info.carParkingDays}</dd>
            <dt>Buses being parked</dt>
            <dd>{info.busParking}</dd>
            <dt>Days parking buses</dt>
            <dd>{info.busParkingDays}</dd>
        </dl>
    ) : (
        <p>Not commuting.</p>
    );

    return (
        <div className="supplemental-info">
            <dl className="prop-list">
                <dt>Phone Number</dt>
                <dd>{info.phoneNumber}</dd>

                <dt>School Address</dt>
                <dd>{`${info.streetAddress || ""}, ${info.city ||
                    ""} ${info.postalCode || ""}, ${info.country || ""}`}</dd>

                <dt>Hotel</dt>
                <dd>
                    {info.busunHotel
                        ? info.busunHotel.name
                        : info.nonBusunHotel}
                </dd>

                <dt>Shuttle Service</dt>
                <dd>{shuttleDays.join(", ")}</dd>

                <dt>Arrival Time</dt>
                <dd>{info.arrivalTime}</dd>

                <dt>Storing Luggage</dt>
                <dd>{luggageStorage(info.luggageStorage)}</dd>

                <dt>Delegate Social Band Color</dt>
                <dd>{info.delegateSocialNeedAdvisor ? "Red" : "Yellow"}</dd>

                <dt>Delegate Count</dt>
                <dd>{info.delegateCount}</dd>

                <dt>Chaperone Count</dt>
                <dd>{info.chaperoneCount}</dd>

                <dt>Parliamentary Procedure Training</dt>
                <dd>{info.parliProTrainingCount}</dd>

                <dt>Crisis Training Count</dt>
                <dd>{info.crisisTrainingCount}</dd>

                <dt>Tour Count</dt>
                <dd>{info.tourCount}</dd>

                <dt>Info Session Count</dt>
                <dd>{info.infoSessionCount}</dd>
            </dl>

            {financialAid}
            {commuting}

            <h3>Chaperone Information</h3>
            <p>{info.chaperoneInfo}</p>
        </div>
    );
}

class SupplementalInfoWrapper extends React.Component {
    constructor(props, context) {
        super(props, context);

        this.state = {
            error: null,
        };
    }

    componentDidMount() {
        this.loadSupplementalInfo();
    }

    loadSupplementalInfo() {
        console.log(`Loading supplemental information for ${this.props.id}`);
        fetchSupplementalInfo(this.props.id)
            .then(info =>
                this.props.dispatch(loadSupplementalInfo(this.props.id, info))
            )
            .catch(error => this.setState({ error }));
    }

    render() {
        const { error } = this.state;
        const { supplementalInfo } = this.props;

        if (error) {
            return <ErrorPage error={error} />;
        } else if (supplementalInfo) {
            return <SupplementalInfo info={supplementalInfo} />;
        } else {
            return <LoadingPage />;
        }
    }
}

const mapStateToProps = (state, props) => ({
    supplementalInfo: selectSupplementalInfo(props.id, state),
});

export default connect(mapStateToProps)(SupplementalInfoWrapper);
