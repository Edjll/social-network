import {AdminPanel} from "../AdminPanel/AdminPanel";
import {BrowserRouter, Route, Switch} from "react-router-dom";
import './AdminPage.css';
import {CityAll} from "../../city/CityAll/CityAll";

export const AdminPage = () => {

    return (
        <BrowserRouter>
            <div className={"admin-page"}>
                <AdminPanel/>
                    <Switch>
                        <Route path={"/admin/city"} component={CityAll}/>
                    </Switch>
            </div>
        </BrowserRouter>
    );
}