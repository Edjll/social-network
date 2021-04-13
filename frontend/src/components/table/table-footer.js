import './table-footer.css';

export const TableFooter = (props) => {

    return (
        <div className={`table__footer ${props.className ? props.className : ''}`}>
            {props.children}
        </div>
    );
}