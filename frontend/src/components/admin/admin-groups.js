import RequestService from "../../services/RequestService";
import {Table} from "../table/table";
import {TableHead} from "../table/table-head";
import {TableHeadItem} from "../table/table-head-item";
import {TableBody} from "../table/table-body";
import {TableRow} from "../table/table-row";
import {TableRowItem} from "../table/table-row-item";
import AuthService from "../../services/AuthService";
import {FormSwitch} from "../form/form-switch";
import {TableFooter} from "../table/table-footer";
import {TablePagination} from "../table/table-pagination";
import {TablePageSize} from "../table/table-page-size";
import * as React from "react";
import {Link} from "react-router-dom";
import {FormInput} from "../form/form-input";
import {LoadingAnimation} from "../loading-animation/loading-animation";

export class AdminGroups extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            groups: [],
            page: 0,
            maxPage: 0,
            size: 8,
            refreshCount: true,
            loadingGroupsCount: false,
            loadingGroups: false,
            reload: false,
            idDirection: null,
            titleDirection: null,
            addressDirection: null,
            enabledDirection: null,
            id: null,
            title: null,
            address: null,
            description: null,
            search: false,
            changedPageSize: false
        }
    }

    componentDidMount() {
        this.loadGroups();
        document.querySelector('.admin_table__content').style.minHeight = document.querySelector('.admin_table__search').clientHeight + 1 + 'px';
    }

    loadGroups() {
        if (this.state.refreshCount) {
            this.loadGroupsCount();
        }
        this.setState({loadingGroups: true, changedPageSize: false}, () => {
            RequestService.getAxios().get(RequestService.URL + '/admin/groups', {
                params: {
                    page: this.state.page,
                    size: this.state.size,
                    idDirection: this.state.idDirection,
                    titleDirection: this.state.titleDirection,
                    addressDirection: this.state.addressDirection,
                    enabledDirection: this.state.enabledDirection,
                    id: this.state.id,
                    title: this.state.title,
                    address: this.state.address,
                    description: this.state.description

                }
            }).then(response => {
                this.setState({
                    groups: response.data,
                    loadingGroups: false,
                    reload: false
                })
            });
        });
    }

    loadGroupsCount() {
        this.setState({loadingGroupsCount: true}, () => {
            RequestService.getAxios().get(RequestService.URL + '/admin/groups/count', {
                params: {
                    id: this.state.id,
                    title: this.state.title,
                    address: this.state.address,
                    description: this.state.description
                }
            }).then(response => {
                this.setState({
                    maxPage: response.data,
                    refreshCount: false,
                    loadingGroupsCount: false
                })
            });
        });
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
                break;
        }
        this.setState(direction, () => this.loadGroups());
    }

    handleClick(page) {
        this.setState({page: page}, () => this.loadGroups());
    }

    handleChangePageSize(value) {
        this.setState({size: value, changedPageSize: true, refreshCount: true});
    }

    handleChangeId(value) {
        if (this.state.id !== value)
            this.setState({id: value, page: 0, refreshCount: true, reload: true});
    }

    handleChangeTitle(value) {
        if (this.state.title !== value)
            this.setState({title: value, page: 0, refreshCount: true, reload: true});
    }

    handleChangeAddress(value) {
        if (this.state.address !== value)
            this.setState({address: value, page: 0, refreshCount: true, reload: true});
    }

    handleBlur() {
        if (this.state.reload)
            this.loadGroups();
    }

    handleChangeEnabled(id, enabled, callback) {
        RequestService.getAxios().put(RequestService.URL + `/admin/groups/${id}`, {enabled: enabled})
            .then(() => {
                if (callback) callback(enabled)
            })
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
                    <h1 className={"admin_table__header__title"}>Groups</h1>
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
                        <TableHeadItem name={'title'} order={this.state.titleDirection}
                                       handleClick={this.handleChangeDirection.bind(this)}>Title</TableHeadItem>
                        <TableHeadItem>Description</TableHeadItem>
                        <TableHeadItem name={'address'} order={this.state.addressDirection}
                                       handleClick={this.handleChangeDirection.bind(this)}>Address</TableHeadItem>
                        <TableHeadItem name={'enabled'} order={this.state.enabledDirection}
                                       handleClick={this.handleChangeDirection.bind(this)}>Enabled</TableHeadItem>
                    </TableHead>
                    <TableBody className={'admin_table__body'}>
                        <TableRow className={'admin_table__search'}>
                            <TableRowItem>
                                <FormInput clearable={true}
                                           value={this.state.id}
                                           handleChange={this.handleChangeId.bind(this)}
                                           className={"admin_table__search__input"}
                                           handleSubmit={this.handleBlur.bind(this)}
                                           handleButton={this.handleBlur.bind(this)}
                                           button={'🔍'}
                                           pattern={'[0-9]'}
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
                                           pattern={'[a-zA-Zа-яА-Я0-9_- ]'}
                                />
                            </TableRowItem>
                            <TableRowItem/>
                            <TableRowItem>
                                <FormInput clearable={true}
                                           value={this.state.address}
                                           handleChange={this.handleChangeAddress.bind(this)}
                                           className={"admin_table__search__input"}
                                           handleSubmit={this.handleBlur.bind(this)}
                                           handleButton={this.handleBlur.bind(this)}
                                           button={'🔍'}
                                           pattern={'[a-zA-Z0-9_]'}
                                />
                            </TableRowItem>
                            <TableRowItem/>
                        </TableRow>
                        <div className={'admin_table__content'}>
                            {
                                this.state.loadingGroups
                                    ?   <LoadingAnimation className={"admin_table__content__loading"}/>
                                    :   ''
                            }
                            {
                                this.state.groups.map(group => {
                                    return (
                                        <TableRow key={group.id}>
                                            <TableRowItem>{group.id}</TableRowItem>
                                            <TableRowItem>
                                                <span className={"table__body__row__item_truncate"} title={group.title}>
                                                    {group.title}
                                                </span>
                                            </TableRowItem>
                                            <TableRowItem>
                                                <span className={"table__body__row__item_truncate"} title={group.description}>
                                                    {group.description}
                                                </span>
                                            </TableRowItem>
                                            <TableRowItem>
                                                <Link to={`/group/${group.address}`}>{group.address}</Link>
                                            </TableRowItem>
                                            <TableRowItem className={"admin_table__actions"}>
                                                {
                                                    group.id !== AuthService.getId()
                                                        ? <FormSwitch enabled={group.enabled}
                                                                      handleClick={(enabled, callback) => this.handleChangeEnabled(group.id, enabled, callback)}/>
                                                        : ''
                                                }
                                            </TableRowItem>
                                        </TableRow>
                                    );
                                })
                            }
                            {
                                this.state.groups.length === 0 && !this.state.loadingGroups
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
                            loading={this.state.loadingGroupsCount}
                        />
                        <TablePageSize
                            handleChange={this.handleChangePageSize.bind(this)}
                            value={this.state.size}
                            handleButton={this.loadGroups.bind(this)}
                            changed={this.state.changedPageSize}
                        />
                    </TableFooter>
                </Table>
            </div>
        );
    }
}