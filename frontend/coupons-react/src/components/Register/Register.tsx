import "./Register.css"
import {useState} from "react";
import axios from "axios";

export function Register() {
    let [username, setUserName] = useState('');
    let [password, setPassword] = useState('');
    let [passwordConfirm, setPasswordConfirm] = useState('');
    let [companyId, setCompanyId] = useState('');

    function validatePassword(password: string, passwordConfirm: string) {
        if (password !== passwordConfirm) {
            //throw new ApplicationException(ErrorType.PASSWORDS_DO_NOT_MATCH);
            return false;
        }
        if (password.length < 8) {
            //throw new ApplicationException(ErrorType.PASSWORD_TOO_SHORT);

            return false;
        }
        if (password.length > 20) {
            //throw new ApplicationException(ErrorType.PASSWORD_TOO_LONG);
            return false;
        }
        return true;
    }

    async function validateCompanyId(companyId: any) {

        if (companyId !== null) {
            if (companyId < 1) {
                //throw new ApplicationException(ErrorType.COMPANY_ID_INVALID);
                //window.alert("COMPANY_ID_INVALID");
                return false;
            }
            const response = await axios.get("http://localhost:8080/companies/" + companyId);
            if (response.data === "") {
                //throw new ApplicationException
                //window.alert("COMPANY_ID_INVALID");
                return false; //ErrorType.COMPANY_ID_INVALID);
            }
            return true;
        }


    }


    async function registerUser(newUser: { password: string; companyId: string; username: string }) {
        try {
            let loginDetails = {username: newUser.username, password: newUser.password, companyId: newUser.companyId};
            const response = await axios.post("http://localhost:8080/users", {
                username: newUser.username,
                password: newUser.password
            });

            const serverResponse = response.data;
            // check if the token is valid
            // if the token is valid, redirect to the products page
            // if the token is not valid, show an error message
            // if the token is valid, save the token in the local storage
            // if the token is valid, save the token in the axios defaults

        } catch (error: any) {
            alert("Error:  username or password are incorrect");
            console.log(error.message);
        }

    }

    const onSubmit = async (e: Event) => {
        e.preventDefault();

        try {
            // Validate the email address structure.
            const emailAddress = username;
            if (!emailAddress.match(/^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-z\\A-Z]{2,7}$/)) {
                // throw new ApplicationException(ErrorType.EMAIL_FORMAT_INVALID);
                //changes the email border color to red
                return false;

            }

            // Validate the password.
            validatePassword(password, passwordConfirm);

            // Validate the company ID.

            validateCompanyId(companyId);

            // Submit the form.
            await registerUser({username, password, companyId});

            // Redirect to the home page.
            window.location.href = "/";
        } catch (error: any) {
            // Show an error message.
            alert(error.message);
        }
    };

    return (
        <div>
            <h1>Register</h1>
            <p>this is an empty Registration page</p>

            <label> enter your email address:
                <input type="email" placeholder='email' onChange={event => {
                    setUserName(event.target.value)
                }}/>
            </label>
            <br/>
            <label> enter your password:
                <input type="password" placeholder='password' onChange={event => {
                    setPassword(event.target.value)
                }}/>
            </label>
            <br/>
            <label> confirm your password:
                <input type="password" placeholder='password' onChange={event => {
                    setPasswordConfirm(event.target.value)
                }}/>
            </label>
            <br/>
            <label> optional: enter company id:
                <input type="number" placeholder='company id' onChange={event => setCompanyId(event.target.value)}/>
            </label>
            <br/>

            {/*<button onClick={onSubmit}> sign UP!!</button>*/}
            <br/>




        </div>
    );
}