import React from "react";
import PropTypes from "prop-types";
import { compose, withProps, withStateHandlers } from "recompose";
import { withScriptjs, withGoogleMap, GoogleMap, Marker, InfoWindow } from "react-google-maps";
import { hot } from 'react-hot-loader'

import LoadingPage from '../lib/components/LoadingPage';

class CommitteeMap extends React.PureComponent {
    constructor(props, context) {
        super(props, context);

        this.state = {
            openMarkers: {}
        };

        this.handleToggleOpen = this.handleToggleOpen.bind(this);
        this.setMap = this.setMap.bind(this);
    }

    handleToggleOpen(committeeId) {
        this.setState({
            openMarkers: { ...this.state.openMarkers, [committeeId]: !this.state.openMarkers[committeeId] },
        });
    }

    setMap(map) {
        this._map = map;
        this.updateBounds();
    }

    render() {
        const { committees, locations } = this.props;
        const { openMarkers } = this.state;

        return (
            <GoogleMap defaultZoom={2} ref={this.setMap}>
                { committees.map(committee => (
                    <Marker key={committee.id}
                            position={locations[committee.id]}
                            onClick={() => this.handleToggleOpen(committee.id)}
                    >
                        { openMarkers[committee.id] && <InfoWindow onCloseClick={() => this.handleToggleOpen(committee.id)}>
                            <React.Fragment>
                                <p>{ committee.name }</p>
                                <p>{ committee.description }</p>
                            </React.Fragment>
                        </InfoWindow> }
                    </Marker>
                )) }
            </GoogleMap>
        );
    }

    /**
     * Update the map to fit the locations given in props.locations.
     */
    updateBounds() {
        if (!this._map) {
            return;
        }

        const bounds = new google.maps.LatLngBounds();
        for (let location of Object.values(this.props.locations)) {
            bounds.extend(location);
        }

        this._map.fitBounds(bounds);
    }
}

CommitteeMap = compose(
    withProps({
        googleMapURL: "https://maps.googleapis.com/maps/api/js?v=3.exp&libraries=geometry&key=AIzaSyD25YgeQlpx3Rg2N9P9K5eXJv8AUXRGbtE",
        loadingElement: <LoadingPage />,
        containerElement: <div className="committee-map-container" />,
        mapElement: <div className="committee-map-container-map" />,
    }),
    withScriptjs,
    withGoogleMap,

    withStateHandlers(() => ({
        openMarkers: {}
    }), {
        onToggleOpen: ({ openMarkers }) => (committeeId) => ({
            openMarkers: { ...openMarkers, [committeeId]: !openMarkers[committeeId] }
        })
    })
)(CommitteeMap);

export default hot(module)(CommitteeMap);

CommitteeMap.propTypes = {
    locations: PropTypes.objectOf(PropTypes.shape({ lat: PropTypes.number.isRequired, lng: PropTypes.number.isRequired })),
    committees: PropTypes.arrayOf(PropTypes.shape({
        id: PropTypes.number.isRequired,
        name: PropTypes.string.isRequired,
    })),
};