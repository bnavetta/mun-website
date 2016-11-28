import React from 'react';
import request from 'superagent';

async function loadSchools() {
	console.log('Loading school data');
	const res = await request.get('/admin/school/list.json').accept('json');
	if (res.ok) {
		console.log('Loaded list of schools');
		return res.body;
	} else {
		console.error('Error loading schools', res.body.message);
		throw new Error(res.body.message);
	}
}

// Might not actually need sorting?
// Just column filtering?

const columnDefinitions = [
	{ field: 'id', title: 'ID' },
	{ field: 'name', title: 'School Name' },
	{ field: 'status', title: 'Registration Status' },
	{ field: 'registrationTime', title: 'Registration Time' }
]

class SchoolTable extends React.Component {
	constructor(props) {
		super(props);

		this.state = {
			visibleColumns: { id: true, name: true, status: true, registrationTime: true }
		};
	}

	toggleColumn(field) {
		const { visibleColumns } = this.state;
		this.setState({ visibleColumns: { ...visibleColumns, [field]: !visibleColumns[field] } });
	}

	render() {
		const { schools } = this.props;
		const columns = columnDefinitions.filter(col => this.state.visibleColumns[col.field]);
		const hiddenColumns = columnDefinitions.filter(col => !this.state.visibleColumns[col.field]);

		const header = columns.map(col => (
			<th key={col.field}
				onClick={this.toggleColumn.bind(this, col.field)}>
				{col.title}
			</th>
		));

		const reactivateToggles = hiddenColumns.map(col => (
			<li className="list-inline-item"
				key={col.field}
				onClick={this.toggleColumn.bind(this, col.field)}>
				{col.title}
			</li>
		));

		const rows = schools.map(school => (
			<tr key={school.id}>
				{columns.map(col => <td key={col.field}>{school[col.field]}</td>)}
			</tr>
		));

		return (
			<div>
				<ul className="list-inline">
					{reactivateToggles}
				</ul>
				<table className="table">
					<thead>
						<tr>
							{header}
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

export default class SchoolDisplay extends React.Component {
	constructor(props) {
		super(props);

		this.state = {
			fetching: false,
			schools: [],
			error: null,
		};
	}

	componentDidMount() {
		this.setState({ fetching: true });
		loadSchools()
			.then(schools => this.setState({ schools, fetching: false }))
			.catch(err => this.setState({ error: err.message, fetching: false }));
	}

	render() {
		const { schools, error, fetching } = this.state;
		if (fetching) {
			return <div>Loading...</div>
		} else if (!!error) {
			return <div className="alert alert-danger" role="alert">{error}</div>
		} else {
			return <SchoolTable schools={schools} />
		}
	}
}