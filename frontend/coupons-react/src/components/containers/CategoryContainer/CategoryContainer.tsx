import "./CategoryContainer.css";
import {useEffect} from "react";
import {useDispatch, useSelector} from "react-redux";
import {AppState} from "../../../redux/app-state";

import {store} from "../../../redux/store";
import {ActionType} from "../../../redux/action-type";
import axios from "axios";
import ICategory from "../../../models/ICategory";

// fetches all categories from the server
export async function fetchAllCategories() {
    try {
        const response = await axios.get("http://localhost:8080/categories");
        let data = response.data;
        let categories: ICategory[] = data;
        store.dispatch({ type: ActionType.FETCH_ALL_CATEGORIES, payload: categories });// this is the initial call to fetch coupons
        return categories;

    } catch (error: any) {
        console.log(error.message);
    }
}

export function CategoryContainer() {
    const dispatch = useDispatch();

    useEffect(() => {
        fetchAllCategories();
    }, []);


    let categories = useSelector((state: AppState) => state.categories);
// sets a filter function to view only the coupons from the selected categories that are not null

    function onCategoryClicked(id: number) {
        dispatch({type: ActionType.FILTER_COUPONS_BY_CATEGORY, payload: id})
        }
    return (
        <div className={"CategoryCard"}>
            {/*{categories.map((category) => <CategoryCard key={category.id} categoryName={category.categoryName} id={category.id}/>)}*/}
            {categories.map((category) => <button key={category.id} name={category.categoryName} onClick={()=>onCategoryClicked(category.id)}>{category.categoryName}</button>)}
        </div>
    );
}

// multiple select dropdown menu
