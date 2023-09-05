import axios from "axios";
import {ActionType} from "../redux/action-type";
import {store} from "../redux/store";

export default interface ICategory {
    id: number;
    categoryName: string;
}

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