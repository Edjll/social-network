import './Header.css';
import AuthService from "../../services/AuthService";
import {Link} from "react-router-dom";
import {ProfileCart} from "../profileCart/ProfileCart";

const Header = () => {

    let element;

    if (AuthService.isAuthenticated()) {
        element = <ProfileCart/>
    } else {
        element = <button className={"header__nav__link"} onClick={() => AuthService.login()}>Login</button>
    }

    return (
        <header>
            <div className="wrapper">
                <p className="logo">Social-network</p>
                <nav className={"header__nav"}>
                    <Link to={"/"} className={"header__nav__link"}>Home</Link>
                    <Link to={"/search"} className={"header__nav__link"}>Search</Link>
                    {element}
                </nav>
            </div>
        </header>
    );
}

export default Header;