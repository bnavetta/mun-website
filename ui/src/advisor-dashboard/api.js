import {csrfHeaders, request} from "../lib/util";

export function fetchSchool() {
    return request('/your-mun/school');
}

export function fetchAdvisor() {
    return request('/your-mun/self');
}

export function fetchSupplementalInfo() {
    return request('/your-mun/supplemental-info')
}

export async function updateSupplementalInfo(info) {
    const res = await fetch('/your-mun/supplemental-info', {
        method: 'POST',
        body: JSON.stringify(info),
        headers: {
            'Content-Type': 'application/json',
            ...csrfHeaders
        },
        credentials: 'same-origin'
    });

    if (res.ok) {
        return { success: true, result: await res.json() };
    } else {
        return { success: false, errors: await res.json() };
    }
}

export function updateApplication(app) {
    return request('/your-mun/update-application', {
        method: 'POST',
        body: JSON.stringify(app),
        headers: {
            'Content-Type': 'application/json'
        }
    });
}

export function fetchHotels() {
    return request('/api/hotels');
}