import "./CouponCard.css";
import React from "react";
import ICoupon from "../../../models/ICoupon";


export function CouponCard(props: ICoupon) {

    function dateToString(date: Date) {
        return date.toString()
    }


    return (
        <div className="CouponCard">


            <h2>{props.name}</h2>
            {/*<p>{props.id}</p><br/>*/}
            <label>
                <p>{props.description}</p>
            </label>
            <time dateTime={props.startDate.toString()}>{props.endDate.toString()}</time>
            <br/>
            <time dateTime={props.endDate.toString()}>{props.endDate.toString()}</time>
            {/*{props.categoryId}<br/>*/}
            <p>{props.categoryName}</p>
            <p>{props.amount}</p>
            <p>{props.price}</p>
            {/*{props.companyId}<br/>*/}
            <address>
                <p>{props.companyName}</p>
                contact mail: <a href={props.contactEmail}>{props.contactEmail}</a>
            </address>
        </div>
    );
}