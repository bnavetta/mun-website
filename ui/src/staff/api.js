import { request } from "../lib/util";

export function fetchSchools() {
    return request("/staff/api/school");
}

export function fetchAdvisors() {
    return request("/staff/api/advisors");
}

export function fetchSupplementalInfo(schoolId) {
    return request(`/staff/api/school/${schoolId}/supplemental-info`);
}

export async function authenticateAs(advisorId) {
    let url = new URL('/staff/api/advisors/authenticate-as');
    url.searchParams.set('advisor-id', advisorId.toString());

    await request(url, {
        method: 'PUT'
    });
}