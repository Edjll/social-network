import * as React from "react";
import {Select} from "../form/select/Select";
import {Button} from "../form/button/Button";
import RequestService from "../../services/RequestService";
import {Input} from "../form/input/Input";
import AuthService from "../../services/AuthService";
import './ProfileEdit.css';
import {Spinner} from "../spinner/Spinner";

export default class ProfileEdit extends React.Component {

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
            RequestService.getAxios().get(RequestService.URL + `/city/all`, {params: {countryId: value}})
                .then(response =>
                    this.setState({
                        selectedCountry: true, cities: response.data.map(option => {
                            return {key: option.id, text: option.title}
                        })
                    })
                );
            this.setState({
                country: {
                    id: value.key,
                    title: value.text
                }
            })
        } else if (this.state.selectedCountry === true) {
            this.setState({
                selectedCountry: false,
                city: {
                    id: null,
                    title: null,
                },
                country: {
                    id: null,
                    title: null
                }
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
        RequestService.getAxios().get(RequestService.URL + "/country/all")
            .then(response => {
                this.setState({
                    countries: response.data.map(option => {
                        return {key: option.id, text: option.title}
                    }).sort((a, b) => a.text > b.text),
                    loadQueue: this.state.loadQueue - 1
                });
            });

        RequestService.getAxios()
            .get(RequestService.URL + `/user/${AuthService.getUsername()}/detail`)
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

    handleSubmit(e) {
        e.preventDefault();
        RequestService.getAxios().post(RequestService.URL + `/user/update`, {
            cityId: this.state.city.id,
            birthday: this.state.birthday
        }).then(response => this.props.history.push(`/user/${AuthService.getUsername()}`));
    }

    handleBirthday(value) {
        this.setState({birthday: value})
    }

    render() {

        return (
            <div className={"profile-edit"}>
                <div className={"profile-edit__title"}>
                    <h1>Editing a profile</h1>
                </div>
                {
                    this.state.loadQueue === 0
                        ? <form className={"profile-edit__form"} onSubmit={this.handleSubmit.bind(this)}>
                            <Input type={"date"} label={"Birthday"} name={"birthday"}
                                   handleChange={this.handleBirthday.bind(this)} value={this.state.birthday}/>
                            <Select name="country" label="Country" options={this.state.countries}
                                    onChange={this.handleChangeCountry.bind(this)}
                                    value={{key: this.state.country.id, text: this.state.country.title}}/>
                            {this.state.selectedCountry ? <Select name="city" label="City" options={this.state.cities}
                                                                  onChange={this.handleChangeCity.bind(this)} value={{
                                key: this.state.city.id,
                                text: this.state.city.title
                            }}/> : ""}
                            <Button text="Save"/>
                        </form>
                        : <Spinner/>
                }
            </div>
        );
    }
}