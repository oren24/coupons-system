import "./CouponsContainer.css";
import {useEffect} from "react";
import {CouponCard} from "../../cards/CouponCard/CouponCard";
import {useDispatch, useSelector} from "react-redux";
import {AppState} from "../../../redux/app-state";

import axios from "axios";
import {store} from "../../../redux/store";
import {ActionType} from "../../../redux/action-type";
import ICoupon from "../../../models/ICoupon";

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
export function CouponsContainer() {
    let dispatch = useDispatch();

    useEffect(() => {
        fetchAllCoupons();
    }, []);


    let coupons = useSelector((state: AppState) => state.coupons);


    return (
        <div className={"CouponsContainer"}>

            {coupons.map((coupon) => <CouponCard key={coupon.id}
                                                 id={coupon.id}
                                                 name={coupon.name}
                                                 description={coupon.description}
                                                 startDate={coupon.startDate}
                                                 endDate={coupon.endDate}
                                                 categoryId={coupon.categoryId}
                                                 categoryName={coupon.categoryName}
                                                 amount={coupon.amount}
                                                 price={coupon.price}
                                                 companyId={coupon.companyId}
                                                 companyName={coupon.companyName}
                                                 contactEmail={coupon.contactEmail}/>
            )
            }

        </div>
    );
}