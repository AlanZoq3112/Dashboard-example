<template>
  <div class="body">

    <template>
      <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
          <b>
            <h3>Peliculas CRUD <b-icon icon="camera-reels"></b-icon></h3>
          </b>
        </div>
        <div class="bodybutton">
          <b-button v-b-modal.modal-save-movie class="btnadd">
            <b-icon icon="plus"></b-icon> Registrar pelicula
          </b-button>
        </div>
      </div>
    </template>

    <br>

    <div>
      <TransitionGroup name="roll" tag="div" class="d-flex d-fixed">
        <b-col v-for="(movie, index) in paginatedItems" :key="index">
          <b-card :title="movie.name" style="height: 100%; width:auto">
            <b-card-text class="card-text-scroll">
              <b>Director:</b> {{ movie.director }}<br>
              <b>Género:</b> {{ movie.genero }}<br>
              <b>Descripción:</b> {{ movie.description }}<br>
              <b>Fecha de estreno:</b> {{ movie.publishDate }}<br>
            </b-card-text>
            <template #footer>
              <div class="icono">
                <b-button variant="faded" @click="OpenEditModal(movie)"><b-icon icon="pencil"></b-icon></b-button>
                <b-button variant="faded" style="color: red;" @click="deleteMovie(movie.id)"><b-icon
                    icon="trash"></b-icon></b-button>
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
        <b-pagination v-model="currentPage" :total-rows="rows" :per-page="perPage" aria-controls="my-table"
          class="mt-3"></b-pagination>

        <p class="mt-3">Página actual: {{ currentPage }}</p>
      </div>

      <div class="mb-3">
        <label for="perPageSelect" class="mr-2">Películas por página:</label>
        <b-form-select v-model="perPage" id="perPageSelect" :options="perPageOptions"></b-form-select>
      </div>
    </div>

    <ModalSaveMovie @movie-updated="fetchData" />
    <ModalUpdateMovie ref="ModalUpdateMovie" :movie="selectedMovie" @movie-updated="fetchData" />

  </div>
</template>

<script>
import ModalSaveMovie from '@/modules/Movies/Views/ModalSaveMovie.vue';
import ModalUpdateMovie from './ModalUpdateMovie.vue';
import axios from 'axios'
import Swal from 'sweetalert2';

export default {
  components: { ModalSaveMovie, ModalUpdateMovie },
  name: "pelis",
  data() {
    return {
      firstDate: '',
      twoDate: '',
      selectedGenre: null,
      genreInput: null,
      data: null,
      selectedMovie: null,
      perPage: 4,
      currentPage: 1,
      perPageOptions: [4, 8, 12, 16]
    }
  },
  computed: {
    rows() {
      return this.data ? this.data.data.length : 0;
    },
    paginatedItems() {
      const start = (this.currentPage - 1) * this.perPage;
      const end = start + this.perPage;
      return this.data ? this.data.data.slice(start, end) : [];
    }
  },
  methods: {
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
    OpenEditModal(movie) {
      this.selectedMovie = movie;
      this.$bvModal.show('modal-update-movie');
    },
    async deleteMovie(id) {
      const confirmed = await Swal.fire({
        title: "¿Estás seguro de eliminar la película?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#008c6f',
        cancelButtonColor: '#e11c24',
        confirmButtonText: "Sí, eliminar",
        cancelButtonText: 'Cancelar',
      });

      if (confirmed.isConfirmed) {
        try {
          const response = await axios.delete(`http://localhost:8080/api-movieBack/peliculas/${id}`);
          if (response.data.error) {
            console.error(response.data.message);
          } else {
            Swal.fire({
              title: 'Eliminada',
              text: 'La película se eliminó correctamente',
              icon: 'success',
              timer: 3000
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
    filterMovies() {
      this.currentPage = 1;
      this.fetchData();
    },
  },
  mounted() {
    this.fetchData();
  },
};
</script>

<style>
.body {
  padding-left: 20px;
  padding-right: 20px;
  margin-top: 10px;
}

@media screen and (max-width: 768px) {
  .body {
    padding-left: 10px;
    padding-right: 10px;
  }
}


.btnadd {
  background-color: #089779;
  padding-right: px;
}

.icono {
  text-align: end;
}

.card-text-scroll {
  max-height: 150px;
  overflow-y: auto;
  /* añade una barra de desplazamiento vertical si es necesario */
}
</style>