import './App.css';
import React from "react";
import Header from "./components/header/header";
import {BrowserRouter, Route, Switch} from "react-router-dom";
import {PrivateRoute} from "./components/security/PrivateRoute";
import {Home} from "./components/home/home";
import {Profile} from "./components/profile/profile";
import {Dialog} from "./components/dialog/dialog";
import AuthService from "./services/AuthService";
import {Register} from "./components/register/register";
import {Group} from "./components/group/group";
import {GroupCreator} from "./components/group/group-creator";
import {UserSearch} from "./components/user/user-search";
import {UserSubscribers} from "./components/user/user-subscribers";
import {UserFriends} from "./components/user/user-friends";
import {GroupSubscribers} from "./components/group/group-subscribers";
import {Admin} from "./components/admin/admin";
import ProfileEditor from "./components/profile/profile-editor";
import {GroupSearch} from "./components/group/group-search";
import {UserGroups} from "./components/user/user-groups";
import {Login} from "./components/login/login";

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
                        <PrivateRoute path={`/user/:username/message`} component={Dialog}/>
                        <Route path={`/users`} component={UserSearch}/>
                        <Route path={"/profile/edit"} component={ProfileEditor}/>
                        <Route path={"/user/friends"} component={UserFriends}/>
                        <Route path={"/user/subscribers"} component={UserSubscribers}/>
                        <Route path={"/user/groups"} component={UserGroups}/>
                        <Route path={"/user/:username"} component={Profile}/>
                        <PrivateRoute path={"/group/create"} component={GroupCreator}/>
                        <Route path={"/groups"} component={GroupSearch}/>
                        <Route path={"/group/subscribers"} component={GroupSubscribers}/>
                        <Route path={"/group/:address"} component={Group}/>
                        <Route path={"/"} component={Home}/>
                    </Switch>
                </main>
            </BrowserRouter>
        );
    }
}

export default App;
