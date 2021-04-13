import {Link} from "react-router-dom";
import * as React from "react";
import './admin-panel.css';

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
            <div className={`admin_panel ${this.state.active ? 'admin_panel-show' : ''}`}>
                {
                    this.state.active
                        ?   <div className={"admin_panel__links"}>
                            <Link to={"/admin/cities"} className={"admin_panel__link"} onClick={this.handleClickShow.bind(this)}>Cities</Link>
                            <Link to={"/admin/countries"} className={"admin_panel__link"} onClick={this.handleClickShow.bind(this)}>Countries</Link>
                            <Link to={"/admin/users"} className={"admin_panel__link"} onClick={this.handleClickShow.bind(this)}>Users</Link>
                        </div>
                        : ''
                }
                <div className={"admin_panel__button-show"} onClick={this.handleClickShow.bind(this)}>
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