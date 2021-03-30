import * as React from "react";
import '../CityForm.css';
import {Input} from "../../form/input/Input";
import RequestService from "../../../services/RequestService";
import {Button} from "../../form/button/Button";

export class DeleteCity extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            loadQueue: 1,
            city: {
                id: null,
                title: null,
                country: {
                    id: null,
                    title: null
                }
            }
        }
    }

    componentDidMount() {
        document.body.style.overflow = 'hidden';
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
        RequestService.getAxios().delete(RequestService.URL + "/city/delete", {
            params: {
                id: this.state.city.id
            }
        }).then(() => this.handleClose());
    }

    render() {

        return (
            <div className={"city-form"}>
                {
                    this.state.loadQueue === 0
                        ? <form className={"city-form__form"} onSubmit={this.handleSubmit.bind(this)}>
                            <h1 className={"city-form__title"}>Delete city</h1>
                            <Input value={this.state.city.id} label={"id"} disabled={true}/>
                            <Input value={this.state.city.title} label={"title"} disabled={true}/>
                            <Input value={this.state.city.country.title} label={"country"} disabled={true}/>
                            <Button text={"Delete"} className={"city-form__button"}/>
                        </form>
                        : ''
                }
                <div className={"city-form__button-close"} onClick={this.handleClose.bind(this)}>×</div>
            </div>
        );
    }
}