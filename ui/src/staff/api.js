import { request } from "../lib/util";

export function fetchSchools() {
    return request('/api/school');
}