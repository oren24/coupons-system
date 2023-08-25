import ICategory from "../../../models/ICategory";

export interface ICategoryCardProps {

}

export function CategoryCard(props: ICategory) {

    return (
        <div className={"CategoryCard"}>
            {props.name}
        </div>
    );
}