const entrypoints = {
	bootstrap: ['./src/bootstrap/index.js'],
	index: ['./src/index.jsx'],
	admin: ['./src/admin/index.js'],

	registration: ['./src/registration/index.jsx'],

	committeeAdmin: ['./src/admin/committee/index.jsx'],
	schoolAdmin: ['./src/admin/school/index.jsx'],
};

export default function makeEntrypoints(base) {
	const result = {};
	for (const name of Object.keys(entrypoints)) {
		result[name] = [...base, 'babel-polyfill', ...entrypoints[name]];
	}
	return result;
}