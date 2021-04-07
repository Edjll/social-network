import {AdminPanel} from "../AdminPanel/AdminPanel";
import {Route, Switch} from "react-router-dom";
import './AdminPage.css';
import {CityAll} from "../../city/CityAll/CityAll";
import {CountryAll} from "../../country/CountryAll/CountryAll";
import {UserAll} from "../../user/UserAll";
import {AdminNavigation} from "../AdminNavigation/AdminNavigation";

export const AdminPage = () => {

    return (
        <div className={"admin-page"}>
            <AdminPanel/>
            <Switch>
                <Route path={"/admin/cities"} component={CityAll}/>
                <Route path={"/admin/countries"} component={CountryAll}/>
                <Route path={"/admin/users"} component={UserAll}/>
                <Route path={"/admin"} component={AdminNavigation}/>
            </Switch>
        </div>
    );
}