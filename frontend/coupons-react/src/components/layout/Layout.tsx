import React, {useState} from 'react';
import {CouponsContainer} from "../containers/CouponsContainer/CouponsContainer";
import {Register} from "../Register/Register";
import {Login} from "../Login/Login";
import './Layout.css';

function Layout() {


    return (
        <section className="layout">
            <header>

            </header>

            <aside>
                <Login />
            </aside>

            <main>

                <CouponsContainer />

            </main>

            <footer>

            </footer>
        </section>
    );
}

export default Layout;
