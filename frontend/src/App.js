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

class App extends React.Component {

    render() {
        return (
            <BrowserRouter>
                <Header/>
                <main>
                    <Switch>
                        <PrivateRoute path={`/user/:username/message`} component={Dialog}/>
                        <Route path={`/search`} component={Search}/>
                        <PrivateRoute path={`/user/edit`} component={ProfileEdit}/>
                        <Route path={"/user/:username"} component={Profile}/>
                        <Route path="/" component={Home}/>
                    </Switch>
                </main>
            </BrowserRouter>
        );
    }

}

export default App;
