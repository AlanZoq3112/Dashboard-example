import axios from "axios";
const api = "http://localhost:8080/api/movie/";

const saveMovie = async (movieData) => {
  try {
    const response = await axios.post(api, movieData, {
      headers: {
        "Content-Type": "application/json",
      },
    });
    return response;
  } catch (error) {
    console.error("Error al enviar la solicitud:", error);
    if (error.response) {
      console.error("Respuesta del servidor:", error.response.data);
    }
    throw error;
  }
};

const updateMovie = async (movieData) => {
  try {
    const response = await axios.put(api, movieData, {
      headers: {
        "Content-Type": "application/json",
      },
    });
    return response;
  } catch (error) {
    console.error("Error al enviar la solicitud:", error);
    if (error.response) {
      console.error("Respuesta del servidor:", error.response.data);
    }
    throw error;
  }
};

const deleteMovie = async (id) => {
  console.log(`${api}${id}`)
  axios.delete(`${api}${id}`)
  .then(response => {
    console.log('Elemento eliminado con éxito:', response.data);
  })
  .catch(error => {
    console.error('Error al intentar eliminar el elemento:', error);
  });
};

const getMovies = async (params, body) => {
  try {
    const pagination = params
      ? `?page=${params?.page || 0}&size=${params?.size || 10}&sort=${
          params?.sort || "id"
        },${params?.direction || "asc"}`
      : "";
    const response = await axios.post(`${api}paged/${pagination}`, body);
    return response.data;
  } catch (error) {
    throw error;
  }
};

const getMovie = async (movieId) => {
  try {
    const response = await axios.get(api + movieId);
    return response.data;
  } catch (error) {
    console.log("Obtener uno fallo!", error);
    throw error;
  }
};

const changeStatus = async (movieId) => {
  try {
    const response = await axios.patch(api + movieId);
    return response.data;
  } catch (error) {
    console.log("Change status fallo!", error);
    throw error;
  }
};

const fetchLogs = async () => {
  try {
    const response = await axios.get(api);
    return response.data;
  } catch (error) {
    console.error("Error al obtener los logs:", error);
    throw error;
  }
};


export default {
  getMovies,
  getMovie,
  saveMovie,
  updateMovie,
  changeStatus,
  fetchLogs,
  deleteMovie
};
