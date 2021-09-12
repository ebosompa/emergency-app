import React from "react";
import ReactDom from "react-dom";
import 'bootstrap/dist/css/bootstrap.min.css';
import './Login.css';
import {Link} from "react-router-dom";

function Login(){
    function loginUser(event){
    }

    return (

        <div className="login text-center">
            <form class="form-signin">
                <h1> Welcome Responder</h1>
                <h1 class="h3 mb-3 font-weight-normal">Please sign in</h1>
                <label for="inputID" class="sr-only">ResponderID</label>
                <input type="text" id="inputEmail" class="form-control" placeholder="Responder ID" required autofocus/>
                <label for="inputPassword" class="sr-only">Password</label>
                <input type="password" id="inputPassword" class="form-control" placeholder="Password" required/>
                <button class="btn btn-lg btn-primary btn-block" onClick={loginUser} type="submit">Sign in</button>
            </form>
        </div>
    )
}

export default Login;