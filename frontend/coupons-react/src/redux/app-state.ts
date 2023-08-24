import axios from "axios";
import ICoupon from "../models/ICoupon";
import {useState} from "react";

export class AppState {
    // using axios to get data from the server
    public coupons: ICoupon[] = [];


}