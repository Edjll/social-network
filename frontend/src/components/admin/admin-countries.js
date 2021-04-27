import RequestService from "../../services/RequestService";
import {Link, Route, Switch} from "react-router-dom";
import {Table} from "../table/table";
import {TableHead} from "../table/table-head";
import {TableHeadItem} from "../table/table-head-item";
import {TableBody} from "../table/table-body";
import {TableRow} from "../table/table-row";
import {TableRowItem} from "../table/table-row-item";
import {TableFooter} from "../table/table-footer";
import {TablePagination} from "../table/table-pagination";
import {TablePageSize} from "../table/table-page-size";
import * as React from "react";
import {CountryCreator} from "../country/country-creator";
import {CountryUpdater} from "../country/country-updater";
import {CountryRemover} from "../country/country-remover";
import {FormInput} from "../form/form-input";

export class AdminCountries extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            countries: [],
            page: 0,
            maxPage: 0,
            size: 10,
            idDirection: null,
            titleDirection: null,
            id: null,
            title: null,
            search: false
        }
    }

    componentDidMount() {
        this.loadCountries(this.state.page, this.state.size);
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (this.props.location !== prevProps.location && this.props.location.state && this.props.location.state.update) {
            this.loadCountries();
        }
    }

    loadCountries() {
        RequestService.getAxios().get(RequestService.ADMIN_URL + '/countries', {
            params: {
                page: this.state.page,
                size: this.state.size,
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
        this.setState({size: value}, () => this.loadCountries());
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
            <div className={"admin_table"}>
                <div className={"admin_table__header"}>
                    <h1 className={"admin_table__header__title"}>Countries</h1>
                    <div className={"admin_table__header__actions"}>
                        <div className={"admin_table__header__button-search"}
                             onClick={this.handleActiveSearch.bind(this)}>🔍
                        </div>
                        <Link to={"/admin/countries/create"}
                              className={"admin_table__header__link-create"}>create</Link>
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
                                ? <TableRow>
                                    <TableRowItem>
                                        <FormInput value={this.state.id}
                                                   handleBlur={this.handleBlur.bind(this)} type={'number'}
                                                   handleChange={this.handleChangeId.bind(this)}
                                                   className={"admin_table__search__input"}
                                                   handleSubmit={this.handleBlur.bind(this)}
                                        />
                                    </TableRowItem>
                                    <TableRowItem>
                                        <FormInput value={this.state.title}
                                                   handleBlur={this.handleBlur.bind(this)}
                                                   handleChange={this.handleChangeTitle.bind(this)}
                                                   className={"admin_table__search__input"}
                                                   handleSubmit={this.handleBlur.bind(this)}
                                        />
                                    </TableRowItem>
                                    <TableRowItem/>
                                </TableRow>
                                : ''
                        }
                        {
                            this.state.countries.map(country => {
                                return (
                                    <TableRow key={country.id}>
                                        <TableRowItem>{country.id}</TableRowItem>
                                        <TableRowItem>{country.title}</TableRowItem>
                                        <TableRowItem className={"admin_table__actions"}>
                                            <Link to={`/admin/countries/${country.id}/update`}
                                                  className={"admin_table__action"}>✎</Link>
                                            <Link to={`/admin/countries/${country.id}/delete`}
                                                  className={"admin_table__action"}>🗑</Link>
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
                <Switch>
                    <Route path={"/admin/countries/create"} component={CountryCreator}/>
                    <Route path={"/admin/countries/:id/update"} component={CountryUpdater}/>
                    <Route path={"/admin/countries/:id/delete"} component={CountryRemover}/>
                </Switch>
            </div>
        );
    }
}