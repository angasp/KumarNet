import React, { useEffect } from "react";
import { Routes, Route, BrowserRouter } from "react-router-dom";
import { Provider } from "react-redux";
import { Layout } from "antd";
import "antd/dist/antd.css";

import setAuthToken from "./util/setAuthToken";
import store from "./store";
import AuthScreen from "./screens/AuthScreen";
import "./styles/app.css";
import NavBar from "./screens/NavBar";
import Dashboard from "./screens/DashboardScreen";
import Users from "./containers/Users";

import { loadUser } from "./actions/auth";
import Profile from "./screens/ProfileScreen";

const App = () => {
	if (localStorage.token) {
		setAuthToken(localStorage.token);
	}

	useEffect(() => {
		store.dispatch(loadUser());
	}, []); //[] is for not making it an endless loop, but run once on mounting

	return (
		<Provider store={store}>
			<BrowserRouter>
				<Layout>
					<NavBar />
					<Routes>
						<Route path="/" element={<AuthScreen />} />
						<Route path="/dashboard" element={<Dashboard />} />
						<Route path="/users" element={<Users />} />
						<Route path="/profile/:id" element={<Profile />} />
					</Routes>
				</Layout>
			</BrowserRouter>
		</Provider>
	);
};

export default App;
