import './loading-animation.css';

export const LoadingAnimation = (props) => {
    return (
        <div className={'loading_animation ' + props.className}>
            <div className={'loading_animation__item'}/>
            <div className={'loading_animation__item'}/>
            <div className={'loading_animation__item'}/>
        </div>
    );
}