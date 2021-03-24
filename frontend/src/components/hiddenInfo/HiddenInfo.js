import './HiddenInfo.css';

export const HiddenInfo = (props) => {
    return (
        <div className={"hidden-info"}>
            <span className={"hidden-info__text"}>{props.text}</span>
            <div className={"hidden-info__hidden"}>{props.hidden}</div>
        </div>
    );
}