import * as React from "react";
import '../CityForm.css';
import {Input} from "../../form/input/Input";
import {Select} from "../../form/select/Select";
import RequestService from "../../../services/RequestService";
import {Button} from "../../form/button/Button";

export class CreateCity extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            countries: [],
            loadQueue: 1,
            title: '',
            countryId: null
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
    }

    handleClose() {
        document.body.style.overflow = 'auto';
        this.props.history.goBack();
    }

    handleSubmit(e) {
        e.preventDefault();
        RequestService.getAxios().post(RequestService.URL + "/city/save", {
            title: this.state.title,
            country: {
                id: this.state.countryId
            }
        }).then(() => this.handleClose());
    }

    handleChangeTitle(value) {
        this.setState({title: value});
    }

    handleSelectCountry(value) {
        if (value === null) this.setState({countryId: null});
        else this.setState({countryId: value.key});
    }

    render() {

        return (
            <div className={"city-form"}>
                {
                    this.state.loadQueue === 0
                        ?   <form className={"city-form__form"} onSubmit={this.handleSubmit.bind(this)}>
                                <h1 className={"city-form__title"}>Create city</h1>
                                <Input handleChange={this.handleChangeTitle.bind(this)} label={"title"}/>
                                <Select options={this.state.countries} label={"country"} onChange={this.handleSelectCountry.bind(this)}/>
                                <Button text={"Save"} className={"city-form__button"}/>
                            </form>
                        :   ''
                }
                <div className={"city-form__button-close"} onClick={this.handleClose.bind(this)}>×</div>
            </div>
        );
    }
}