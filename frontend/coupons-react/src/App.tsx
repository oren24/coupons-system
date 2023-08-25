import './App.css';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import {CouponsContainer} from "./components/containers/CouponsContainer/CouponsContainer";
import {CouponCard} from "./components/cards/CouponCard/CouponCard";
import Layout from './components/layout/Layout';


function App() {

    return (
        <div className="App">
            <BrowserRouter>
                <Routes>
                    <Route path={"/containers/CouponsContainer"} element={"CouponsContainer"}/>
                    <Route path={"/CouponCard"} element={"CouponCard"}/>
                    <Route path={"/layout"} element={"Layout"}/>
                </Routes>
            </BrowserRouter>

            <Layout/>


        </div>
    );
}

export default App;
