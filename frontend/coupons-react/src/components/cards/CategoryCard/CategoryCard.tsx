import "./CategoryCard.css";
import ICategory from "../../../models/ICategory";

export interface ICategoryCardProps {
}

export function CategoryCard(props: ICategory) {

    return (
        <div className={"CategoryCard"}>
            <p>{props.categoryName}</p>
            {props.categoryName}
            <button>{props.categoryName}</button>

        </div>
    );
}