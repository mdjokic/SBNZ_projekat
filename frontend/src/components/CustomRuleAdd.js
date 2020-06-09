import React, { useState } from 'react';
import Axios from 'axios';
import Navbar from '../shared/Navbar';
import { useHistory } from 'react-router-dom';

const CustomRuleAdd = () => {
    const [text, setText] = useState('')
    const [name, setName] = useState('')

    const history = useHistory()

    const add = (e) => {
        e.preventDefault()
        const toPost = {
            text: text,
            name: name
        }
        setText('Working on it...')
        Axios.post(`${process.env.REACT_APP_API_URL}/drl`, toPost)
            .then(() => history.push('/rules'))
    }

    return (
        <div>
            <Navbar />
            <section className="hero is-info is-fullheight">
                <div className="hero-head">
                    <form onSubmit={add}>
                        <div className="container is-centered" style={{ marginTop: 16 }}>
                            <div className="box">
                                <div className="columns is-centered">
                                    <h1 className="title" style={{ color: 'black' }}>Add new drl file</h1>
                                </div>
                                Name: <input className="input" value={name} onChange={(e) => setName(e.target.value)}></input>
                                <textarea  style={{ marginTop: 16 }} className="textarea" rows={20} cols={25} value={text} onChange={(e) => setText(e.target.value)} />
                                <div className="buttons is-right" style={{ marginTop: 16 }}>
                                    <button type="submit" className="button is-info">Add</button>
                                </div>
                            </div>
                        </div>
                    </form>

                </div>
            </section>
        </div>
    )
}

export default CustomRuleAdd;