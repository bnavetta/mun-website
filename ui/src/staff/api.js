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
