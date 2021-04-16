import {Route, Switch} from "react-router-dom";
import './admin.css';
import {AdminNavigation} from "./admin-navigation";
import {AdminPanel} from "./admin-panel";
import {AdminUsers} from "./admin-users";
import {AdminCities} from "./admin-cities";
import {AdminCountries} from "./admin-countries";
import {AdminGroups} from "./admin-groups";

export const Admin = () => {

    return (
        <div className={"admin"}>
            <AdminPanel/>
            <Switch>
                <Route path={"/admin/cities"} component={AdminCities}/>
                <Route path={"/admin/countries"} component={AdminCountries}/>
                <Route path={"/admin/users"} component={AdminUsers}/>
                <Route path={"/admin/groups"} component={AdminGroups}/>
                <Route path={"/admin"} component={AdminNavigation}/>
            </Switch>
        </div>
    );
}