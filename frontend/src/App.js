import './App.css';
import React from "react";
import Header from "./components/header/Header";
import {PrivateRoute} from "./components/security/PrivateRoute";
import {BrowserRouter, Route, Switch} from "react-router-dom";
import {Profile} from "./components/profile/Profile";
import {Home} from "./components/home/Home";

class App extends React.Component {

    render() {

        return (
            <BrowserRouter>
                <Header/>
                <main>
                    <Switch>
                        <PrivateRoute roles={['USER', 'ADMIN']} path="/profile" component={Profile}/>
                        <Route path="/" component={Home}/>
                    </Switch>
                </main>
            </BrowserRouter>
        );
    }

}

export default App;
