import './Header.css';
import AuthService from "../../services/AuthService";

const Header = () => {

    let button;

    if (AuthService.authenticated()) {
        button = <button onClick={() => AuthService.logout({redirectUri: window.location.origin})}>Logout</button>
    } else {
        button = <button onClick={() => AuthService.login()}>Login</button>
    }

    return (
        <header>
            <p className="logo">Social-network</p>
            <nav>
                <div className="link">
                    <a href="/" style={{textDecoration: 'none'}}>Home</a>
                </div>
                <div className="link">
                    <a href="/profile" style={{textDecoration: 'none'}}>Profile</a>
                </div>
                <div className="link">{button}</div>
            </nav>
        </header>
    );
}

export default Header;