"use client"; // 

import { Alert, Button, Snackbar } from "@mui/material";
import { green } from "@mui/material/colors";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useRouter } from "next/navigation"; 
import { currentUser, login } from "../../lib/Redux/Auth/Action.jsx";

const Signin = () => {
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [inputData, setInputData] = useState({ email: "", password: "" });
  const dispatch = useDispatch();
  const router = useRouter();  
  const { auth } = useSelector((store) => store);

  const handleSubmit = (e) => {
    e.preventDefault();
    dispatch(login(inputData));
    setOpenSnackbar(true);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setInputData((values) => ({ ...values, [name]: value }));
  };

  const handleSnackbarClose = () => {
    setOpenSnackbar(false);
  };

  useEffect(() => {
    const token = typeof window !== "undefined" ? localStorage.getItem("token") : null;
    if (token) {
      dispatch(currentUser(token));
    }
  }, []);

  useEffect(() => {
    if (auth.reqUser?.name) {
      router.push("/"); 
    }
  }, [auth.reqUser]);

  return (
    <div className="flex justify-center h-screen w-[100vw] items-center">
      <div className="p-10 w-[30%] shadow-md bg-white">
        <form onSubmit={handleSubmit} className="space-y-5">
          <div>
            <p className="mb-2">Email</p>
            <input
              type="text"
              name="email"
              placeholder="Enter your email"
              onChange={handleChange}
              value={inputData.email}
              className="py-2 outline outline-green-600 w-full rounded-md border"
            />
          </div>
          <div>
            <p className="mb-2">Password</p>
            <input
              type="password"
              name="password"
              placeholder="Enter your password"
              onChange={handleChange}
              value={inputData.password}
              className="py-2 outline outline-green-600 w-full rounded-md border"
            />
          </div>
          <div>
            <Button
              type="submit"
              sx={{ bgcolor: green[700], padding: ".5rem 0rem" }}
              className="w-full"
              variant="contained"
            >
              Sign In
            </Button>
          </div>
        </form>

        <div className="flex space-x-3 items-center mt-5">
          <p className="m-0">Create New Account</p>
          <Button variant="text" onClick={() => router.push("/signup")}>
            SignUp
          </Button>
        </div>
      </div>
      <Snackbar
        open={openSnackbar}
        autoHideDuration={6000}
        onClose={handleSnackbarClose}
      >
        <Alert
          onClose={handleSnackbarClose}
          severity="success"
          sx={{ width: "100%" }}
        >
          Login Successfully!!
        </Alert>
      </Snackbar>
    </div>
  );
};

export default Signin;
