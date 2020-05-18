import React from 'react'
import { Link } from 'react-router-dom'

const Navbar = () => {

    return(
        <nav className="navbar is-white" role="navigation" aria-label="main navigation">
            <div className="navbar-menu">
                <div className="navbar-start">
                    <Link className="navbar-item" to="/dashboard">
                        Dashboard
                    </Link>
                    <Link className="navbar-item" to="/users">
                        Users
                    </Link>
                </div>
            </div>
        </nav>
    )


}

export default Navbar;