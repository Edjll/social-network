import './App.css';
import React from "react";
import Header from "./components/header/header";
import {BrowserRouter, Route, Switch} from "react-router-dom";
import {PrivateRoute} from "./components/security/private-route";
import {Home} from "./components/home/home";
import {Profile} from "./components/profile/profile";
import AuthService from "./services/AuthService";
import {Register} from "./components/register/register";
import {Group} from "./components/group/group";
import {UserSearch} from "./components/user/user-search";
import {UserSubscribers} from "./components/user/user-subscribers";
import {UserFriends} from "./components/user/user-friends";
import {GroupSubscribers} from "./components/group/group-subscribers";
import {Admin} from "./components/admin/admin";
import {GroupSearch} from "./components/group/group-search";
import {UserGroups} from "./components/user/user-groups";
import {Login} from "./components/login/login";
import {Messenger} from "./components/messenger/messenger";
import ProfileEditor from "./components/profile/profile-editor";

class App extends React.Component {
    render() {
        return (
            <BrowserRouter>
                <Header/>
                <main>
                    <Switch>
                        <Route path={'/login'} component={Login}/>
                        <Route path={`/register`} component={Register}/>
                        <PrivateRoute path={'/admin'} roles={[AuthService.Role.ADMIN]} component={Admin}/>
                        <PrivateRoute path={`/messenger`} component={Messenger}/>
                        <Route path={`/users`} component={UserSearch}/>
                        <PrivateRoute path={"/profile/edit"} component={ProfileEditor}/>
                        <Route path={"/user/friends"} component={UserFriends}/>
                        <Route path={"/user/subscribers"} component={UserSubscribers}/>
                        <Route path={"/user/groups"} component={UserGroups}/>
                        <Route path={"/user/:username"} component={Profile}/>
                        <Route path={"/groups"} component={GroupSearch}/>
                        <Route path={"/group/subscribers"} component={GroupSubscribers}/>
                        <Route path={"/group/:address"} component={Group}/>
                        <PrivateRoute path={"/"} component={Home}/>
                    </Switch>
                </main>
            </BrowserRouter>
        );
    }
}

export default App;
