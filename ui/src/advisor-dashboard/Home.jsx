import React from "react";
import { hot } from "react-hot-loader";

function Home({ advisor, school }) {
    return (
        <React.Fragment>
            <h1>{ school.name }</h1>
            <hr/>
            <h3>Advisor: <a href={'mailto:' + advisor.email}>{advisor.name}</a></h3>
            <dl className="prop-list">
                <dt>Application Submitted</dt>
                <dd>{ school.hasApplied ? 'Yes' : 'No' }</dd>
                {/*<dt>Accepted</dt>*/}
                {/*<dd>{ school.accepted ? 'Yes' : 'No' }</dd>*/}
                <dt>Registration Code</dt>
                <dd>{ school.registrationCode }</dd>
            </dl>
        </React.Fragment>
    )
}

export default hot(module)(Home);