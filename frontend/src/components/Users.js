import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import Navbar from '../shared/Navbar';

const Users = () => {

    const [users, setUsers] = useState([])

    useEffect(() => {
        axios.get(`${process.env.REACT_APP_API_URL}/users`)
            .then(response => setUsers(response.data))
    }, [])

    return (
        <div>
        <Navbar />
        <section className="hero is-info is-fullheight">
            <div className="hero-head">
                <div className="container is-centered" style={{ marginTop: 16, width: 640 }}>
                    <div className="box">
                        <div className="columns is-centered">
                            <h1 className="title" style={{color: 'black'}}>All users</h1>
                        </div>
                        <div className="columns is-centered">
                            <table className="table is-striped" style={{ marginTop: 16, width: 640 }}>
                                <thead>
                                    <tr>
                                        <th>Username</th>
                                        <th>Threat level</th>
                                        <th>Punishment</th>
                                        <th></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {
                                        users.map(user =>
                                            <tr key={user.id}>
                                                <td>{user.username}</td>
                                                <td>{user.threatLevel}</td>
                                                <td>{user.punishment}</td>
                                                <td><Link to={`/users/${user.id}`}>details</Link></td>
                                            </tr>)
                                    }
                                </tbody>
                            </table>
                        </div>
                    </div>

                </div>
            </div>

        </section>
    </div>
    )
}

export default Users;