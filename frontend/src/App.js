import React from 'react';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import Login from './components/Login';
import Dashboard from './components/Dashboard';

function App() {
  return (
    <Router>
      <Switch>
        <Route path={'(|/login)'} exact component={Login}/>
        <Route path={'/dashboard'} exact component={Dashboard}/>
      </Switch>
    </Router>
  );
}

export default App;
