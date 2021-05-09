import RequestService from "../../services/RequestService";
import * as React from "react";
import {FormSwitch} from "../form/form-switch";
import {FormInput} from "../form/form-input";
import {TableRowItem} from "../table/table-row-item";
import {TableRow} from "../table/table-row";
import {TableBody} from "../table/table-body";
import {TableHeadItem} from "../table/table-head-item";
import {TableHead} from "../table/table-head";
import {Table} from "../table/table";
import {TableFooter} from "../table/table-footer";
import {TablePagination} from "../table/table-pagination";
import {TablePageSize} from "../table/table-page-size";
import AuthService from "../../services/AuthService";
import {Link} from "react-router-dom";

export class AdminUsers extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            users: [],
            page: 0,
            maxPage: 0,
            size: 10,
            idDirection: null,
            usernameDirection: null,
            emailDirection: null,
            cityDirection: null,
            enabledDirection: null,
            id: null,
            username: null,
            email: null,
            city: null,
            search: false
        }
    }

    componentDidMount() {
        this.loadUsers();
    }

    loadUsers() {
        RequestService.getAxios().get(RequestService.URL + '/admin/users', {
            params: {
                page: this.state.page,
                size: this.state.size,
                idDirection: this.state.idDirection,
                usernameDirection: this.state.usernameDirection,
                emailDirection: this.state.emailDirection,
                cityDirection: this.state.cityDirection,
                enabledDirection: this.state.enabledDirection,
                id: this.state.id,
                username: this.state.username,
                email: this.state.email,
                city: this.state.city
            }
        }).then(response => {
            this.setState({
                users: response.data.content,
                maxPage: response.data.totalPages,
                page: response.data.number
            })
        });
    }

    handleClick(page) {
        this.setState({page: page}, () => this.loadUsers());
    }

    handleChangePageSize(value) {
        this.setState({size: value}, () => this.loadUsers());
    }

    handleChangeDirection(directionName) {
        const direction = {};
        const field = `${directionName}Direction`;
        switch (this.state[field]) {
            case 'asc':
                direction[field] = 'desc';
                break
            case 'desc':
                direction[field] = null;
                break
            default:
                direction[field] = 'asc';
                break
        }
        this.setState(direction, () => this.loadUsers());
    }

    handleChangeId(value) {
        this.setState({id: value});
    }

    handleChangeUsername(value) {
        this.setState({username: value});
    }

    handleChangeEmail(value) {
        this.setState({email: value});
    }

    handleChangeCity(value) {
        this.setState({city: value});
    }

    handleBlur() {
        this.loadUsers();
    }

    handleActiveSearch() {
        this.setState({search: !this.state.search})
    }

    handleChangeEnabled(id, enabled, callback) {
        RequestService.getAxios().put(RequestService.URL + `/admin/users/${id}`, {enabled: enabled})
            .then(() => {
                if (callback) callback(enabled)
            })
    }

    render() {

        return (
            <div className={"admin_table"}>
                <div className={"admin_table__header"}>
                    <h1 className={"admin_table__header__title"}>Users</h1>
                    <div className={"admin_table__header__actions"}>
                        <div className={"admin_table__header__button-search"}
                             onClick={this.handleActiveSearch.bind(this)}>🔍
                        </div>
                    </div>
                </div>
                <Table>
                    <TableHead>
                        <TableHeadItem name={'id'} order={this.state.idDirection}
                                       handleClick={this.handleChangeDirection.bind(this)}>#</TableHeadItem>
                        <TableHeadItem name={'username'} order={this.state.usernameDirection}
                                       handleClick={this.handleChangeDirection.bind(this)}>Username</TableHeadItem>
                        <TableHeadItem name={'email'} order={this.state.emailDirection}
                                       handleClick={this.handleChangeDirection.bind(this)}>Email</TableHeadItem>
                        <TableHeadItem name={'city'} order={this.state.cityDirection}
                                       handleClick={this.handleChangeDirection.bind(this)}>City</TableHeadItem>
                        <TableHeadItem name={'enabled'} order={this.state.enabledDirection}
                                       handleClick={this.handleChangeDirection.bind(this)}>Enabled</TableHeadItem>
                    </TableHead>
                    <TableBody>
                        {
                            this.state.search
                                ? <TableRow>
                                    <TableRowItem>
                                        <FormInput clearable={true}
                                                   value={this.state.id}
                                                   handleBlur={this.handleBlur.bind(this)}
                                                   handleChange={this.handleChangeId.bind(this)}
                                                   className={"admin_table__search__input"}
                                                   handleSubmit={this.handleBlur.bind(this)}
                                        />
                                    </TableRowItem>
                                    <TableRowItem>
                                        <FormInput clearable={true}
                                                   value={this.state.username}
                                                   handleBlur={this.handleBlur.bind(this)}
                                                   handleChange={this.handleChangeUsername.bind(this)}
                                                   className={"admin_table__search__input"}
                                                   handleSubmit={this.handleBlur.bind(this)}
                                        />
                                    </TableRowItem>
                                    <TableRowItem>
                                        <FormInput clearable={true}
                                                   value={this.state.email}
                                                   handleBlur={this.handleBlur.bind(this)}
                                                   handleChange={this.handleChangeEmail.bind(this)}
                                                   className={"admin_table__search__input"}
                                                   handleSubmit={this.handleBlur.bind(this)}
                                        />
                                    </TableRowItem>
                                    <TableRowItem>
                                        <FormInput clearable={true}
                                                   value={this.state.city}
                                                   handleBlur={this.handleBlur.bind(this)}
                                                   handleChange={this.handleChangeCity.bind(this)}
                                                   className={"admin_table__search__input"}
                                                   handleSubmit={this.handleBlur.bind(this)}
                                        />
                                    </TableRowItem>
                                    <TableRowItem/>
                                </TableRow>
                                : ''
                        }
                        {
                            this.state.users.map(user => {
                                return (
                                    <TableRow key={user.id}>
                                        <TableRowItem>{user.id}</TableRowItem>
                                        <TableRowItem><Link to={`/user/${user.username}`}>{user.username}</Link></TableRowItem>
                                        <TableRowItem>{user.email}</TableRowItem>
                                        <TableRowItem>{user.city}</TableRowItem>
                                        <TableRowItem className={"admin_table__actions"}>
                                            {
                                                user.id !== AuthService.getId()
                                                    ? <FormSwitch enabled={user.enabled}
                                                                  handleClick={(enabled, callback) => this.handleChangeEnabled(user.id, enabled, callback)}/>
                                                    : ''
                                            }
                                        </TableRowItem>
                                    </TableRow>
                                );
                            })
                        }
                    </TableBody>
                    <TableFooter>
                        <TablePagination
                            page={this.state.page}
                            maxPage={this.state.maxPage}
                            maxButtons={5}
                            handleClick={this.handleClick.bind(this)}
                        />
                        <TablePageSize handleChange={this.handleChangePageSize.bind(this)}
                                       value={this.state.size}/>
                    </TableFooter>
                </Table>
            </div>
        );
    }
}