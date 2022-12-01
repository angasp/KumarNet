import { combineReducers } from "@reduxjs/toolkit";

import auth from "./auth";
import profile from "./profile";
import post from "./post";

export default combineReducers({  auth, profile, post });
