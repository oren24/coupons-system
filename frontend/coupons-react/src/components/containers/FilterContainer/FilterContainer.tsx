import "./FilterContainer.css";
import {useEffect} from "react";
import {useDispatch, useSelector} from "react-redux";
import {AppState} from "../../../redux/app-state";

import {store} from "../../../redux/store";
import {ActionType} from "../../../redux/action-type";
import axios from "axios";
import ICategory from "../../../models/ICategory";
import {CategoryContainer} from "../CategoryContainer/CategoryContainer";
import {fetchAllCoupons} from "../CouponsContainer/CouponsContainer";

// fetches all categories from the server


export function FilterContainer() {
    const dispatch = useDispatch();


    function onResetClicked() {
        dispatch({type: ActionType.FETCH_ALL_COUPONS, payload: store.getState().allCoupons})
        }

    return (
        <div className={"FilterContainer"}>
            <CategoryContainer/>

        <button onClick={onResetClicked}>Reset filters</button>

        </div>

    );
}

// multiple select dropdown menu
