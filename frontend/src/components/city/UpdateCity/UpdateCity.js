import * as React from "react";
import '../CityForm.css';
import {Input} from "../../form/input/Input";
import {Select} from "../../form/select/Select";
import RequestService from "../../../services/RequestService";
import {Button} from "../../form/button/Button";

export class UpdateCity extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            countries: [],
            loadQueue: 2,
            title: '',
            countryId: null,
            city: {
                id: null,
                title: null,
                country: {
                    id: null,
                    title: null
                }
            },
            errors: {
                title: null,
                country: null
            }
        }
    }

    componentDidMount() {
        document.body.style.overflow = 'hidden';
        RequestService.getAxios().get(RequestService.URL + "/country/all")
            .then(response => {
                this.setState({
                    countries: response.data.map(option => {
                        return {key: option.id, text: option.title}
                    }).sort((a, b) => a.text > b.text),
                    loadQueue: this.state.loadQueue - 1
                });
            });
        RequestService.getAxios().get(RequestService.URL + "/city/" + this.props.match.params.id)
            .then(response => {
                this.setState({
                    city: response.data,
                    countryId: response.data.country.id,
                    title: response.data.title,
                    loadQueue: this.state.loadQueue - 1
                });
            });
    }

    handleClose() {
        document.body.style.overflow = 'auto';
        this.props.history.goBack();
    }

    handleSubmit(e) {
        e.preventDefault();
        RequestService.getAxios().put(RequestService.URL + "/city/update", {
            id: this.state.city.id,
            title: this.state.title,
            countryId: this.state.countryId
        })
            .then(() => this.handleClose())
            .catch(error => this.setState({
                errors: {
                    title: error.response.data.errors.title,
                    country: error.response.data.errors.countryId
                }
            }));
    }

    handleChangeTitle(value) {
        this.setState({title: value, errors: {...this.state.errors, title: null}});
    }

    handleSelectCountry(value) {
        if (value === null) this.setState({countryId: null, errors: {...this.state.errors, country: null}});
        else this.setState({countryId: value.key, errors: {...this.state.errors, country: null}});
    }

    render() {

        return (
            <div className={"city-form"}>
                {
                    this.state.loadQueue === 0
                        ? <form className={"city-form__form"} onSubmit={this.handleSubmit.bind(this)}>
                                <h1 className={"city-form__title"}>Update city</h1>
                                <Input value={this.state.city.id} label={"id"} disabled={true}/>
                                <Input value={this.state.city.title} handleChange={this.handleChangeTitle.bind(this)}
                                       label={"title"} error={this.state.errors.title}/>
                                <Select value={{key: this.state.city.country.id, text: this.state.city.country.title}}
                                        options={this.state.countries} label={"country"}
                                        onChange={this.handleSelectCountry.bind(this)} error={this.state.errors.country}/>
                                <Button text={"Update"} className={"city-form__button"}/>
                            </form>
                        : ''
                }
                <div className={"city-form__button-close"} onClick={this.handleClose.bind(this)}>×</div>
            </div>
        );
    }
}