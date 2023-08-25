import "./CouponsContainer.css";
import {useEffect} from "react";
import {CouponCard} from "../../cards/CouponCard/CouponCard";
import {useDispatch, useSelector} from "react-redux";
import {AppState} from "../../../redux/app-state";
import {fetchAllCoupons} from "../../../models/ICoupon";


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