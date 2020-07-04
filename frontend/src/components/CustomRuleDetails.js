import React, { useState, useEffect } from 'react';
import Axios from 'axios';
import Navbar from '../shared/Navbar';
import { useParams } from 'react-router-dom';

const CustomRuleDetails = () => {
    const [text, setText] = useState('')

    const { id } = useParams();

    const get = () => {
        Axios.get(`${process.env.REACT_APP_API_URL}/drl/${id}`)
            .then(response => setText(response.data))
    }

    useEffect(() => {
        get()
    }, [id])

    const save = () => {
        setText('Working on it...')
        const toPut = {
            id: id,
            text: text,
        }
        Axios.put(`${process.env.REACT_APP_API_URL}/drl`, toPut)
            .then(() => get())
            .catch(() => setText('Error in rule'))

    }

    return (
        <div>
            <Navbar />
            <section className="hero is-info is-fullheight">
                <div className="hero-head">
                    <div className="container is-centered" style={{ marginTop: 16 }}>
                        <div className="box">
                            <div className="columns is-centered">
                                <h1 className="title" style={{ color: 'black' }}>Edit drl file</h1>
                            </div>
                            <form>
                                <textarea className="textarea" rows={25} cols={25} value={text} onChange={(e) => setText(e.target.value)} />
                            </form>
                            <div className="buttons is-right" style={{ marginTop: 16 }}>
                                <button type="submit" className="button is-info" onClick={save}>Save</button>
                            </div>
                        </div>

                    </div>
                </div>
            </section>
        </div>
    )
}

export default CustomRuleDetails;