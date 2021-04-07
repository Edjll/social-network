import {Link} from "react-router-dom";
import './AdminNavigation.css';

export const AdminNavigation = () => {

    return (
        <div className={"admin-navigation"}>
            <Link to={"/admin/cities"} className={"admin-navigation__link"}>Cities</Link>
            <Link to={"/admin/countries"} className={"admin-navigation__link"}>Countries</Link>
            <Link to={"/admin/users"} className={"admin-navigation__link"}>Users</Link>
        </div>
    );
}