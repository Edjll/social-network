import {Table} from "../../table/Table/Table";
import {TableHead} from "../../table/TableHead/TableHead";
import {TableBody} from "../../table/TableBody/TableBody";
import {TableRow} from "../../table/TableRow/TableRow";
import {TableRowItem} from "../../table/TableRowItem/TableRowItem";
import {TableHeadItem} from "../../table/TableHeadItem/TableHeadItem";
import {TablePagination} from "../../table/TablePagination/TablePagination";
import * as React from "react";
import axios from "axios";
import RequestService from "../../../services/RequestService";
import {TableFooter} from "../../table/TableFooter/TableFooter";
import {TablePageSize} from "../../table/TablePageSize/TablePageSize";
import './CountryAll.css';
import {CreateCountry} from "../CreateCountry/CreateCountry";
import {BrowserRouter, Link, Route, Switch} from "react-router-dom";
import {UpdateCountry} from "../UpdateCountry/UpdateCountry";
import {DeleteCountry} from "../DeleteCountry/DeleteCountry";
import {Input} from "../../form/input/Input";

export class CountryAll extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            countries: [],
            page: 0,
            maxPage: 0,
            pageSize: 10,
            idDirection: null,
            titleDirection: null,
            id: null,
            title: null,
            search: false
        }
    }

    componentDidMount() {
        this.loadCountries(this.state.page, this.state.pageSize);
    }

    loadCountries() {
        RequestService.getAxios().get(RequestService.URL + '/country/page', {
            params: {
                page: this.state.page,
                size: this.state.pageSize,
                idDirection: this.state.idDirection,
                titleDirection: this.state.titleDirection,
                id: this.state.id,
                title: this.state.title
            }
        }).then(response => {
            this.setState({
                countries: response.data.content,
                maxPage: response.data.totalPages,
                page: response.data.number
            })
        });
    }

    handleClick(page) {
        this.setState({page: page}, () => this.loadCountries());
    }

    handleChangePageSize(value) {
        this.setState({pageSize: value}, () => this.loadCountries());
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
        this.setState(direction, () => this.loadCountries());
    }

    handleChangeId(value) {
        this.setState({id: value});
    }

    handleChangeTitle(value) {
        this.setState({title: value});
    }

    handleBlur() {
        this.loadCountries();
    }

    handleActiveSearch() {
        this.setState({search: !this.state.search})
    }

    render() {

        return (
            <BrowserRouter>
                <div className={"country-all"}>
                    <div className={"country-all__header"}>
                        <h1 className={"country-all__header__title"}>Countries</h1>
                        <div className={"country-all__header__actions"}>
                            <div className={"country-all__header__button-search"} onClick={this.handleActiveSearch.bind(this)}>🔍</div>
                            <Link to={"/admin/country/create"} className={"country-all__header__link-create"}>create</Link>
                        </div>
                    </div>
                    <Table>
                        <TableHead>
                            <TableHeadItem name={'id'} order={this.state.idDirection}
                                           handleClick={this.handleChangeDirection.bind(this)}>#</TableHeadItem>
                            <TableHeadItem name={'title'} order={this.state.titleDirection}
                                           handleClick={this.handleChangeDirection.bind(this)}>Title</TableHeadItem>
                            <TableHeadItem>Actions</TableHeadItem>
                        </TableHead>
                        <TableBody>
                            {
                                this.state.search
                                    ?   <TableRow>
                                            <TableRowItem>
                                                <Input handleBlur={this.handleBlur.bind(this)} type={'number'}
                                                       handleChange={this.handleChangeId.bind(this)}
                                                       className={"country-all__search__input"}
                                                />
                                            </TableRowItem>
                                            <TableRowItem>
                                                <Input handleBlur={this.handleBlur.bind(this)}
                                                       handleChange={this.handleChangeTitle.bind(this)}
                                                       className={"country-all__search__input"}
                                                />
                                            </TableRowItem>
                                            <TableRowItem/>
                                        </TableRow>
                                    :   ''
                            }
                            {
                                this.state.countries.map(country => {
                                    return (
                                        <TableRow key={country.id}>
                                            <TableRowItem>{country.id}</TableRowItem>
                                            <TableRowItem>{country.title}</TableRowItem>
                                            <TableRowItem className={"country-all__actions"}>
                                                <Link to={`/admin/country/${country.id}/update`}
                                                      className={"country-all__action"}>✎</Link>
                                                <Link to={`/admin/country/${country.id}/delete`}
                                                      className={"country-all__action"}>🗑</Link>
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
                <Switch>
                    <Route path={"/admin/country/create"} component={CreateCountry}/>
                    <Route path={"/admin/country/:id/update"} component={UpdateCountry}/>
                    <Route path={"/admin/country/:id/delete"} component={DeleteCountry}/>
                </Switch>
            </BrowserRouter>
        );
    }
}