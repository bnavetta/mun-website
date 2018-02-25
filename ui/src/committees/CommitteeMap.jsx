import React from "react";
import PropTypes from "prop-types";
import { compose, withProps, withStateHandlers } from "recompose";
import { withScriptjs, withGoogleMap, GoogleMap, Marker, InfoWindow } from "react-google-maps";
import { hot } from 'react-hot-loader'

import LoadingPage from '../lib/components/LoadingPage';

const CommitteeMap = compose(
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
)(props => (
    <GoogleMap
        defaultZoom={2}
        defaultCenter={{ lat: 44.010549, lng: 25.2794373 }}
    >
        { props.committees.map(committee => (
            <Marker key={committee.id}
                    position={props.locations[committee.id]}
                    onClick={() => props.onToggleOpen(committee.id)}
            >
                { props.openMarkers[committee.id] && <InfoWindow onCloseClick={() => props.onToggleOpen(committee.id)}>
                    <p>{ committee.name }</p>
                </InfoWindow> }
            </Marker>
        )) }
    </GoogleMap>
));

export default hot(module)(CommitteeMap);

CommitteeMap.propTypes = {
    locations: PropTypes.objectOf(PropTypes.shape({ lat: PropTypes.number.isRequired, lng: PropTypes.number.isRequired })),
    committees: PropTypes.arrayOf(PropTypes.shape({
        id: PropTypes.number.isRequired,
        name: PropTypes.string.isRequired,
    })),
};