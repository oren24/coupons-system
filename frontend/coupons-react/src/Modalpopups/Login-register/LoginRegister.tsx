import Modal from "react-modal";
import {Login} from "../../components/Login/Login";
import {useEffect, useState} from "react";
import {Register} from "../../components/Register/Register";
const customRegisterStyles = {
    content: {
        top: '50%',
        left: '50%',
        right: 'auto',
        bottom: 'auto',
        marginRight: '-50% auto',

        transform: 'translate(-50%, -50%)',
    },
};
const customLoginStyles = {
    content: {
        top: '50% ',
        left: '50%',
        right: 'auto',
        bottom: 'auto',
        marginRight: '-50%',
        marginTop: '-15%',
        transform: 'translate(-50%, -50%)',
    },
};
export function LoginRegister() {
    //function to decode the username from the token
    const decodeToken = (token: string) => {
        let tokenBody = token.split('.')[1];
        let decodedToken = atob(tokenBody);
        let user = JSON.parse(decodedToken).sub;
        return user;
    }
    // check if there is a token in the local storage and if there is, decode it and get the username



    // create a state for the login and registration modals
    const [isLoginModalVisible, setIsLoginModalVisible] = useState(false);
    const [isRegisterModalVisible, setIsRegisterModalVisible] = useState(false);

    // hook to check if the token is exist in the local storage
    const [token, setToken] = useState(localStorage.getItem('token')? true : false);

    //let [username, setUsername] = useState(token ? decodeToken(token) : null);
    // a function to handle the login and registration modals
    const handleLoginModal = () => {
        setIsLoginModalVisible(!isLoginModalVisible);
        setIsRegisterModalVisible(false);
    }

    // a function to handle the login and registration modals
    const handleRegisterModal = () => {
        setIsRegisterModalVisible(!isRegisterModalVisible);
        setIsLoginModalVisible(false);

    }
    // a useEffect hook to check if the token is exist in the local storage
    // if the token is exist, hide the login and registration buttons and show the usermame and logout button
    // if the token is not exist, show the login and registration buttons and hide the usermame and logout button
    useEffect(() => {
        if (token) {
            setIsLoginModalVisible(false);
            setIsRegisterModalVisible(false);
        }},[token])
    return (
        <div className="login-register">
            <button onClick={handleLoginModal}>Login</button>
            <Modal
                isOpen={isLoginModalVisible}
                onRequestClose={handleLoginModal}
                style={customLoginStyles}>

                <Login/>

                <button id='register' onClick={handleRegisterModal}>dont have a user? Register!</button>
                <button onClick={handleLoginModal}>Close</button>

            </Modal>
            <button onClick={handleRegisterModal}>Register</button>

            <Modal

                isOpen={isRegisterModalVisible}
                onRequestClose={handleRegisterModal}
                style={customRegisterStyles}>

                <Register/>

                <button id='login' onClick={handleLoginModal}>already registered? Login!</button>
                <button onClick={handleRegisterModal}>Close</button>
            </Modal>

        </div>
    );
}