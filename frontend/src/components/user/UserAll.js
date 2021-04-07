import * as React from "react";
import RequestService from "../../services/RequestService";
import {Link} from "react-router-dom";
import {Table} from "../table/Table/Table";
import {TableHead} from "../table/TableHead/TableHead";
import {TableHeadItem} from "../table/TableHeadItem/TableHeadItem";
import {TableBody} from "../table/TableBody/TableBody";
import {TableRow} from "../table/TableRow/TableRow";
import {TableRowItem} from "../table/TableRowItem/TableRowItem";
import {Input} from "../form/input/Input";
import {TableFooter} from "../table/TableFooter/TableFooter";
import {TablePagination} from "../table/TablePagination/TablePagination";
import {TablePageSize} from "../table/TablePageSize/TablePageSize";
import {Switch} from "../form/switch/Switch";
import AuthService from "../../services/AuthService";

export class UserAll extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            users: [],
            page: 0,
            maxPage: 0,
            pageSize: 10,
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
        RequestService.getAxios().get(RequestService.URL + '/user/page', {
            params: {
                page: this.state.page,
                size: this.state.pageSize,
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
        this.setState({pageSize: value}, () => this.loadUsers());
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
        RequestService.getAxios().put(RequestService.URL + `/user/enabled`, { id: id, enabled: enabled })
            .then(response => { if (callback) callback(enabled) })
    }

    render() {

        return (
            <div className={"city-all"}>
                <div className={"city-all__header"}>
                    <h1 className={"city-all__header__title"}>Users</h1>
                    <div className={"city-all__header__actions"}>
                        <div className={"city-all__header__button-search"}
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
                                        <Input handleBlur={this.handleBlur.bind(this)}
                                               handleChange={this.handleChangeId.bind(this)}
                                               className={"city-all__search__input"}
                                        />
                                    </TableRowItem>
                                    <TableRowItem>
                                        <Input handleBlur={this.handleBlur.bind(this)}
                                               handleChange={this.handleChangeUsername.bind(this)}
                                               className={"city-all__search__input"}
                                        />
                                    </TableRowItem>
                                    <TableRowItem>
                                        <Input handleBlur={this.handleBlur.bind(this)}
                                               handleChange={this.handleChangeEmail.bind(this)}
                                               className={"city-all__search__input"}
                                        />
                                    </TableRowItem>
                                    <TableRowItem>
                                        <Input handleBlur={this.handleBlur.bind(this)}
                                               handleChange={this.handleChangeCity.bind(this)}
                                               className={"city-all__search__input"}
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
                                        <TableRowItem>{user.username}</TableRowItem>
                                        <TableRowItem>{user.email}</TableRowItem>
                                        <TableRowItem>{user.city}</TableRowItem>
                                        <TableRowItem className={"city-all__actions"}>
                                            {
                                                user.id !== AuthService.getId()
                                                    ?   <Switch enabled={user.enabled} handleClick={(enabled, callback) => this.handleChangeEnabled(user.id, enabled, callback)}/>
                                                    :   ''
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
                                       value={this.state.pageSize}/>
                    </TableFooter>
                </Table>
            </div>
        );
    }
}