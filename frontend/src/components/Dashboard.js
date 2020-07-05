import React, { useState, useEffect } from 'react';
import { MatchService } from '../services/MatchService'
import moment from 'moment';
import Stomp from 'stompjs';
import Navbar from '../shared/Navbar';

var stompClient = null;

const Dashboard = () => {
    const [latestMatches, setLatestMatches] = useState([]);
    const [started, setStarted] = useState(false);
    const [simulationParams, setSimulationParams] = useState({
        userId: null,
        finishedChance: 90,
        noReportChance: 50,
        intervalBetweenMatches: 3000
    })
    useEffect(() => {
        window.addEventListener("unload", () => handleStop());
        MatchService.getLatest()
            .then((response) => setLatestMatches(response.data));
        return () => handleStop();
    }, [])

    const handleStart = (e) => {
        e.preventDefault();
        var socket = new WebSocket('ws://localhost:8080/connect');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            stompClient.subscribe('/topic/messages', function (messageOutput) {
                updateMatches(messageOutput);
            });
            stompClient.send("/ws/start", {}, JSON.stringify(simulationParams));
            setStarted(true);
        });
    }

    const handleChange = (name) => (event) => {
        const val = event.target.value;
        if (name === "userId" && val.trim() === "") {
            setSimulationParams({ ...simulationParams, [name]: null })
            return;
        }
        setSimulationParams({ ...simulationParams, [name]: val });
    }

    const handleStop = (e) => {
        if(e === undefined){
            return;
        }
        e.preventDefault()
        if (stompClient === null) {
            return;
        }
        stompClient.send("/ws/stop")
        stompClient.disconnect()
        setStarted(false);
    }

    const handleReset = () => {
        MatchService.reset()
            .then(() => window.location.reload())
    }

    const updateMatches = (message) => {
        console.log(message);
        var latestMatch = JSON.parse(message.body);
        if (latestMatch.body === null) {
            return;
        }
        setLatestMatches(prevState => {
            var newArray = [];
            newArray = Array.from(prevState);
            if (prevState.length < 10) {
                newArray.unshift(latestMatch);
            } else {
                newArray.pop();
                newArray.unshift(latestMatch);
            }
            return [...newArray]
        });
    }



    return (
        <div>
            <Navbar />
            <div className="hero is-info is-bold is-fullheight">
                <div className="hero-head">
                </div>
                <div className="hero-body">
                    <div className="container ">
                        <div className="box" style={{ minHeight: "800px" }}>
                            <div className="columns">
                                <h3 className="subtitle is-3" style={{ color: "black" }}>Latest matches</h3>
                            </div>
                            <div className="columns is-vcentered">
                                <div className="column">
                                    <table className="table is-striped">
                                        <thead>
                                            <tr>
                                                <th>Id</th>
                                                <th>Username</th>
                                                <th>Date</th>
                                                <th>Finished</th>
                                                <th>K/D</th>
                                                <th>Report</th>
                                                <th>Threat level</th>
                                                <th>Punishment</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {latestMatches.map((match) =>
                                                <tr key={match.id}>
                                                    <td>{match.id}</td>
                                                    <td>{match.usernameId}</td>
                                                    <td>{moment(match.timestamp).format('DD.MM.YYYY HH:mm:ss')}</td>
                                                    <td>{String(match.finished)}</td>
                                                    <td>{Number(match.kd).toFixed(2)}</td>
                                                    <td>{match.report}</td>
                                                    <td>{match.threatLevel}</td>
                                                    <td>{match.punishment}</td>
                                                </tr>
                                            )}
                                        </tbody>
                                    </table>
                                </div>
                                <div style={{ textAlign: "center" }} className="column">
                                    <form onSubmit={handleStart}>
                                        <div className="columns">
                                            <div className="column">
                                                {
                                                    started ? <button className="button is-info is-medium " onClick={handleStop}>Stop</button> : <button className="button is-info is-medium" type="submit">Start simulation</button>
                                                }
                                            </div>
                                        </div>
                                        <div className="columns">
                                            <div className="column">
                                                <div className="field">
                                                    <label className="label">Username</label>
                                                    <div className="control">
                                                        <input placeholder="UserId" className="input" onChange={handleChange("userId")} />
                                                    </div>
                                                </div>
                                            </div>
                                            <div className="column">
                                                <div className="field">
                                                    <label className="label">Finished Chance</label>
                                                    <div className="control">
                                                        <input required placeholder="Finished Chance" className="input" value={simulationParams.finishedChance} type="number" step="1" min="0" max="100" onChange={handleChange("finishedChance")} />
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="columns">
                                            <div className="column">
                                                <div className="field">
                                                    <label className="label">No report Chance</label>
                                                    <div className="control">
                                                        <input required placeholder="No report Chance" className="input" value={simulationParams.noReportChance} type="number" step="1" min="0" max="100" onChange={handleChange("noReportChance")} />
                                                    </div>
                                                </div>
                                            </div>
                                            <div className="column">
                                                <div className="field">
                                                    <label className="label">Interval between matches</label>
                                                    <div className="control">
                                                        <input required placeholder="Interval between matches" className="input" value={simulationParams.intervalBetweenMatches} type="number" step="1000" min="0" onChange={handleChange("intervalBetweenMatches")} />
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </form>
                                    <div className="columns">
                                        <div className="column">
                                            { !started && <button className="button is-info is-medium " onClick={handleReset}>Reset everything</button>}
                                        </div>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Dashboard;