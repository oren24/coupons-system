import "./CouponsContainer.css";
import axios from "axios";
import {useEffect, useState} from "react";
import {CouponCard} from "../../cards/CouponCard/CouponCard";

export function CouponsContainer() {

    async function fetchCoupons() {
        try {
            const response = await axios.get("http://localhost:8080/coupons");
            let coupons= response.data;

            //todo: remove "console.log" from the code after testing
            console.log(coupons);
            console.log(typeof(coupons[0]));


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


    const [coupons, setCoupons] = useState<ICoupon[]>([]);
    //const [coupons2, setCoupons2] = useState<ICoupon>(fetchCoupons);

    useEffect(() => {
        fetchCoupons().then((coupons) => {
            setCoupons(coupons);
        });
    }, []);
    return (
        <div className={"CouponsContainer"}>

            {coupons.map(coupon =><CouponCard key={coupon.id}
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
                                              contactEmail={coupon.contactEmail}/> )}

        </div>
    );
}