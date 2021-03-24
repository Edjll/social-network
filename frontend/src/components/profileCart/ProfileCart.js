import * as React from "react";
import {Link} from "react-router-dom";
import AuthService from "../../services/AuthService";
import './ProfileCart.css';

export class ProfileCart extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            active: false
        }
    }

    handleClick() {
        this.setState({active: !this.state.active})
    }

    render() {
        return (
            <div className={"profile-cart"}>
                <button className={"profile-cart__name"} onClick={this.handleClick.bind(this)}>{AuthService.getFullName()}</button>
                {
                    this.state.active
                        ?   <div className={"profile-cart__actions"} onClick={this.handleClick.bind(this)}>
                                <Link to={`/user/${AuthService.getUsername()}`} className={"profile-cart__profile"}>profile</Link>
                                <button className={"profile-cart__logout"} onClick={() => AuthService.logout({redirectUri: window.location.origin})}>logout</button>
                            </div>
                        : ""
                }
            </div>
        );
    }
}