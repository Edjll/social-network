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
import {HiddenInfo} from "../hidden-info/hidden-info";
import {LoadingAnimation} from "../loading-animation/loading-animation";

export class AdminCountries extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            countries: [],
            page: 0,
            maxPage: 0,
            size: 8,
            idDirection: null,
            titleDirection: null,
            id: null,
            title: null,
            search: false,
            loadingCountries: false,
            reload: false
        }
    }

    componentDidMount() {
        this.loadCountries(this.state.page, this.state.size);
        document.querySelector('.admin_table__content').style.minHeight = document.querySelector('.admin_table__search').clientHeight + 1 + 'px';
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (this.props.location !== prevProps.location && this.props.location.state && this.props.location.state.update) {
            this.loadCountries();
        }
    }

    loadCountries() {
        this.setState({loadingCountries: true}, () => {
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
                    loadingCountries: false,
                    reload: false
                })
            });
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
                break;
            default:
                direction[field] = 'asc';
                break
        }
        this.setState(direction, () => this.loadCountries());
    }

    handleChangeId(value) {
        if (this.state.id !== value)
            this.setState({id: value, reload: true, page: 0});
    }

    handleChangeTitle(value) {
        if (this.state.title !== value)
            this.setState({title: value, reload: true, page: 0});
    }

    handleBlur() {
        if (this.state.reload)
            this.loadCountries();
    }

    handleActiveSearch() {
        this.setState({search: !this.state.search}, () => {
            if (this.state.search) {
                document.querySelector('.admin_table__content').style.marginTop = document.querySelector('.admin_table__search').clientHeight + 1 + 'px';
            } else {
                document.querySelector('.admin_table__content').style.marginTop = '0';
            }
        })
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
                    <TableBody className={'admin_table__body'}>
                        <TableRow className={'admin_table__search'}>
                            <TableRowItem>
                                <FormInput clearable={true}
                                           value={this.state.id}
                                           type={'number'}
                                           handleChange={this.handleChangeId.bind(this)}
                                           className={"admin_table__search__input"}
                                           handleSubmit={this.handleBlur.bind(this)}
                                           handleButton={this.handleBlur.bind(this)}
                                           button={'🔍'}
                                />
                            </TableRowItem>
                            <TableRowItem>
                                <FormInput clearable={true}
                                           value={this.state.title}
                                           handleChange={this.handleChangeTitle.bind(this)}
                                           className={"admin_table__search__input"}
                                           handleSubmit={this.handleBlur.bind(this)}
                                           handleButton={this.handleBlur.bind(this)}
                                           button={'🔍'}
                                />
                            </TableRowItem>
                            <TableRowItem/>
                        </TableRow>
                        <div className={'admin_table__content'}>
                            {
                                this.state.loadingCountries
                                    ?   <LoadingAnimation className={"admin_table__content__loading"}/>
                                    :   ''
                            }
                            {
                                this.state.countries.map(country => {
                                    return (
                                        <TableRow key={country.id}>
                                            <TableRowItem>{country.id}</TableRowItem>
                                            <TableRowItem>{country.title}</TableRowItem>
                                            <TableRowItem className={"admin_table__actions"}>
                                                <HiddenInfo text={"✎"}
                                                            hidden={"Change country"}
                                                            link={`/admin/countries/${country.id}/update`}/>
                                                <HiddenInfo text={"🗑"}
                                                            hidden={"Remove country"}
                                                            link={`/admin/countries/${country.id}/delete`}/>
                                            </TableRowItem>
                                        </TableRow>
                                    );
                                })
                            }
                            {
                                this.state.countries.length === 0 && !this.state.loadingCountries
                                    ?   <div className={'admin_table__content__empty'}>Not found</div>
                                    :   ''
                            }
                        </div>
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