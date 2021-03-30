import './TablePageSize.css';

export const TablePageSize = (props) => {

    return (
        <input className={`table__page-size ${props.className ? props.className : ''}`} defaultValue={props.value} type={'number'} onBlur={(e) => props.handleChange(e.target.value)}/>
    );
}