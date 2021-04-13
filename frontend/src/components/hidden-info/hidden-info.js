import './hidden-info.css';

export const HiddenInfo = (props) => {
    return (
        <div className={"hidden_info"}>
            <span className={"hidden_info__text"}>{props.text}</span>
            <div className={"hidden_info__hidden"}>{props.hidden}</div>
        </div>
    );
}