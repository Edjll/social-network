import AuthService from "../../services/AuthService";
import './Profile.css'

export const Profile = () => {
    return (
        <p className="username">Username: { AuthService.getUsername() }</p>
    );
}