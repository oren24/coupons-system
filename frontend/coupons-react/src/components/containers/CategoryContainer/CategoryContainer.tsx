import "./CategoryContainer.css";
import {useEffect} from "react";
import {fetchAllCoupons} from "../../../models/ICoupon";
import {useDispatch, useSelector} from "react-redux";
import {AppState} from "../../../redux/app-state";
import {fetchAllCategories} from "../../../models/ICategory";
import {CategoryCard} from "../../cards/CategoryCard/CategoryCard";
import {store} from "../../../redux/store";
import {ActionType} from "../../../redux/action-type";

export function CategoryContainer() {

    useEffect(() => {
        fetchAllCategories();
    }, []);


    let categories = useSelector((state: AppState) => state.categories);
// sets a filter function to view only the coupons from the selected categories that are not null

    function onCategoryClicked(id: number) {
        // useDispatch({type: ActionType.FILTER_COUPONS_BY_CATEGORY, payload: id})
        }
    return (
        <div className={"CategoryCard"}>
            {/*{categories.map((category) => <CategoryCard key={category.id} categoryName={category.categoryName} id={category.id}/>)}*/}
            {categories.map((category) => <button key={category.id} name={category.categoryName} onClick={()=>onCategoryClicked(category.id)}>{category.categoryName}</button>)}
        </div>
    );
}

// multiple select dropdown menu
