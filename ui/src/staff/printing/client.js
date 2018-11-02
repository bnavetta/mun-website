import parse from "date-fns/parse";

import { request } from "../../lib/util";

export default class PrintQueueClient {
    constructor() {
        console.log("AAAA");
        this._source = new EventSource("/staff/print-system/queue");

        this._source.addEventListener(
            "print-update",
            this._handlePrintUpdate.bind(this)
        );
        this._source.addEventListener("open", this._handleOpen.bind(this));
        this._source.addEventListener("error", this._handleError.bind(this));
        this.onUpdate = () => {};
    }

    _handleOpen() {
        console.log("Subscribed to print queue");
    }

    _handlePrintUpdate(event) {
        const printRequest = JSON.parse(event.data);
        printRequest.submissionTime = parse(printRequest.submissionTime);
        this.onUpdate(printRequest);
    }

    _handleError(error) {
        console.error("Error receiving print queue updates", error);
    }

    submitRequest(numCopies, deliveryLocation, file) {
        const formData = new FormData();
        formData.append("numCopies", numCopies.toString());
        formData.append("deliveryLocation", deliveryLocation);
        formData.append("source", file, file.name);

        return request("/staff/print-system/submit", {
            method: "POST",
            body: formData,
        });
    }

    claim(id) {
        return request(`/staff/print-system/claim/${id}`, {
            method: "PUT",
        });
    }

    complete(id) {
        return request(`/staff/print-system/complete/${id}`, {
            method: "PUT",
        });
    }

    close() {
        console.log("Unsubscribing from print queue");
        this._source.close();
    }
}
