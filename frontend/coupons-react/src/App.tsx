import React from 'react';
import logo from './logo.svg';
import './App.css';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import {CouponsContainer} from "./components/containers/CouponsContainer/CouponsContainer";
import {CouponCard} from "./components/cards/CouponCard/CouponCard";

function App() {
  return (
    <div className="App" >
      <BrowserRouter>
        <Routes>
          <Route path={"/containers/CouponsContainer"} element={"CouponsContainer"} />
          <Route path={"/CouponCard"} element={"CouponCard"} />
        </Routes>
      </BrowserRouter>

      <CouponsContainer />




      {/*<header className="App-header">*/}
      {/*  <img src={logo} className="App-logo" alt="logo" />*/}
      {/*  <p>*/}
      {/*    Edit <code>src/App.tsx</code> and save to reload.*/}
      {/*  </p>*/}
      {/*  <a*/}
      {/*    className="App-link"*/}
      {/*    href="https://reactjs.org"*/}
      {/*    target="_blank"*/}
      {/*    rel="noopener noreferrer"*/}
      {/*  >*/}
      {/*    Learn React*/}
      {/*  </a>*/}
      {/*</header>*/}
    </div>
  );
}

export default App;
