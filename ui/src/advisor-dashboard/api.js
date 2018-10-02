import Raven from "raven-js";
import { csrfHeaders, request } from "../lib/util";

export function fetchSchool() {
    return request("/your-mun/school");
}

export function fetchAdvisor() {
    return request("/your-mun/self");
}

export function fetchSupplementalInfo() {
    return request("/your-mun/supplemental-info");
}

export function fetchDelegates() {
    return request("/your-mun/delegates");
}

export function setDelegateName(id, name) {
    return request(
        `/your-mun/delegates/set-name?id=${encodeURIComponent(
            id
        )}&name=${encodeURIComponent(name)}`,
        {
            method: "PUT",
        }
    );
}

export async function updateSupplementalInfo(info) {
    try {
        const res = await fetch("/your-mun/supplemental-info", {
            method: "POST",
            body: JSON.stringify(info),
            headers: {
                "Content-Type": "application/json",
                ...csrfHeaders,
            },
            credentials: "same-origin",
        });

        if (res.ok) {
            return { success: true, result: await res.json() };
        } else {
            return { success: false, errors: await res.json() };
        }
    } catch (e) {
        Raven.captureException(e, { extra: { supplementalInfo: info } });
        throw e;
    }
}

export function updateApplication(app) {
    return request("/your-mun/update-application", {
        method: "POST",
        body: JSON.stringify(app),
        headers: {
            "Content-Type": "application/json",
        },
    });
}

export function fetchHotels() {
    return request("/api/hotels");
}

export function changePassword(password, confirm) {
    return request("/your-mun/change-password", {
        method: "POST",
        body: JSON.stringify({ password, confirm }),
        headers: {
            "Content-Type": "application/json",
        },
    });
}
