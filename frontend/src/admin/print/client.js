import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import request from "../../util/superagent";

export function subscribeToQueue(listener) {
    const socket = new SockJS('/admin/print/sock');
    const stomp = Stomp.over(socket);
    stomp.debug = null;

    stomp.connect({}, () => {
        console.log('Connected to print queue socket');
        stomp.subscribe('/topic/print/queue', message => {
            listener(JSON.parse(message.body).queueContents);
        })
    });

    return () => stomp.disconnect();
}

export async function loadQueue() {
    const res = await request.get('/admin/print/request').accept('json');
    if (!res.ok) {
        throw new Error(res.body);
    } else {
        return res.body;
    }
}

export async function submitRequest(requestData) {
    const res = await request.post('/admin/print/request').send(requestData).accept('json');
    if (!res.ok) {
        throw new Error(res.body);
    } else {
        return res.body;
    }
}

export async function claimRequest(id) {
    const res = await request.post(`/admin/print/request/${id}/claim`).accept('json');
    if (!res.ok) {
        throw new Error(res.body);
    } else {
        return res.body;
    }
}

export async function completeRequest(id) {
    const res = await request.post(`/admin/print/request/${id}/complete`).accept('json');
    if (!res.ok) {
        throw new Error(res.body);
    } else {
        return res.body;
    }
}