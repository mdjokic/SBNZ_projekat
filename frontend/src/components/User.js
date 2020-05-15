import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import moment from 'moment';

const User = () => {

    const { id } = useParams()

    const [user, setUser] = useState({
        matches: []
    })

    useEffect(() => {
        axios.get(`${process.env.REACT_APP_API_URL}/users/${id}`)
            .then(response => setUser(response.data))
    }, [id])

    return (
        <section className="hero is-info is-fullheight">
            <div className="container is-centered" style={{ marginTop: 16, width: 640 }}>
                <div className="box">
                    <div className="columns">
                        <h1 className="title is-3" style={{ color: 'black' }}>User details</h1><br />
                    </div>
                    <div className="columns">
                        <h1 className="title is-4" style={{ color: 'black' }}>Username: {user.username}</h1>
                    </div>
                    <div className="columns">
                        <h1 className="title is-4" style={{ color: 'black' }}>Threat level: {user.threatLevel}</h1>
                    </div>
                    <div className="columns">
                        <h1 className="title is-4" style={{ color: 'black' }}>Punishment: {user.punishment}</h1>
                    </div>
                    <br />
                    <div className="columns">
                        <h1 className="title is-3" style={{ color: 'black' }}>Match history</h1>
                    </div>
                    <div className="columns">
                        <table className="table is-striped" style={{ marginTop: 16, width: 640 }}>
                            <thead>
                                <tr>
                                    <th>Match ID</th>
                                    <th>Username</th>
                                    <th>Timestamp</th>
                                    <th>Finished</th>
                                    <th>Report</th>
                                </tr>
                            </thead>
                            <tbody>
                                {
                                    user.matches.map(match =>
                                        <tr key={match.id}>
                                            <td>{match.id}</td>
                                            <td>{match.userId}</td>
                                            <td>{moment(match.timestamp).format('DD.MM.YYYY HH:mm:ss')}</td>
                                            <td>{match.finished ? 'FINISHED' : 'LEFT'}</td>
                                            <td>{match.report}</td>
                                        </tr>)
                                }
                            </tbody>
                        </table>
                    </div>
                </div>

            </div>
        </section>
    )

}

export default User;