import React, { useState, useEffect } from 'react';
import Axios from 'axios';
import Navbar from '../shared/Navbar';
import { Link, useHistory } from 'react-router-dom';

const CustomRules = () => {
    const [rules, setRules] = useState([])
    const history = useHistory();

    const [deleting, setDeleting] = useState(false)

    const get = () => {
        Axios.get(`${process.env.REACT_APP_API_URL}/drl`)
            .then(response => setRules(response.data))
    }

    useEffect(() => {
        get()
    }, [])

    const goAdd = (event) => {
        event.preventDefault();
        history.push("/rules/add")
        return;
    }

    const goDelete = (id) => (event) => {
        event.preventDefault();
        setDeleting(true);
        Axios.delete(`${process.env.REACT_APP_API_URL}/drl/${id}`)
            .then(() => {
                setDeleting(false);
                get()
            })
        return;
    }

    return (
        <div>
            <Navbar />
            <section className="hero is-info is-fullheight">
                <div className="hero-head">
                    <div className="container is-centered" style={{ marginTop: 16, width: 640 }}>
                        <div className="box">
                            <button className="button is-info" onClick={goAdd}>New rule</button>
                            <div className="columns is-centered">
                                <h1 className="title" style={{ color: 'black' }}>{deleting ? 'Deleting...' : 'Custom rules'}</h1>
                            </div>
                            <div className="columns is-centered">
                                <table className="table is-striped" style={{ marginTop: 16, width: 640 }}>
                                    <thead>
                                        <tr>
                                            <th>Name</th>
                                            <th colSpan={2}>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {
                                            rules.map(rule =>
                                                <tr key={rule.id}>
                                                    <td>{rule.name}</td>
                                                    <td>
                                                        <Link to={`/rules/${rule.id}`}>
                                                            details
                                                    </Link>
                                                    </td>
                                                    <td>
                                                        <a onClick={goDelete(rule.id)}>delete</a>
                                                    </td>
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

export default CustomRules;