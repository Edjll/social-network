import AuthService from "../../services/AuthService";
import './Profile.css'
import '../form/button/Button.css';
import React from "react";
import RequestService from "../../services/RequestService";
import {Link} from "react-router-dom";

export class Profile extends React.Component{

    constructor(props) {
        super(props);
        this.state = {
            id: "",
            firstName: "",
            lastName: "",
            birthday: "",
            city: ""
        }
    }

    componentDidMount() {
        RequestService.getAxios()
            .get(RequestService.URL + `/user/${this.props.match.params.username}`)
            .then(response => this.setState(response.data))
    }

    render() {
        return (
            <div className="profile">
                <div className="profile__info">
                    <div className="profile__info__header">
                        <p className="profile__info__username">{`${this.state.firstName} ${this.state.lastName}`}</p>
                        {
                            AuthService.isAuthenticated() && AuthService.getUsername() === this.props.match.params.username
                                ? <Link to={`/user/edit`} className="button">Edit info</Link>
                                : <Link to={`#`} className="button">Send message</Link>
                        }
                    </div>
                    <div className="profile__info__block">
                        <span>Birthday:</span>
                        <span>{this.state.birthday ? new Date(this.state.birthday).toLocaleDateString() : "not specified"}</span>
                    </div>
                    <div className="profile__info__block">
                        <span>City:</span>
                        <span>{this.state.city ? this.state.city : "not specified"}</span>
                    </div>
                </div>
            </div>
        );
    }
}