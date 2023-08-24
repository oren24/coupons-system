import "./CouponsContainer.css";
import axios from "axios";
import {useEffect, useState} from "react";
import {CouponCard} from "../../cards/CouponCard/CouponCard";
import {useDispatch, useSelector} from "react-redux";
import {AppState} from "../../../redux/app-state";
import {ActionType} from "../../../redux/action-type";
import {store} from "../../../redux/store";
import ICoupon, {fetchAllCoupons} from "../../../models/ICoupon";

export function CouponsContainer() {
    let dispatch = useDispatch();
    async function fetchCoupons() {
        try {
            const response = await axios.get("http://localhost:8080/coupons");
            let data= response.data;
            let coupons:ICoupon[] = data;

            store.dispatch({type: ActionType.FETCH_ALL_COUPONS,payload: coupons});// this is the initial call to fetch coupons
            return coupons;

        } catch (error: any) {
            console.log(error.message);
        }
    }

    // creates a map of coupons
    // function Coupons() {
    //
    //
    //     //useEffect(() => fetchCoupons, []);
    //
    // }


     //const [coupons, setCoupons] = useState<ICoupon[]>([]);
    //const [coupons2, setCoupons2] = useState<ICoupon>(fetchCoupons);

    useEffect(() => {
        fetchAllCoupons();
    }, []);
    // this is the initial call to fetch coupons


    let coupons = useSelector((state: AppState) => state.coupons);
    //coupons.forEach(coupon => console.log(coupon));
    // creates initial call to fetch coupons


    return (
        <div className={"CouponsContainer"}>

            {coupons.map((coupon) =><CouponCard key={coupon.id}
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