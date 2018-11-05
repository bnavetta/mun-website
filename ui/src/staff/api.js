import * as R from "ramda";
import { request } from "../lib/util";

export function fetchUserInfo() {
    return request("/staff/user-info");
}

export function fetchSchools() {
    return request("/api/school");
}

export function fetchAdvisors() {
    return request("/api/advisors");
}

export function fetchSupplementalInfo(schoolId) {
    return request(`/api/school/${schoolId}/supplemental-info`);
}

export function fetchDelegates(schoolId) {
    return request(`/api/school/${schoolId}/delegates`);
}

export async function fetchCommittees() {
    const committees = await request(`/api/committee`);
    return R.unnest(R.values(committees));
}

export function fetchPositions(committeeId) {
    return request(`/api/committee/${committeeId}/positions`);
}

export function updateAttendance(committeeId, positionId, session, present) {
    return request(`/api/committee/${committeeId}/attendance`, {
        method: "POST",
        body: JSON.stringify({ session, present, positionId }),
        headers: {
            "Content-Type": "application/json",
        },
    });
}

export async function authenticateAs(advisorId) {
    let url = new URL("/api/advisors/authenticate-as", window.location.href);
    url.searchParams.set("advisorId", advisorId.toString());

    await request(url, {
        method: "PUT",
    });
}