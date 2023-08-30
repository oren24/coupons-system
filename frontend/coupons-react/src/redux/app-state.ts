import ICoupon from "../models/ICoupon";
import ICategory from "../models/ICategory";
import ICompany from "../models/ICompany";

export class AppState {
    // using axios to get data from the server
    public coupons: ICoupon[] = [];
    public categories: ICategory[] = [];
    public companies: ICompany[] = [];
}