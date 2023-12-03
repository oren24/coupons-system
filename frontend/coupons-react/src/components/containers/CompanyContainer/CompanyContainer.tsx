import "./CompanyContainer.css";
import {useEffect} from "react";
import {useDispatch, useSelector} from "react-redux";
import {AppState} from "../../../redux/app-state";

import {store} from "../../../redux/store";
import {ActionType} from "../../../redux/action-type";
import axios from "axios";
import ICategory from "../../../models/ICategory";
import ICompany from "../../../models/ICompany";

// fetches all categories from the server
export async function fetchAllCompanies() {
    try {
        const response = await axios.get("http://localhost:8080/companies");
        let data = response.data;
        let companies: ICompany[] = data;
        store.dispatch({type: ActionType.FETCH_ALL_COMPANIES, payload: companies});// this is the initial call to fetch coupons
        return companies;

    } catch (error: any) {
        console.log(error.message);
    }
}

export function CompanyContainer() {
    const dispatch = useDispatch();

    useEffect(() => {
        fetchAllCompanies();
    }, []);


    let companies = useSelector((state: AppState) => state.companies);

// sets a filter function to view only the coupons from the selected categories that are not null

    function onCompanyClicked(id: number) {
        dispatch({type: ActionType.FILTER_COUPONS_BY_COMPANY, payload: id})
    }

    return (
        <div className={"CategoryCard"}>
            {/*{categories.map((category) => <CategoryCard key={category.id} categoryName={category.categoryName} id={category.id}/>)}*/}
            {companies.map((company) => <button key={company.id} name={company.name}
                                                  onClick={() => onCompanyClicked(company.id)}>{company.name}</button>)}
        </div>

    );
}

// multiple select dropdown menu
