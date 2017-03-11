// @flow
import React from 'react';
import R from 'ramda';
import request from '../util/superagent';

type Advisor = { name: string, email: string };

const nameLens = R.lensProp('name');
const emailLens = R.lensProp('email');

type Props = {
    schoolId: number,
};

export default class AddAdvisorsForm extends React.Component {
    static propTypes = {
        schoolId: React.PropTypes.number.isRequired,
    };

    constructor(props: Props) {
        super(props);

        this.state = {
            advisors: [],
        };

        this.renderAdvisor = (advisor: Advisor, index: number) => (
            <tr key={index}>
                <td><input type="text" className="form-control" value={advisor.name} onChange={e => this.updateName(index, e.target.value)} /></td>
                <td><input type="email" className="form-control" value={advisor.email} onChange={e => this.updateEmail(index, e.target.value)} /></td>
                <td><button onClick={() => this.removeAdvisor(index)} className="btn btn-danger">X</button></td>
            </tr>
        );

        this.addAdvisor = () => {
            this.setState({
                advisors: [...this.state.advisors, { name: '', email: '' }],
            });
        };

        this.submitAdvisors = () => {
            request
                .post('/yourbusun/add-advisors')
                .send({
                    schoolId: this.props.schoolId,
                    advisors: this.state.advisors,
                })
                .end((err, res) => {
                    if (err) {
                        this.setState({ error: err.toString() });
                    } else if (!res.ok) {
                        this.setState({ error: res.body.error });
                    } else {
                        this.setState({ message: 'Invitations Sent!' });
                    }
                });
        };
    }

    state: {
        advisors: Array<Advisor>,
        error: ?string,
        message: ?string,
    };

    props: Props;

    addAdvisor: () => void;

    removeAdvisor(index: number) {
        this.setState({
            advisors: R.remove(index, 1, this.state.advisors),
        });
    }

    updateName(index: number, name: string) {
        this.setState({
            advisors: R.adjust(R.set(nameLens, name), index, this.state.advisors),
        });
    }

    updateEmail(index: number, email: string) {
        this.setState({
            advisors: R.adjust(R.set(emailLens, email), index, this.state.advisors),
        });
    }

    submitAdvisors: () => void;

    renderAdvisor: (advisor: Advisor, index: number) => React.Element<*>;

    render() {
        return (
            <div>
                { this.state.message && <div className="alert alert-success">{ this.state.message }</div> }
                { this.state.error && <div className="alert alert-danger">{ this.state.error }</div> }
                <table className="table">
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Email Address</th>
                            <th>Delete</th>
                        </tr>
                    </thead>
                    <tbody>
                        { this.state.advisors.map(this.renderAdvisor) }
                    </tbody>
                    <tfoot>
                        <tr>
                            <td>
                                <button className="btn btn-success" onClick={this.addAdvisor}>+</button>
                            </td>
                            <td>
                                <button className="btn btn-primary" onClick={this.submitAdvisors}>Send Invitations</button>
                            </td>
                        </tr>
                    </tfoot>
                </table>
            </div>
        );
    }
}
