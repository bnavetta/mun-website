const entrypoints = {
	bootstrap: ['./src/bootstrap/index.js'],
	index: ['./src/index.jsx'],
	mypage: ['./src/mypage.js'],
	admin: ['./src/admin/index.js'],

	committeeAdmin: ['./src/admin/committee/index.jsx'],
	schoolAdmin: ['./src/admin/school/index.jsx'],
};

export default function makeEntrypoints(base) {
	const result = {};
	for (const name of Object.keys(entrypoints)) {
		// result[name] = base.concat(entrypoints[name]);
		result[name] = [...base, 'babel-polyfill', ...entrypoints[name]];
	}
	return result;
}