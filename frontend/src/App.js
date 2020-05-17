import React from 'react';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import Login from './components/Login';
import Dashboard from './components/Dashboard';
import Users from './components/Users';
import User from './components/User';
import { PrivateRoute } from './components/PrivateRoute';

function App() {
  return (
    <Router>
      <Switch>
        <Route path={'(|/login)'} exact component={Login}/>
        <PrivateRoute path={'/dashboard'} exact component={Dashboard} roles={['ROLE_ADMIN']}/>
        <PrivateRoute path={'/users'} exact component={Users} roles={['ROLE_ADMIN']}/>
        <PrivateRoute path={'/users/:id'} exact component={User} roles={['ROLE_ADMIN']}/>
      </Switch>
    </Router>
  );
}

export default App;
