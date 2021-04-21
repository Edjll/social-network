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

export class AdminGroups extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            groups: [],
            page: 0,
            maxPage: 0,
            size: 10
        }
    }

    componentDidMount() {
        this.loadGroups();
    }

    loadGroups() {
        RequestService.getAxios().get(RequestService.URL + '/admin/groups', {
            params: {
                page: this.state.page,
                size: this.state.size
            }
        }).then(response => {
            this.setState({
                groups: response.data.content,
                maxPage: response.data.totalPages,
                page: response.data.number
            })
        });
    }

    handleClick(page) {
        this.setState({page: page}, () => this.loadGroups());
    }

    handleChangePageSize(value) {
        this.setState({size: value}, () => this.loadGroups());
    }

    handleBlur() {
        this.loadGroups();
    }

    handleChangeEnabled(id, enabled, callback) {
        RequestService.getAxios().put(RequestService.URL + `/admin/groups/${id}`, {enabled: enabled})
            .then(() => {
                if (callback) callback(enabled)
            })
    }

    render() {

        return (
            <div className={"admin_table"}>
                <div className={"admin_table__header"}>
                    <h1 className={"admin_table__header__title"}>Groups</h1>
                </div>
                <Table>
                    <TableHead>
                        <TableHeadItem>#</TableHeadItem>
                        <TableHeadItem>Title</TableHeadItem>
                        <TableHeadItem>Description</TableHeadItem>
                        <TableHeadItem>Address</TableHeadItem>
                        <TableHeadItem>Enabled</TableHeadItem>
                    </TableHead>
                    <TableBody>
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