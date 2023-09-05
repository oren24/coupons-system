import axios from "axios";
import {store} from "../redux/store";
import {ActionType} from "../redux/action-type";

export default interface ICompany {

    id: number,
    name: string,
    registryNumber: number,
    address: string,
    contactEmail: string

}

export async function fetchAllCompanies() {
    const response = await axios.get("http://localhost:8080/companies");
    let data = response.data;
    let companies: ICompany[] = data;
    store.dispatch({type: ActionType.FETCH_ALL_COMPANIES, payload: companies});
    return data;
}