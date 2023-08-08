import React, {useState} from 'react';
import {CouponsContainer} from "../containers/CouponsContainer/CouponsContainer";
import {Register} from "../Register/Register";


function Layout() {


    return (
        <section className="layout">
            <header>

            </header>

            <aside>
                <Register />
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
