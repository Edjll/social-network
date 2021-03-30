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
import './CityAll.css';
import {CreateCity} from "../CreateCity/CreateCity";
import {BrowserRouter, Link, Route, Switch} from "react-router-dom";
import {UpdateCity} from "../UpdateCity/UpdateCity";
import {DeleteCity} from "../DeleteCity/DeleteCity";
import {Input} from "../../form/input/Input";

export class CityAll extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            cities: [],
            page: 0,
            maxPage: 0,
            pageSize: 15,
            idDirection: null,
            titleDirection: null,
            countryDirection: null,
            id: null,
            title: null,
            country: null
        }
    }

    componentDidMount() {
        this.loadCities(this.state.page, this.state.pageSize);
    }

    loadCities() {
        axios.get(RequestService.URL + '/city/page', {
            params: {
                page: this.state.page,
                size: this.state.pageSize,
                idDirection: this.state.idDirection,
                titleDirection: this.state.titleDirection,
                countryDirection: this.state.countryDirection,
                id: this.state.id,
                title: this.state.title,
                country: this.state.country,
                search: false
            }
        }).then(response => {
            this.setState({
                cities: response.data.content,
                maxPage: response.data.totalPages,
                page: response.data.number
            })
        });
    }

    handleClick(page) {
        this.setState({page: page}, () => this.loadCities());
    }

    handleChangePageSize(value) {
        this.setState({pageSize: value}, () => this.loadCities());
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
            <BrowserRouter>
                <div className={"city-all"}>
                    <div className={"city-all__header"}>
                        <h1 className={"city-all__header__title"}>Cities</h1>
                        <div className={"city-all__header__actions"}>
                            <div className={"city-all__header__button-search"} onClick={this.handleActiveSearch.bind(this)}>🔍</div>
                            <Link to={"/admin/city/create"} className={"city-all__header__link-create"}>create</Link>
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
                                    ?   <TableRow>
                                            <TableRowItem>
                                                <Input handleBlur={this.handleBlur.bind(this)} type={'number'}
                                                       handleChange={this.handleChangeId.bind(this)}
                                                       className={"city-all__search__input"}
                                                />
                                            </TableRowItem>
                                            <TableRowItem>
                                                <Input handleBlur={this.handleBlur.bind(this)}
                                                       handleChange={this.handleChangeCountry.bind(this)}
                                                       className={"city-all__search__input"}
                                                />
                                            </TableRowItem>
                                            <TableRowItem>
                                                <Input handleBlur={this.handleBlur.bind(this)}
                                                       handleChange={this.handleChangeTitle.bind(this)}
                                                       className={"city-all__search__input"}
                                                />
                                            </TableRowItem>
                                            <TableRowItem/>
                                        </TableRow>
                                    :   ''
                            }
                            {
                                this.state.cities.map(city => {
                                    return (
                                        <TableRow key={city.id}>
                                            <TableRowItem>{city.id}</TableRowItem>
                                            <TableRowItem>{city.country.title}</TableRowItem>
                                            <TableRowItem>{city.title}</TableRowItem>
                                            <TableRowItem className={"city-all__actions"}>
                                                <Link to={`/admin/city/${city.id}/update`}
                                                      className={"city-all__action"}>✎</Link>
                                                <Link to={`/admin/city/${city.id}/delete`}
                                                      className={"city-all__action"}>🗑</Link>
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
                    <Route path={"/admin/city/create"} component={CreateCity}/>
                    <Route path={"/admin/city/:id/update"} component={UpdateCity}/>
                    <Route path={"/admin/city/:id/delete"} component={DeleteCity}/>
                </Switch>
            </BrowserRouter>
        );
    }
}