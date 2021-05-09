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
import {CityCreator} from "../city/city-creator";
import {CityUpdater} from "../city/city-updater";
import {CityRemover} from "../city/city-remover";
import {FormInput} from "../form/form-input";

export class AdminCities extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            cities: [],
            page: 0,
            maxPage: 0,
            size: 10,
            idDirection: null,
            titleDirection: null,
            countryDirection: null,
            id: null,
            title: null,
            country: null,
            search: false
        }
    }

    componentDidMount() {
        this.loadCities();
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (this.props.location !== prevProps.location && this.props.location.state && this.props.location.state.update) {
            this.loadCities();
        }
    }

    loadCities() {
        RequestService.getAxios().get(RequestService.ADMIN_URL + '/cities', {
            params: {
                page: this.state.page,
                size: this.state.size,
                idDirection: this.state.idDirection,
                titleDirection: this.state.titleDirection,
                countryDirection: this.state.countryDirection,
                id: this.state.id,
                title: this.state.title,
                country: this.state.country,
            }
        }).then(response => new Promise((resolve) => {
            this.setState({
                cities: response.data.content,
                maxPage: response.data.totalPages
            }, () => {
                resolve()
            });
        }));
    }

    handleClick(page) {
        this.setState({page: page}, () => this.loadCities());
    }

    handleChangePageSize(value) {
        this.setState({size: value}, () => this.loadCities());
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
        this.setState(direction, () => this.loadCities());
    }

    handleChangeId(value) {
        this.setState({id: value});
    }

    handleChangeTitle(value) {
        this.setState({title: value});
    }

    handleChangeCountry(value) {
        this.setState({country: value});
    }

    handleBlur() {
        this.loadCities();
    }

    handleActiveSearch() {
        this.setState({search: !this.state.search})
    }

    render() {
        return (
            <div className={"admin_table"}>
                <div className={"admin_table__header"}>
                    <h1 className={"admin_table__header__title"}>Cities</h1>
                    <div className={"admin_table__header__actions"}>
                        <div className={"admin_table__header__button-search"}
                             onClick={this.handleActiveSearch.bind(this)}>🔍
                        </div>
                        <Link to={"/admin/cities/create"} className={"admin_table__header__link-create"}>create</Link>
                    </div>
                </div>
                <Table>
                    <TableHead>
                        <TableHeadItem name={'id'} order={this.state.idDirection}
                                       handleClick={this.handleChangeDirection.bind(this)}>#</TableHeadItem>
                        <TableHeadItem name={'country'} order={this.state.countryDirection}
                                       handleClick={this.handleChangeDirection.bind(this)}>Country</TableHeadItem>
                        <TableHeadItem name={'title'} order={this.state.titleDirection}
                                       handleClick={this.handleChangeDirection.bind(this)}>Title</TableHeadItem>
                        <TableHeadItem>Actions</TableHeadItem>
                    </TableHead>
                    <TableBody>
                        {
                            this.state.search
                                ? <TableRow>
                                    <TableRowItem>
                                        <FormInput clearable={true}
                                                   value={this.state.id}
                                                   handleBlur={this.handleBlur.bind(this)} type={'number'}
                                                   handleChange={this.handleChangeId.bind(this)}
                                                   className={"admin_table__search__input"}
                                                   handleSubmit={this.handleBlur.bind(this)}
                                        />
                                    </TableRowItem>
                                    <TableRowItem>
                                        <FormInput clearable={true}
                                                   value={this.state.country}
                                                   handleBlur={this.handleBlur.bind(this)}
                                                   handleChange={this.handleChangeCountry.bind(this)}
                                                   className={"admin_table__search__input"}
                                                   handleSubmit={this.handleBlur.bind(this)}
                                        />
                                    </TableRowItem>
                                    <TableRowItem>
                                        <FormInput clearable={true}
                                                   value={this.state.title}
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
                            this.state.cities.map(city => {
                                return (
                                    <TableRow key={city.id}>
                                        <TableRowItem>{city.id}</TableRowItem>
                                        <TableRowItem>{city.country.title}</TableRowItem>
                                        <TableRowItem>{city.title}</TableRowItem>
                                        <TableRowItem className={"admin_table__actions"}>
                                            <Link to={`/admin/cities/${city.id}/update`}
                                                  className={"admin_table__action"}>✎</Link>
                                            <Link to={`/admin/cities/${city.id}/delete`}
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
                    <Route path={"/admin/cities/create"} component={CityCreator}/>
                    <Route path={"/admin/cities/:id/update"} component={CityUpdater}/>
                    <Route path={"/admin/cities/:id/delete"} component={CityRemover}/>
                </Switch>
            </div>

        );
    }
}