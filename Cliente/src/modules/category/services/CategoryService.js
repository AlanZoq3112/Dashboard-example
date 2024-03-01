import axios from "axios";
const api = "http://localhost:8080/api/category/";

const getCategories = async (params) => {
  try {
    const pagination = params
      ? `?page=${params?.page || 0}&size=${params?.size || 10}&sort=${
          params?.sort || "id"
        },${params?.direction || "asc"}`
      : "";
    const response = await axios.post(`${api}paged/${pagination}`, {});
    return response.data;
  } catch (error) {
    throw error;
  }
};

export default { getCategories };
