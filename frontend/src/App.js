import './App.css';
import React from "react";
import Header from "./components/header/Header";
import {BrowserRouter, Route, Switch} from "react-router-dom";
import {Search} from "./components/search/Search";
import {PrivateRoute} from "./components/security/PrivateRoute";
import {Home} from "./components/home/Home";
import {Profile} from "./components/profile/Profile";
import ProfileEdit from "./components/profileEdit/ProfileEdit";
import {Dialog} from "./components/dialog/Dialog";
import {AdminPage} from "./components/admin/AdminPage/AdminPage";
import AuthService from "./services/AuthService";
import {Register} from "./components/register/Register";

class App extends React.Component {

    render() {
        return (
            <BrowserRouter>
                <Header/>
                <main>
                    <Switch>
                        <Route path={`/register`} component={Register}/>
                        <PrivateRoute path={'/admin'} roles={[AuthService.Role.ADMIN]} component={AdminPage}/>
                        <PrivateRoute path={`/user/:username/message`} component={Dialog}/>
                        <Route path={`/search`} component={Search}/>
                        <PrivateRoute path={`/user/edit`} component={ProfileEdit}/>
                        <Route path={"/user/:username"} component={Profile}/>
                        <Route path={"/"} component={Home}/>
                    </Switch>
                </main>
            </BrowserRouter>
        );
    }

}

export default App;
