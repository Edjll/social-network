import RequestService from "../../services/RequestService";
import AuthService from "../../services/AuthService";
import * as React from "react";
import {Form} from "../form/form";
import {CardHeader} from "../card/card-header";
import {FormClose} from "../form/form-close";
import {CardBody} from "../card/card-body";
import {FormInput} from "../form/form-input";
import {CardFooter} from "../card/card-footer";
import {FormButton} from "../form/form-button";
import {FormSelect} from "../form/form-select";

export default class ProfileEditor extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            selectedCountry: false,
            countries: [],
            cities: [],
            city: {
                id: null,
                title: null
            },
            country: {
                id: null,
                title: null
            },
            birthday: null,
            loadQueue: 2
        }
    }

    handleChangeCountry(value) {
        if (value != null) {
            RequestService.getAxios().get(RequestService.URL + `/cities`, {params: {countryId: value.key}})
                .then(response =>
                    this.setState({
                        selectedCountry: true, cities: response.data.map(option => {
                            return {key: option.id, text: option.title}
                        })
                    })
                );
            this.setState({
                country: {id: value.key, title: value.text}
            })
        } else if (this.state.selectedCountry === true) {
            this.setState({
                selectedCountry: false,
                city: {id: null, title: null},
                country: {id: null, title: null}
            });
        }
    }

    handleChangeCity(value) {
        if (value !== null) {
            this.setState({
                city: {
                    id: value.key,
                    title: value.text,
                }
            });
        } else {
            this.setState({
                city: {
                    id: null,
                    title: null,
                }
            });
        }
    }

    componentDidMount() {
        RequestService.getAxios().get(RequestService.URL + "/countries")
            .then(response => {
                this.setState({
                    countries: response.data.map(option => {
                        return {key: option.id, text: option.title}
                    }).sort((a, b) => a.text > b.text),
                    loadQueue: this.state.loadQueue - 1
                });
            });

        RequestService.getAxios()
            .get(RequestService.URL + `/users/${AuthService.getId()}/details`)
            .then(response => {
                this.setState({
                    city: {
                        id: response.data.city.id,
                        title: response.data.city.title
                    },
                    country: {
                        id: response.data.city.country.id,
                        title: response.data.city.country.title
                    },
                    birthday: response.data.birthday
                });
                if (this.state.country.id !== null) {
                    this.handleChangeCountry({key: this.state.country.id, text: this.state.country.title});
                }
                this.setState({loadQueue: this.state.loadQueue - 1});
            });
    }

    handleSubmit() {
        RequestService.getAxios().post(RequestService.URL + `/users/${AuthService.getId()}`, {
            cityId: this.state.city.id,
            birthday: this.state.birthday
        }).then(() => this.props.history.replace(`/user/${AuthService.getUsername()}`));
    }

    handleBirthday(value) {
        this.setState({birthday: value})
    }

    handleClose() {
        this.props.history.goBack();
    }

    render() {

        return (
            <div className={"attention_center"}>
                <Form handleSubmit={this.handleSubmit.bind(this)}>
                    <CardHeader>
                        <h1>Editing profile</h1>
                        <FormClose handleClick={this.handleClose.bind(this)}/>
                    </CardHeader>
                    <CardBody>
                        <FormInput value={this.state.birthday} type={"date"} title={"birthday"}
                                   handleChange={this.handleBirthday.bind(this)}/>
                        {
                            this.state.loadQueue === 0
                                ? <FormSelect
                                    value={{key: this.state.country.id, text: this.state.country.title}}
                                    options={this.state.countries}
                                    title={"country"}
                                    handleChange={this.handleChangeCountry.bind(this)}/>
                                : ''
                        }
                        {
                            this.state.country.id && this.state.cities.length > 0
                                ? <FormSelect
                                    value={{key: this.state.city.id, text: this.state.city.title}}
                                    title={"city"}
                                    options={this.state.cities}
                                    handleChange={this.handleChangeCity.bind(this)}
                                />
                                : ''
                        }
                    </CardBody>
                    <CardFooter>
                        <FormButton>Save</FormButton>
                    </CardFooter>
                </Form>
            </div>
        );
    }
}