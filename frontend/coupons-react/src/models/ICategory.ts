import axios from "axios";

export default interface ICategory {
    id: number;
    name: string;
}

export async function fetchAllCategories() {
    try {
        const response = await axios.get("http://localhost:8080/categories");
        let data = response.data;
        let categories: ICategory[] = data;

        return categories;

    } catch (error: any) {
        console.log(error.message);
    }
}