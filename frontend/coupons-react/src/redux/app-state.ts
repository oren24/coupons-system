import ICoupon from "../models/ICoupon";
import ICategory from "../models/ICategory";
import ICompany from "../models/ICompany";

export class AppState {
    // using axios to get data from the server
    public allCoupons: ICoupon[] = [];
    public coupons: ICoupon[] = [];
    public categories: ICategory[] = [];
    public companies: ICompany[] = [];
    public priceRange: number[] = [0, 1000];
    public dateRange: Date[] = [new Date(), new Date()];
}