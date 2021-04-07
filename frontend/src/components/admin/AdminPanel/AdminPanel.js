import './AdminPanel.css';
import * as React from "react";
import {Link} from "react-router-dom";

export class AdminPanel extends React.Component {

    constructor(props) {
        super(props);

        let active = localStorage.getItem("adminPanel");

        if (active === null) {
            active = true;
            localStorage.setItem("adminPanel", active);
        } else {
            active = active === 'true'
        }

        this.state = {
            active: active
        }

    }

    handleClickShow() {
        this.setState({active: !this.state.active}, () => localStorage.setItem("adminPanel", this.state.active));
    }

    render() {
        return (
            <div className={`admin-panel ${this.state.active ? 'admin-panel-show' : ''}`}>
                {
                    this.state.active
                        ?   <div className={"admin-panel__links"}>
                                <Link to={"/admin/city"} className={"admin-panel__link"} onClick={this.handleClickShow.bind(this)}>Cities</Link>
                                <Link to={"/admin/countries"} className={"admin-panel__link"} onClick={this.handleClickShow.bind(this)}>Countries</Link>
                                <Link to={"/admin/users"} className={"admin-panel__link"} onClick={this.handleClickShow.bind(this)}>Users</Link>
                            </div>
                        : ''
                }
                <div className={"admin-panel__button-show"} onClick={this.handleClickShow.bind(this)}>
                    {
                        this.state.active
                            ?   '<'
                            :   '>'
                    }
                </div>
            </div>
        );
    }
}