import './Header.css';
import AuthService from "../../services/AuthService";
import {Link} from "react-router-dom";

const Header = () => {

    let button;

    if (AuthService.isAuthenticated()) {
        button = <button className={"header__nav__link"} onClick={() => AuthService.logout({redirectUri: window.location.origin})}>Logout</button>
    } else {
        button = <button className={"header__nav__link"} onClick={() => AuthService.login()}>Login</button>
    }

    return (
        <header>
            <div className="wrapper">
                <p className="logo">Social-network</p>
                <nav className={"header__nav"}>
                    <Link to={"/"} className={"header__nav__link"}>Home</Link>
                    <Link to={"/user/search"} className={"header__nav__link"}>Search</Link>
                    {
                        AuthService.isAuthenticated()
                            ? <Link to={`/user/${AuthService.getUsername()}`} className={"header__nav__link"}>Profile</Link>
                            : ""
                    }
                    {button}
                </nav>
            </div>
        </header>
    );
}

export default Header;