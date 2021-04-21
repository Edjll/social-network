import './toast.css';

export const Toast = (props) => {

    setTimeout(() => props.handleClose(), props.time * 1000);

    return (
        <div className={'toast'}>
            <div className={'toast__progress'} style={{animationDuration: `${props.time}s`}}/>
            <div className={'toast__header'}>
                <span>
                    {props.header}
                </span>
                <div className={"toast__header__close"} onClick={() => props.handleClose()}>
                    ✖
                </div>
            </div>
            <div className={'toast__body'}>
                <span>
                    {props.body}
                </span>
            </div>
        </div>
    );
}