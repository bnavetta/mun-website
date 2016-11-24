import React from 'react';
import {Table, Column, Cell} from 'fixed-data-table';
import request from 'superagent';

import 'fixed-data-table/dist/fixed-data-table.css';

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

// https://github.com/facebook/fixed-data-table/blob/master/examples/SortExample.js

// const SortTypes = Object.freeze({
// 	ASC: 'ASC',
// 	DESC: 'DESC'
// });
//
// function reverseSortDirection(sortDir) {
// 	return sortDir === SortTypes.DESC ? SortTypes.ASC : SortTypes.DESC;
// }
//
// class SortHeaderCell extends React.Component {
// 	constructor(props) {
// 		super(props);
// 		this.handleSortChange = this.handleSortChange.bind(this);
// 	}
//
// 	render() {
// 		const {sortDir, children, ...props} = this.props;
// 		return (
// 			<Cell {...props}>
// 				<button onClick={this.handleSortChange}>
// 					{children} {sortDir ? (sortDir === SortTypes.DESC ? '↓' : '↑') : ''}
// 				</button>
// 			</Cell>
// 		);
// 	}
//
// 	handleSortChange(e) {
// 		e.preventDefault();
// 		if (!!this.props.onSortChange) {
// 			this.props.onSortChange(this.props.columnKey, this.props.sortDir ? reverseSortDirection(this.props.sortDir) : SortTypes.DESC);
// 		}
// 	}
// }

function TextDataCell({rowIndex, data, columnKey, ...props}) {
	return (
		<Cell {...props}>
			{data[rowIndex][columnKey]}
		</Cell>
	);
}

// Might not actually need sorting?
// Just column filtering?

class SchoolTable extends React.Component {
	constructor(props) {
		super(props);
	}

	render() {
		const { schools } = this.props;
		return (
			<Table
				rowHeight={50}
				rowsCount={schools.length}
				headerHeight={50}
				width={1000}
				height={500}
				{...this.props}>
				<Column
					columnKey="id"
					header={<Cell>ID</Cell>}
					cell={<TextDataCell data={schools} />}
					width={50}
				/>
				<Column
					columnKey="name"
					header={<Cell>School Name</Cell>}
					cell={<TextDataCell data={schools} />}
					width={300}
				/>
				<Column
					columnKey="status"
					header={<Cell>Registration Status</Cell>}
					cell={<TextDataCell data={schools} />}
					width={100}
					/>
				<Column
					columnKey="registrationTime"
					header={<Cell>Registration Time</Cell>}
					cell={<TextDataCell data={schools} />}
					width={150}
				/>
			</Table>
		)
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