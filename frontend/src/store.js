import { configureStore } from "@reduxjs/toolkit";

import rootReducer from "./reducers";

const initialState = {};

const store = configureStore({
	reducer: rootReducer,
	devTools: true,
	middleware: (getDefaultMiddleware) => getDefaultMiddleware(),
	preloadedState: initialState,
});

export default store;
