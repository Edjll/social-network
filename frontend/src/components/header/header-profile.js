import AuthService from "../../services/AuthService";
import {Link} from "react-router-dom";
import './header-profile.css';

export const HeaderProfile = () => {

    return (
        <div className={"header_profile"} tabIndex={-1}>
            <div className={"header_profile__name"}>{AuthService.getFullName()}</div>
            <div className={"header_profile__actions"}>
                <Link to={`/user/${AuthService.getUsername()}`} className={"header_profile__profile"}>profile</Link>
                <button className={"header_profile__logout"} onClick={() => AuthService.logout({redirectUri: window.location.origin})}>logout</button>
            </div>
        </div>
    );
}