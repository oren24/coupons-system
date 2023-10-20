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

