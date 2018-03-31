import { request } from "../lib/util";

export function fetchSchool() {
    return request('/your-mun/school');
}

export function fetchAdvisor() {
    return request('/your-mun/self');
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