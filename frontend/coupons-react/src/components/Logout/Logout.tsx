export function Logout() {
    // const decodeToken = (token: string) => {
    //     let tokenBody = token.split('.')[1];
    //     let decodedToken = atob(tokenBody);
    //     let username = JSON.parse(decodedToken).sub;
    //     return username;
    // }
    // check if there is a token in the local storage and if there is, decode it and get the username
    // let token = localStorage.getItem('token');
    // let username = token ? decodeToken(token) : null;

    return (
        <div className={"logout"}>

        </div>
    );
}