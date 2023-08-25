import {useState} from "react";
import axios from "axios";
import jwt_decode from "jwt-decode";
import IUser from "../../models/IUser";

export function Login() {
    let [username, setUserName] = useState('');
    let [password, setPassword] = useState('');


    const onLoginClicked = async () => {


        try {

            let loginDetails = {username, password};
            const response = await axios.post("http://localhost:8080/users/login", {username, password});

            const serverResponse = response.data;
            // check if the token is valid
            // if the token is valid, redirect to the products page
            // if the token is not valid, show an error message
            // if the token is valid, save the token in the local storage
            // if the token is valid, save the token in the axios defaults


            let token = 'Bearer ' + serverResponse;
            axios.defaults.headers.common['Authorization'] = token;
            localStorage.setItem('token', token);

            let userData: any = jwt_decode(token);
            let connectedUser: IUser = userData.sub;


        } catch (error: any) {
            alert("Error:  username or password are incorrect");
            console.log(error.message);

        }
    }


    return (
        <div className="Login">
            <h2>Login</h2>

            <p> already registered? how fun!!</p>

            <h3>Enter your username and password</h3>

            <label>
                username<br/>
                <input type="text" placeholder='userName' onChange={event => setUserName(event.target.value)}/>
            </label>
            <br/>

            <label>
                password<br/>
                <input type="password" placeholder='password' onChange={event => setPassword(event.target.value)}/>
            </label>
            <br/>

            <label>

                <button id='submit' onClick={onLoginClicked}>Submit</button>
            </label>
            <br/>

            <label>

                <button id='register' onClick={onLoginClicked}>dont have a user? Register!</button>
            </label>
            <br/>

        </div>
    );
}