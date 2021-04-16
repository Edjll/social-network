import {Link} from "react-router-dom";
import './admin-navigation.css';

export const AdminNavigation = () => {

    return (
        <div className={"admin_navigation"}>
            <Link to={"/admin/cities"} className={"admin_navigation__link"}>Cities</Link>
            <Link to={"/admin/countries"} className={"admin_navigation__link"}>Countries</Link>
            <Link to={"/admin/users"} className={"admin_navigation__link"}>Users</Link>
            <Link to={"/admin/groups"} className={"admin_navigation__link"}>Groups</Link>
        </div>
    );
}