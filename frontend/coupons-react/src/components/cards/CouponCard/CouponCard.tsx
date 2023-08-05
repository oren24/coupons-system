import axios from "axios";
import React, {useEffect, useState} from "react";


export function CouponCard(props: ICoupon) {

    function dateToString(date:Date){
        return date.toString()
    }


    return (
        <div className="Card">

            {props.id}<br/>
            {props.name}<br/>
            {props.description}<br/>
            <time dateTime={props.startDate.toString()}>{props.endDate.toString()}</time><br/>
            <time dateTime={props.endDate.toString()}>{props.endDate.toString()}</time><br/>
            {props.categoryId}<br/>
            {props.categoryName}<br/>
            {props.amount}<br/>
            {props.price}<br/>
            {props.companyId}<br/>
            {props.companyName}<br/>
            {props.contactEmail}






        </div>
    );
}