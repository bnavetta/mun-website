import { request } from "../lib/util";

export function fetchSchools() {
    return request("/api/school");
}

export function fetchAdvisors() {
    return request("/api/advisors");
}

export function fetchSupplementalInfo(schoolId) {
    return request(`/api/school/${schoolId}/supplemental-info`);
}

export async function authenticateAs(advisorId) {
    let url = new URL('/api/advisors/authenticate-as');
    url.searchParams.set('advisor-id', advisorId.toString());

    await request(url, {
        method: 'PUT'
    });
}