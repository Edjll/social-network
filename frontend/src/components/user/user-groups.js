import * as React from "react";
import RequestService from "../../services/RequestService";
import {GroupCard} from "../group/group-card";
import {Card} from "../card/card";
import {CardHeader} from "../card/card-header";
import {CardBody} from "../card/card-body";
import IntersectionObserverService from "../../services/IntersectionObserverService";

export class UserGroups extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            userId: new URLSearchParams(this.props.location.search).get("id"),
            groups: [],
            page: 0,
            size: 10
        }
    }

    componentDidMount() {
        this.loadGroups(() => IntersectionObserverService.create('.group_card:last-child', this, this.loadGroups));
    }

    loadGroups(callback) {
        RequestService
            .getAxios()
            .get(RequestService.URL + `/users/${this.state.userId}/groups`, {
                params: {
                    page: this.state.page,
                    size: this.state.size
                }
            })
            .then(response => this.setState({
                groups: response.data,
                lastSize: response.data.length
            }, () => {
                if (callback) callback();
            }))
    }

    render() {
        return (
            <Card className={"user_groups"}>
                <CardHeader>
                    <h1>Groups</h1>
                </CardHeader>
                <CardBody>
                    {
                        this.state.groups.map(group => <GroupCard key={group.id}
                                                                  id={group.id}
                                                                  address={group.address}
                                                                  title={group.title}
                                                                  description={group.description}
                                                                  subscribed={group.subscribed}
                                                                  userId={this.state.userId}
                        />)
                    }
                </CardBody>
            </Card>
        );
    }
}