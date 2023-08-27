import {CouponsContainer} from "../containers/CouponsContainer/CouponsContainer";
import './Layout.css';
import {useState} from "react";
import Modal from "react-modal";
import {Login} from "../Login/Login";
import {LoginRegister} from "../../Modalpopups/Login-register/LoginRegister";

function Layout() {

    //function to decode the username from the token


    // create a state for the login and registration modals, if the user is logged in, hide the login and registration buttons and show the usermame and logout button



    return (
        <section className="layout">
            <header>

            </header>

            <aside>
               <LoginRegister/>
            </aside>

            <main>

                <CouponsContainer/>

            </main>

            <footer>

            </footer>
        </section>
    );
}

export default Layout;
