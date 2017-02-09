// @flow

import React from 'react';
// Might not actually need sorting?
// Just column filtering?

const columnDefinitions = [
    { field: 'id', title: 'ID' },
    { field: 'name', title: 'School Name' },
    { field: 'status', title: 'Registration Status' },
    { field: 'requestedDelegates', title: 'Delegation Size' },
    { field: 'registrationTime', title: 'Registration Time' },
    { field: 'numberOfYearsAttended', title: 'Years Attended' },
];

type Props = {
    schools: Array<Object>,
};

export default class SchoolTable extends React.Component {
    constructor(props: Props) {
        super(props);

        this.state = {
            visibleColumns: { id: true, name: true, status: true, registrationTime: true },
        };
    }

    state: {
        visibleColumns: { [columnName: string]: boolean },
    }

    props: Props

    toggleColumn(field: string) {
        const { visibleColumns } = this.state;
        this.setState({ visibleColumns: { ...visibleColumns, [field]: !visibleColumns[field] } });
    }

    render() {
        const { schools } = this.props;
        const columns = columnDefinitions.filter(col => this.state.visibleColumns[col.field]);
        const hiddenColumns = columnDefinitions.filter(
            col => !this.state.visibleColumns[col.field],
        );

        const header = columns.map(col => (
            <th
              key={col.field}
            >
                <button className="btn" onClick={() => this.toggleColumn(col.field)}>{col.title}</button>
            </th>
        ));

        const reactivateToggles = hiddenColumns.map(col => (
            <li
              className="nav-item"
              key={col.field}
            >
                <button
                  className="btn nav-link"
                  onClick={() => this.toggleColumn(col.field)}
                >
                    {col.title}
                </button>
            </li>
        ));

        const rows = schools.map(school => (
            <tr key={school.id}>
                {columns.map(col => <td key={col.field}>{school[col.field]}</td>)}
                <td><a href={`profile/${school.id}`}>Profile</a></td>
            </tr>
        ));

        return (
            <div>
                <ul className="nav nav-pills">
                    {reactivateToggles}
                </ul>
                <table className="table">
                    <thead>
                        <tr>
                            {header}
                            <th />
                        </tr>
                    </thead>
                    <tbody>
                        {rows}
                    </tbody>
                </table>
            </div>
        );
    }
}

SchoolTable.propTypes = {
    schools: React.PropTypes.arrayOf(React.PropTypes.object).isRequired,
};
