import './header.css';
import AuthService from "../../services/AuthService";
import {Link} from "react-router-dom";
import {HeaderProfile} from "./header-profile";

const Header = () => {

    let element;

    if (AuthService.isAuthenticated()) {
        element = <HeaderProfile/>
    } else {
        element = <div>
            <button className={"header__nav__link"} onClick={() => AuthService.login()}>Login</button>
            <span className={"header__nav__link"}>|</span>
            <Link className={"header__nav__link"} to={"/register"}>Register</Link>
        </div>
    }

    return (
        <header>
            <div className="wrapper">
                <p className="logo">Social-network</p>
                <nav className={"header__nav"}>
                    <Link to={"/"} className={"header__nav__link"}>Home</Link>
                    {
                        AuthService.isAuthenticated() && AuthService.hasRole([AuthService.Role.ADMIN])
                            ? <Link className={"header__nav__link"} to={"/admin"}>Admin</Link>
                            : ''
                    }
                    <Link to={"/users"} className={"header__nav__link"}>Users</Link>
                    <Link to={"/groups"} className={"header__nav__link"}>Groups</Link>
                    {element}
                </nav>
            </div>
        </header>
    );
}

export default Header;