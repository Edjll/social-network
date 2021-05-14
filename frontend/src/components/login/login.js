import * as React from "react";
import {Form} from "../form/form";
import {CardHeader} from "../card/card-header";
import {CardBody} from "../card/card-body";
import {FormInput} from "../form/form-input";
import {CardFooter} from "../card/card-footer";
import {FormButton} from "../form/form-button";
import AuthService from "../../services/AuthService";
import './login.css';
import {Redirect} from "react-router-dom";
import Validator from "../../services/Validator";
import validation from "../../services/validation.json";
import {FormInfo} from "../form/form-info";

export class Login extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
            errors: null,
            info: null
        }
    }

    handleChangeUsername(username) {
        this.setState({username: username, errors: {...this.state.errors, username: null}});
    }

    handleChangePassword(password) {
        this.setState({password: password, errors: {...this.state.errors, password: null}});
    }

    handleLogin() {
        if (this.validate() === 0) {
            AuthService
                .login(this.state.username, this.state.password, this.props.location.state ? this.props.location.state.prevLocation : '/')
                .catch(() => {
                    this.setState({info: "invalid username or password"});
                });
        }
    }

    validate() {
        let size = 0;
        let errors = {...this.state.errors};
        const usernameError = Validator.validate('Username', this.state.username, validation.user.username.params);
        if (usernameError) {
            errors = {...errors, username: usernameError};
            size++;
        }

        const passwordError = Validator.validate('Password', this.state.password, validation.user.password.params);
        if (passwordError) {
            errors = {...errors, password: passwordError};
            size++;
        }

        this.setState({errors: errors});
        return size;
    }

    render() {
        if (AuthService.isAuthenticated()) {
            return (<Redirect to={{pathname: '/',}}/>);
        }
        return (
            <div className={"login_form"}>
                <Form handleSubmit={this.handleLogin.bind(this)}>
                    <CardHeader>
                        <h1>Login</h1>
                    </CardHeader>
                    <CardBody>
                        {
                            this.state.info
                                ?   <FormInfo>{this.state.info}</FormInfo>
                                :   ''
                        }
                        <FormInput
                            error={this.state.errors ? this.state.errors.username : null}
                            value={this.state.username}
                            title={"username"}
                            handleChange={this.handleChangeUsername.bind(this)}
                            pattern={"[a-zA-Z0-9_@-]"}
                            clearable={true}
                        />
                        <FormInput
                            error={this.state.errors ? this.state.errors.password : null}
                            value={this.state.password}
                            title={"password"}
                            type={"password"}
                            handleChange={this.handleChangePassword.bind(this)}
                            pattern={"[a-zA-Zа-яА-Я@_0-9]"}
                            clearable={true}
                        />
                    </CardBody>
                    <CardFooter>
                        <FormButton>Login</FormButton>
                    </CardFooter>
                </Form>
            </div>
        );
    }
}