import './table-head.css';

export const TableHead = (props) => {

    return (
        <div className={`table__head ${props.className ? props.className : ''}`}>
            {props.children}
        </div>
    );
}