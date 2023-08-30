import "./Login.css"
import {useState} from "react";
import Modal from "react-modal";
import axios from "axios";
import jwt_decode from "jwt-decode";
import IUser from "../../models/IUser";

export function Login() {
    let [username, setUserName] = useState('');
    let [password, setPassword] = useState('');
    let [isLoginFailed, setIsLoginFailed] = useState(false);


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


            if (serverResponse.success) {
                let token = 'Bearer ' + serverResponse.data;
                axios.defaults.headers.common['Authorization'] = token;
                localStorage.setItem('token', token);

                let userData: any = jwt_decode(token);
                let connectedUser: IUser = userData.sub;

                // redirect to the products page
            } else {
                // show an error message
                setIsLoginFailed(true);
            }

        } catch (error: any) {
            // create an error message and a red border for the input fields if the login failed

            alert("Error:  username or password are incorrect");
            console.log(error.message);
            setIsLoginFailed(true);

        }
    }


    return (
        <div className="Login">
            <h2>Login</h2>

            <p> already registered? how fun!!</p>

            <h3>Enter your username and password</h3>

            <label >
                username<br/>
                <input type="email" placeholder='userName' onChange={event => setUserName(event.target.value)} style={isLoginFailed ? {border: '1px solid red'} : {}}/>
            </label>
            <br/>

            <label>
                password<br/>
                <input type="password" placeholder='password' onChange={event => setPassword(event.target.value)} style={isLoginFailed ? {border: '1px solid red'} : {}}/>
            </label>
            <br/>

            <label>

                <button id='submit' onClick={onLoginClicked}>Submit</button>
            </label>
            <br/>


            <br/>

        </div>
    );
}