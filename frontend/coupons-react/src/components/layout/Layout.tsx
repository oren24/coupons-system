import {CouponsContainer} from "../containers/CouponsContainer/CouponsContainer";
import './Layout.css';
import {useState} from "react";
import Modal from "react-modal";
import {Login} from "../Login/Login";
import {LoginRegister} from "../../Modalpopups/Login-register/LoginRegister";

function Layout() {



    return (
        <section className="layout">
            <header>
                <LoginRegister/>
            </header>

            <aside>

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
