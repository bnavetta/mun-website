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
    const committeeObj = await request(`/api/committee`);

    return [
        ...committeeObj.general,
        ...committeeObj.specialized,
        ...committeeObj.crisis,
        ...committeeObj.jointCrises,
        ...committeeObj.crisis2020sunday,
        ...R.unnest(R.values(committeeObj.jointCrisisRooms)),
    ];
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

export function fetchAwards(committeeId) {
    return request(`/api/committee/${committeeId}/awards`);
}

export function grantAward(committeeId, awardId, positionId) {
    return request(`/api/committee/${committeeId}/awards`, {
        method: "POST",
        body: JSON.stringify({ id: awardId, positionId }),
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