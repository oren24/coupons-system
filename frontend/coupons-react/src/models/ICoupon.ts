import axios from "axios";
import {store} from "../redux/store";
import {ActionType} from "../redux/action-type";

export default interface ICoupon {
    id: number;
    name: string;
    description: string;
    startDate: Date;
    endDate: Date;
    categoryId: number;
    categoryName: string;
    amount: number;
    price: number;
    companyId: number;
    companyName: string;
    contactEmail: string;


}

export async function fetchAllCoupons() {
    try {
        const response = await axios.get("http://localhost:8080/coupons");
        let data = response.data;
        let coupons: ICoupon[] = data;

        store.dispatch({type: ActionType.FETCH_ALL_COUPONS, payload: coupons});// this is the initial call to fetch coupons
        return coupons;

    } catch (error: any) {
        console.log(error.message);
    }
}