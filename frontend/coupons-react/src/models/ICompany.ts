export default interface ICompany {

    id: number,
    name: string,
    registryNumber: number,
    address: string,
    contactEmail: string

}

export async function fetchAllCompanies() {
    const response = await fetch("http://localhost:8080/companies");
    const data = await response.json();
    return data;
}