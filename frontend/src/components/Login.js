import React, {useState} from 'react';
import {AuthenticationService} from '../services/AuthenticationService'
import { useHistory } from 'react-router-dom';

const Login = () => {
    const [user, setUser] = useState({ username: "", password: ""})
    const history = useHistory();
    
    const handleChange = (name) => (event) =>{
        const val = event.target.value;
        setUser({...user, [name]: val});
    }

    const handleSubmit = (event) => {
        event.preventDefault();
        AuthenticationService.authenticate(user)
        .then(() => history.push("/admin"))
        .catch(() => {});
        return;
    }


    return (
        <section className="hero is-info is-fullheight">
            <div className="hero-body">
                <div className="container">
                    <div className="columns is-centered">
                        <div className="column is-4-desktop">
                            <form className="box" onSubmit={handleSubmit}>
                                <div className="field">
                                    <label className="label">Username</label>
                                    <div className="control">
                                        <input placeholder="username" className="input" onChange={handleChange("username")}  required />
                                    </div>
                                </div>
                                <div className="field">
                                    <label className="label">Password</label>
                                    <div className="control">
                                        <input type="password" placeholder="*********" className="input" onChange={handleChange("password")} required />
                                    </div>
                                </div>
                                <div className="control">
                                    <div className="buttons is-right">
                                        <button type="submit" className="button is-info">Login</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    )
}

export default Login;