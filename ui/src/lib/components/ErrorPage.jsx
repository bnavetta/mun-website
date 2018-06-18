import React from "react";
import PropTypes from "prop-types";

import "./error-page.css";

export default function ErrorPage({ error }) {
    return (
        <div className="error-page">
            <h2>An error has occurred:</h2>
            <p>{error.message}</p>
            <p>
                Please contact our{" "}
                <a href="mailto:technology@busun.org">Director of Technology</a>{" "}
                if the problem persists.
            </p>
        </div>
    );
}

ErrorPage.propTypes = {
    error: PropTypes.object.isRequired,
};
