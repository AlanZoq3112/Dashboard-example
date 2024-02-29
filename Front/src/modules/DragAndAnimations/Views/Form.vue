<template>
  <div style="height: 1500px" class="content">
    <div class="mx-5 my-5">
      <b-card>
        <h1>Filtrados / Búsquedas</h1>
        <form action="submit">
          <b-row>
            <b-col cols="5">
              <b-form-group
                id="input-group-3"
                label="Búsqueda por:"
                label-for="input-3"
              >
                <b-form-select
                  id="input-3"
                  v-model="optionSearch"
                  :options="opcSearch"
                  required
                ></b-form-select>
              </b-form-group>
            </b-col>
            <b-col cols="7" v-if="optionSearch != 'dates'">
              <b-form-group
                id="input-group-3"
                :label="getSearchLabel(optionSearch)"
                label-for="input-3"
              >
                <b-input-group>
                  <b-input-group-prepend>
                    <b-button @click="typeSearch" variant="primary">
                      <b-icon icon="search"></b-icon>
                    </b-button>
                  </b-input-group-prepend>
                  <b-form-input
                    v-model="nameToSearch"
                    :type="'search'"
                    placeholder="Buscador ..."
                    class="search-input"
                    debounce="500"
                  />
                </b-input-group>
              </b-form-group>
            </b-col>
            <b-col v-if="optionSearch === 'dates'">
              <b-row>
                <b-col>
                  <b-form-group
                    id="input-group-3"
                    label="Desde:"
                    label-for="input-3"
                  >
                    <b-form-datepicker
                      id="datepicker-full-width"
                      menu-class="w-100"
                      v-model="firstDate"
                      calendar-width="100%"
                      class="mb-2"
                    ></b-form-datepicker>
                  </b-form-group>
                </b-col>
                <b-col>
                  <b-form-group
                    id="input-group-3"
                    label="Hasta:"
                    label-for="input-3"
                  >
                    <b-form-datepicker
                      id="datepicker-full-width"
                      menu-class="w-100"
                      v-model="twoDate"
                      calendar-width="100%"
                      class="mb-2"
                    ></b-form-datepicker>
                  </b-form-group>
                </b-col>
                <b-input-group-prepend>
                  <b-button @click="typeSearch" variant="primary" size="sm">
                    <b-icon icon="search"></b-icon>
                  </b-button>
                </b-input-group-prepend>
              </b-row>
            </b-col>
          </b-row>
        </form>
      </b-card>
    </div>
    <div class="content my-5 mx-5" v-show="showElement">
      <b-card>
        <b-form
          draggable
          @dragstart="handleDragStart"
          @dragover.prevent
          @drop="handleDrop"
        >
          <b-row>
            <b-col>
              <label for="">Nombre: *</label>
              <b-input
                type="text"
                v-model="pelicula.name"
                required
                placeholder="Nombre de la pelicula"
              ></b-input>
            </b-col>
            <b-col>
              <label for="pelicula">Genero de la pelicula: *</label>
              <b-form-select
                v-model="pelicula.genero"
                :state="validarGenero"
                :options="options"
              ></b-form-select>
            </b-col>
          </b-row>
          <b-row>
            <b-col>
              <label for="">Fecha de publicación: *</label>
              <b-form-datepicker
                v-model="pelicula.publishDate"
                type="date"
                class="form-control"
                required
                aria-describedby="input-live-help input-live-feedback"
              />
            </b-col>
            <b-col>
              <label for="">Director: *</label>
              <b-input
                type="text"
                v-model="pelicula.director"
                required
                placeholder="Director de la pelicula"
              ></b-input>
            </b-col>
          </b-row>

          <b-row>
            <b-col>
              <label for="">Descripción: *</label>
              <b-form-textarea
                id="textarea"
                v-model="pelicula.description"
                placeholder="Describe la pelicula..."
                rows="3"
                max-rows="6"
              ></b-form-textarea>
            </b-col>
          </b-row>
        </b-form>
      </b-card>
    </div>

    <div @dragover.prevent @drop="handleDrop" class="my-5 mx-5">
      <b-card>
        <p style="color: grey" v-if="!isDragging">
          Arrastra acá para guardar la película
        </p>
        <p style="color: grey" v-if="isDragging">
          Suelta aquí para guardar la película
        </p>
      </b-card>
    </div>

    <div>
      <div>
        <TransitionGroup name="roll" tag="div" class="d-flex d-fixed">
          <b-col v-for="(movie, index) in paginatedItems" :key="index">
            <b-card :title="movie.name" style="height: 100%; width: auto">
              <b-card-text class="card-text-scroll">
                <b>Director:</b> {{ movie.director }}<br />
                <b>Género:</b> {{ movie.genero }}<br />
                <b>Descripción:</b> {{ movie.description }}<br />
                <b>Fecha de estreno:</b> {{ movie.publishDate }}<br />
              </b-card-text>
              <template #footer>
                <div class="icono">
                  <b-button variant="faded" @click="OpenEditModal(movie)"
                    ><b-icon icon="pencil"></b-icon
                  ></b-button>
                  <b-button
                    variant="faded"
                    style="color: red"
                    @click="deleteMovie(movie.id)"
                    ><b-icon icon="trash"></b-icon
                  ></b-button>
                </div>
              </template>
            </b-card>
          </b-col>
        </TransitionGroup>

        <div class="text-center" v-if="!paginatedItems.length">
          <p>No se encontraron películas registradas</p>
        </div>
      </div>

      <div class="d-flex justify-content-between align-items-center">
        <div>
          <b-pagination
            v-model="currentPage"
            :total-rows="rows"
            :per-page="perPage"
            aria-controls="my-table"
            class="mt-3"
          ></b-pagination>

          <p class="mt-3">Página actual: {{ currentPage }}</p>
        </div>

        <div class="mb-3">
          <label for="perPageSelect" class="mr-2">Películas por página:</label>
          <b-form-select
            v-model="perPage"
            id="perPageSelect"
            :options="perPageOptions"
          ></b-form-select>
        </div>
      </div>

      <ModalSaveMovie @movie-updated="fetchData" />
      <ModalUpdateMovie
        ref="ModalUpdateMovie"
        :movie="selectedMovie"
        @movie-updated="fetchData"
      />
    </div>
  </div>
</template>

<script>
import Swal from "sweetalert2";
import ModalUpdateMovie from "../../Movies/Views/ModalUpdateMovie.vue";
import axios from "axios";
export default {
  name: "dragform",
  components: { ModalUpdateMovie },
  data() {
    return {
      data: null,
      pelicula: {
        name: "",
        genero: "",
        publishDate: null,
        description: "",
        director: "",
      },
      isDragging: false,
      showElement: true,
      lastScrollPosition: 0,
      nameToSearch: "",
      opcSearch: [
        { text: "Todo", value: "all" },
        { text: "Nombre películas", value: "name" },
        { text: "Director", value: "director" },
        { text: "Género", value: "genero" },
        { text: "Rango de Fechas", value: "dates" },
      ],
      optionSearch: "",
      firstDate: "",
      twoDate: "",
      search: {
        name: "default",
        description: "default",
        genero: "default",
        director: "default",
        firstDate: "",
        twoDate: "",
      },
      perPage: 4,
      currentPage: 1,
      perPageOptions: [4, 8, 12, 16],
      selectedMovie: null,
      options: [
        { value: null, text: "Selecciona una opción" },
        { value: "Terror", text: "Terror" },
        { value: "Aventura", text: "Aventura" },
        { value: "Acción", text: "Acción" },
        { value: "Catástrofe", text: "Catástrofe" },
        { value: "Ciencia Ficción.", text: "Ciencia Ficción." },
        { value: "Comedia", text: "Comedia" },
        { value: "Documentales", text: "Documentales" },
        { value: "Drama", text: "Drama" },
        { value: "Infantil", text: "Infantil" },
      ],
    };
  },
  computed: {
    rows() {
      return this.data ? this.data.data.length : 0;
    },
    paginatedItems() {
      const start = (this.currentPage - 1) * this.perPage;
      const end = start + this.perPage;
      return this.data ? this.data.data.slice(start, end) : [];
    },
  },
  methods: {
    getSearchLabel(option) {
      switch (option) {
        case "name":
          return "Búsqueda por Nombre de Película:";
        case "director":
          return "Búsqueda por Director:";
        case "genero":
          return "Búsqueda por Género:";
        default:
          return "Búsqueda";
      }
    },
    async typeSearch() {
      switch (this.optionSearch) {
        case "name":
          this.search.name = this.nameToSearch;
          break;
        case "director":
          this.search.director = this.nameToSearch;
          break;
        case "genero":
          this.search.genero = this.nameToSearch;
          break;
        case "dates":
          this.search.firstDate = this.firstDate;
          this.search.twoDate = this.twoDate;
          break;
      }
      this.searchPeli();
    },
    async searchPeli() {
      let URLAPI = "";
      console.log(this.search);
      if (this.optionSearch === "all") {
        this.fetchData();
      } else if (this.search.firstDate != "") {
        URLAPI = "findRangeDates/";
      } else {
        URLAPI = "findFilter/";
      }
      if (URLAPI != null || URLAPI != "") {
        try {
          await axios
            .post(`http://localhost:8080/api-movieBack/${URLAPI}`, this.search)
            .then((response) => {
              console.log(response);
              this.data = response.data;
            });
        } catch (error) {
          console.error("Error en la búsqueda ->", error);
        }
      }
    },
    async save() {
      Swal.fire({
        title: "¿Estás seguro de registrar la pelicula?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#008c6f",
        cancelButtonColor: "#e11c24",
        confirmButtonText: "Confirmar",
        cancelButtonText: "Cancelar",
      }).then(async (result) => {
        if (result.isConfirmed) {
          try {
            await axios.post(
              "http://localhost:8080/api-movieBack/",
              this.pelicula
            );
            Swal.fire({
              title: "¡Guardada!",
              text: "La pelicula se registró correctamente",
              icon: "success",
            });
            (this.pelicula.name = ""),
              (this.pelicula.genero = ""),
              (this.pelicula.director = ""),
              (this.pelicula.publishDate = null);
            this.pelicula.description = "";
          } catch (error) {
            console.log("Error al guardar la pelicula", error);
          } finally {
            this.fetchData();
          }
        }
      });
    },
    handleDragStart(event) {
      event.dataTransfer.setData("text/plain", JSON.stringify(this.pelicula));
    },
    handleDrop(event) {
      const data = JSON.parse(event.dataTransfer.getData("text/plain"));

      this.pelicula = data;

      this.save();
    },
    fetchData() {
      axios
        .get("http://localhost:8080/api-movieBack/")
        .then((response) => {
          this.data = response.data;
        })
        .catch((error) => {
          console.error("Error al obtener datos de la API", error);
        });
    },
    async deleteMovie(id) {
      const confirmed = await Swal.fire({
        title: "¿Estás seguro de eliminar la película?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#008c6f",
        cancelButtonColor: "#e11c24",
        confirmButtonText: "Sí, eliminar",
        cancelButtonText: "Cancelar",
      });

      if (confirmed.isConfirmed) {
        try {
          const response = await axios.delete(
            `http://localhost:8080/api-movieBack/peliculas/${id}`
          );
          if (response.data.error) {
            console.error(response.data.message);
          } else {
            Swal.fire({
              title: "Eliminada",
              text: "La película se eliminó correctamente",
              icon: "success",
              timer: 3000,
            });
            this.fetchData();
          }
        } catch (error) {
          const { data } = error;
          this.$swal.fire({
            icon: "error",
            text: data?.text ? data.text : "Error interno",
            timer: 3000,
          });
        }
      }
    },
    onScroll() {
      const currentScrollPosition =
        window.pageYOffset || document.documentElement.scrollTop;
      if (Math.abs(currentScrollPosition - this.lastScrollPosition) < 60) {
        return;
      }
      this.showElement = currentScrollPosition < this.lastScrollPosition;
      this.lastScrollPosition = currentScrollPosition;
    },
    OpenEditModal(movie) {
      this.selectedMovie = movie;
      this.$bvModal.show("modal-update-movie");
    },
    filterMovies() {
      this.currentPage = 1;
      this.fetchData();
    },
  },
  mounted() {
    this.fetchData(), window.addEventListener("scroll", this.onScroll);
  },
  beforeDestroy() {
    window.removeEventListener("scroll", this.onScroll);
  },
};
</script>

<style scoped></style>
