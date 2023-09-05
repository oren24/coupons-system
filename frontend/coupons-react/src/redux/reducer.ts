import {AppState} from "./app-state";
import {Action} from "./action";
import {ActionType} from "./action-type";

// import { fetchAllCoupons } from "../components/coupons/couponsUtils";

const appStateInitialValue = new AppState();

// This function is NOT called directly by the user, it's being called by the store itself, when dispatching an action (see store.dispatch)
export function reduce(oldAppState: AppState = appStateInitialValue, action: Action): AppState {
    // debugger;
    // Cloning the oldState (creating a copy)
    const newAppState = {...oldAppState};

    switch (action.type) {
        case ActionType.FETCH_ALL_COUPONS:

            newAppState.coupons = action.payload;
            break;

        case ActionType.FETCH_ALL_CATEGORIES:
            newAppState.categories = action.payload;
            break;

        case ActionType.FETCH_ALL_COMPANIES:
            newAppState.companies = action.payload;
            break;


        case ActionType.FILTER_COUPONS_BY_CATEGORY:
            newAppState.coupons = newAppState.coupons.filter(coupon => coupon.categoryId === action.payload);
            break;


    }


    // After returning the new state, it's being published to all subscribers
    // Each component will render itself based on the new state
    return newAppState;
}