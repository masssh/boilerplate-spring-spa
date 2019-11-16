import axios from "axios";

export async function signup(body: any) {
  const { data } = await axios.post("/api/user/add", body);
  return data;
}

export async function login(body: any) {
  const { data } = await axios.post("/api/login", body);
  return data;
}
