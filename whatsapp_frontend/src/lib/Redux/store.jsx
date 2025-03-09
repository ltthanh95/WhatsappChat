import { configureStore } from "@reduxjs/toolkit";
import {thunk} from "redux-thunk";
import { authReducer } from "./Auth/Reducer";
import { chatReducer } from "./Chat/Reducer";
import { messageReducer } from "./Message/Reducer";

// Create Redux store with reducers and middleware
const store = configureStore({
  reducer: {
    auth: authReducer,
    chat: chatReducer,
    message: messageReducer,
  },
  middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(thunk),
});

export default store; 
