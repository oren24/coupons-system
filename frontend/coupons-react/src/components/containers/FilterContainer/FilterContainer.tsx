import "./FilterContainer.css";
import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {AppState} from "../../../redux/app-state";

import {store} from "../../../redux/store";
import {ActionType} from "../../../redux/action-type";
import axios from "axios";
import ICategory from "../../../models/ICategory";
import {CategoryContainer} from "../CategoryContainer/CategoryContainer";
import {fetchAllCoupons} from "../CouponsContainer/CouponsContainer";
import {CompanyContainer} from "../CompanyContainer/CompanyContainer";

// fetches all categories from the server


export function FilterContainer() {
    const dispatch = useDispatch();
    const [priceRange, setPriceRange] = useState({min: 0, max: 9999});
    const now = Date.now();
    const [dateRange, setDateRange] = useState({min: new Date(now), max: new Date()});

    function onResetClicked() {
        dispatch({type: ActionType.FETCH_ALL_COUPONS, payload: store.getState().allCoupons})
        }
    function onPriceRangeClicked() {
        dispatch({type: ActionType.FILTER_COUPONS_BY_PRICE_RANGE, payload: priceRange})
        }
    function onDateRangeClicked() {
        dispatch({type: ActionType.FILTER_COUPONS_BY_DATE_RANGE, payload: dateRange})
    }

    return (
        <div className={"FilterContainer"}>
            <CategoryContainer/>
            <label>
                <p>Price range</p>
                <p>from</p>
                <input type="number" placeholder={"min"} onChange={(e) => setPriceRange({...priceRange, min: e.target.valueAsNumber})}/>
                <p>to</p>
                <input type="number" placeholder={"max"} onChange={(e) => setPriceRange({...priceRange, max: e.target.valueAsNumber})}/>
                <button onClick={onPriceRangeClicked}>Filter by price range</button>
            </label>
            <label>
                <p>Date range</p>
                <p>from</p>
                <input type="date" placeholder={"min"} onChange={(e) => setDateRange({...dateRange, min: new Date(e.target.valueAsNumber)})}/>
                <p>to</p>
                <input type="date" placeholder={"max"} onChange={(e) => setDateRange({...dateRange, max: new Date(e.target.valueAsNumber)})}/>
                <button onClick={onDateRangeClicked}>Filter by date range</button>
            </label><br/>
            <CompanyContainer/>
            <br/>

        <button onClick={onResetClicked}>Reset filters</button>

        </div>

    );
}

// multiple select dropdown menu
