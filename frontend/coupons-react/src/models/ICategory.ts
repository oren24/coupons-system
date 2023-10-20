import axios from "axios";
import {ActionType} from "../redux/action-type";
import {store} from "../redux/store";

export default interface ICategory {
    id: number;
    categoryName: string;
}

