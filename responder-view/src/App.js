import React from 'react';
import './App.css';
import {Route} from "react-router-dom";
import Login from "./Login";
import ResponderHome from "./ResponderHome";

function App() {
  return (
    <div className="App">
      <Route exact path="/" component={Login}/>
      <Route exact path="/home" component={ResponderHome}/>
    </div>
  );
}

export default App;
