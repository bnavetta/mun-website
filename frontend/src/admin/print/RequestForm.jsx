import React from 'react';
import classnames from 'classnames';

export default class RequestForm extends React.Component {
    constructor(props, context) {
        super(props, context);

        this.state = {
            numCopies: 0,
            deliveryLocation: "",
            requester: "",
            file: null,
            error: "",
        };

        this.handleRequest = this.handleRequest.bind(this);
        this.handleFileUpload = this.handleFileUpload.bind(this);
        this.handleDeliveryLocationChange = this.handleDeliveryLocationChange.bind(this);
        this.handleNumCopiesChange = this.handleNumCopiesChange.bind(this);
    }

    handleRequest(e) {
        e.preventDefault();

        const { numCopies, deliveryLocation, requester, file } = this.state;

        if (numCopies < 1) {
            this.setState({ error: "Number of copies must be positive" });
        } else if (!deliveryLocation) {
            this.setState({ error : "Must provide a delivery location" });
        } else if (!file) {
            this.setState({ error: "Must upload a file "});
        } else {
            const data = new FormData();
            data.append("numCopies", numCopies);
            data.append("deliveryLocation", deliveryLocation);
            data.append("requester", this.props.staffEmail);
            data.append("file", file, file.name);
            this.props.onSubmit(data);
        }
    }

    handleFileUpload(e) {
        this.setState({ file: e.target.files[0] });
    }

    handleNumCopiesChange(e) {
        this.setState({ numCopies: e.target.value });
    }

    handleDeliveryLocationChange(e) {
        this.setState({ deliveryLocation: e.target.value });
    }

    render() {
        return (
            <form className="form-inline mb-2">
                { this.state.error && <div className="alert alert-danger">{this.state.error}</div> }

                <label htmlFor="numCopies" className="sr-only">Number of copies</label>
                <div className="input-group mx-2">
                    <input id="numCopies"
                           type="number"
                           value={this.state.numCopies}
                           min={1}
                           onChange={this.handleNumCopiesChange}
                           className="form-control"
                    />
                    <div className="input-group-addon">copies</div>
                </div>

                <label htmlFor="deliveryLocation" className="sr-only">Delivery location</label>
                <input id="deliveryLocation"
                       type="text"
                       placeholder="Delivery Location"
                       value={this.state.deliveryLocation}
                       onChange={this.handleDeliveryLocationChange}
                       className="form-control mx-2"
                />

                <label className="custom-file mx-2">
                    <input type="file" className="custom-file-input" onChange={this.handleFileUpload} />
                    <span className={classnames('custom-file-control', this.state.file && 'selected')}>{this.state.file && this.state.file.name}</span>
                </label>

                <button type="submit" className="btn btn-primary mx-2" onClick={this.handleRequest}>Upload</button>
            </form>
        )
    }
}