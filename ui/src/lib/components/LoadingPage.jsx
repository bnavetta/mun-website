import React from "react";
import { RingLoader } from "react-spinners";

import { getVariable } from '../util';

import "./loading-page.css";

/**
 * Helper component that renders a spinner taking up all available space
 * @constructor
 */
export default function LoadingPage() {
    return (
        <div className="loading-page">
            <RingLoader color={getVariable('--color-primary')} loading={true} />
        </div>
    )
}