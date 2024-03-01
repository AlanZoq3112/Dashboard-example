<template>
  <div>
    <div>
      <br/>
      <div>
        <h1>Buscador <b-icon icon="search"></b-icon></h1>
      </div>
      <br />
      <b-card
        class="shadow"
        style="display: flex; justify-content: center; align-items: center"
        v-show="showElement"
      >
        <b-row>
          <b-col cols="3">
            <b-form-group label="Título de la película:" label-for="title">
              <b-form-input
                id="title"
                v-model="filters.title"
                placeholder="Título de la película..."
                :type="'search'"
              />
            </b-form-group>
          </b-col>
          <b-col cols="3">
            <b-form-group label="Director:" label-for="director">
              <b-form-input
                v-model="filters.director"
                placeholder="Director..."
                :type="'search'"
              />
            </b-form-group>
          </b-col>
          <b-col cols="3">
            <b-form-group label="De:" label-for="fecha1">
              <b-form-datepicker
                id="fecha1"
                v-model="filters.firstDate"
              ></b-form-datepicker>
            </b-form-group>
          </b-col>
          <b-col cols="3">
            <b-form-group label="Hasta:" label-for="fecha2">
              <b-form-datepicker
                id="fecha2"
                v-model="filters.secondDate"
              ></b-form-datepicker>
            </b-form-group>
          </b-col>
          <b-col cols="6">
            <b-form-group label="Categorías:" label-for="categories">
              <b-form-select
                id="categories"
                v-model="filters.categoryId"
                :options="categoriesOptions"
              >
                <template #first>
                  <b-form-select-option :value="null"
                    >Categoríás</b-form-select-option
                  >
                </template>
              </b-form-select>
            </b-form-group>
          </b-col>
          <b-col cols="6">
            <b-form-group label="Ordenar por:" label-for="sorts">
              <b-form-select
                id="sorts"
                v-model="customOrder.sort"
                :options="sortOptions"
              />
            </b-form-group>
          </b-col>
          <b-col cols="4">
            <b-form-group label="Dirección:" label-for="directions">
              <b-form-select
                id="directions"
                v-model="customOrder.direction"
                :options="directionOptions"
              />
            </b-form-group>
          </b-col>
          <b-col class="mt-3">
            <b-row class="d-flex flex-row-reverse">
              <b-button
                @click="searchMovies"
                variant="primary"
                class="mr-3 my-2"
              >
                <b-icon icon="search"></b-icon>
                Buscar
              </b-button>
            </b-row>
          </b-col>
        </b-row>
      </b-card>
      <b-row> </b-row>
      <br />
      <h1>Películas <b-icon icon="collection-play-fill"></b-icon></h1>
      <b-button @click="addMovie" variant="success" class="mr-3 my-2">
        <b-icon icon="plus-lg"></b-icon>
        Agregar nueva pelicula
      </b-button>
      <br />
      <br/>
      <b-row v-if="movies.length > 0">
        <b-col cols="12">
          <b-row>
            <TransitionGroup
              name="zoom"
              tag="div"
              class="d-flex flex-row flex-wrap"
            >
              <b-col
                cols="12"
                sm="4"
                md="4"
                lg="3"
                v-for="movie in movies"
                :key="movie.id"
              >
                <b-card
                  :title="movie.title"
                  img-src="https://indicepolitico.com/wp-content/uploads/2021/06/cinedot.jpg"
                  img-alt="Image"
                  img-top
                  tag="article"
                  style="max-width: 40rem"
                  class="mb-2 shadow"
                >
                  <b-card-text
                    >Description: {{ movie.description }}</b-card-text
                  >
                  <b-card-text>Director: {{ movie.director }}</b-card-text>
                  <b-card-text
                    >Fecha de estreno: {{ movie.publishDate }}</b-card-text
                  >
                  <b-card-text
                    >Categoria: {{ movie.category.name }}</b-card-text
                  >
                </b-card>
              </b-col>
            </TransitionGroup>
          </b-row>
        </b-col>
      </b-row>

      <b-row v-else>
        <b-col cols="12" class="mt-5">
          <b-card class="text-center">
            <div>No hay películas que mostrar :(</div>
          </b-card>
        </b-col>
      </b-row>

      <ModalForm
        :movieData="movieData"
        :isNew="isNewMovie"
        @getMovies="getMovies"
      />

      <LogsMovies/>

      <loading
        :active.sync="isLoading"
        :can-cancel="false"
        :is-full-page="fullPage"
      ></loading>
    </div>
  </div>
</template>

<script>
import MovieService from "../services/MovieService";
import CategoryService from "../../category/services/CategoryService";
import ModalForm from "./ModalForm.vue";
import LogsMovies from "./LogsMovies.vue";

import Loading from "vue-loading-overlay";
import "vue-loading-overlay/dist/vue-loading.css";

export default {
  components: {
    ModalForm,
    Loading,
    LogsMovies,
  },
  data() {
    return {
      showElement: true,
      pagination: {
        page: 1,
        size: 6,
        sort: "id",
        direction: "asc",
      },
      filters: {
        title: "",
        director: "",
        categoryId: null,
        firstDate: null,
        secondDate: null,
      },
      customOrder: {
        sort: "id",
        direction: "asc",
      },
      categoriesOptions: [],
      sortOptions: [
        { value: "id", text: "Normal" },
        { value: "publishDate", text: "Fecha" },
        { value: "title", text: "Título" },
      ],
      directionOptions: [
        { value: "asc", text: "Ascendente" },
        { value: "desc", text: "Descendente" },
      ],
      rows: null,
      isLoading: false,
      fullPage: true,
      movies: [],
      isNewMovie: true,
      movieData: {
        id: null,
        title: null,
        description: null,
        director: null,
        image: null,
        publishDate: null,
        status: null,
        category: {
          id: null,
          name: null,
        },
      },
    };
  },
  methods: {
    addMovie() {
      this.$bvModal.show("form");
      this.isNewMovie = true;
      this.resetMovieData();
    },
    async updateMovie(movie) {
      this.$bvModal.show("form");
      this.isModalOpen = true;
      this.isNewMovie = false;
      const { data: selectedMovie } = await MovieService.getMovie(movie.id);
      this.movieData = { ...selectedMovie };
    },
    resetMovieData() {
      this.movieData = {
        id: null,
        title: null,
        description: null,
        director: null,
        image: null,
        publishDate: null,
        status: null,
        category: {
          id: null,
          name: null,
        },
      };
    },
    async changeStatus(movie) {
      this.isLoading = true;
      try {
        const { error: statusUpdated } = await MovieService.changeStatus(
          movie.id
        );
        if (!statusUpdated) this.getMovies();
      } catch (error) {
        console.error(error);
      } finally {
        this.isLoading = false;
      }
    },
    async getMovies() {
      this.isLoading = true;
      try {
        const { data: res } = await MovieService.getMovies({
          page: this.pagination.page - 1,
          size: this.pagination.size,
          sort: this.pagination.sort,
          direction: this.pagination.direction,
        });
        this.movies = res.content;
        this.rows = res.totalElements;
      } catch (error) {
        console.error(error.message);
      } finally {
        this.isLoading = false;
      }
    },
    async getCategories() {
      try {
        const {
          data: { content: res },
        } = await CategoryService.getCategories(null);
        this.categoriesOptions = res.map((c) => ({
          value: c.id,
          text: c.name,
        }));
      } catch (error) {
        console.error(error.message);
      }
    },
    async searchMovies() {
      console.log(this.filters);
      console.log(this.customOrder);
      this.isLoading = true;
      try {
        const { data: res } = await MovieService.getMovies(
          {
            page: 0,
            size: this.pagination.size,
            sort: this.customOrder.sort,
            direction: this.customOrder.direction,
          },
          this.filters
        );
        this.movies = res.content;
        this.rows = res.totalElements;
      } catch (error) {
        console.error(error.message);
      } finally {
        this.isLoading = false;
      }
    },
    onScroll() {
      const currentScrollPosition =
        window.pageYOffset || document.documentElement.scrollTop;
      console.log(currentScrollPosition);

      if (Math.abs(currentScrollPosition - this.lastScrollPosition) < 60) {
        return;
      }
      this.showElement = currentScrollPosition < this.lastScrollPosition;
      //
      this.lastScrollPosition = currentScrollPosition;
    },

    handlePageChange(newPage) {
      this.pagination.page = newPage;
      this.getMovies();
    },
  },
  mounted() {
    this.getMovies();
    this.getCategories();
    window.addEventListener("scroll", this.onScroll);
  },
  beforeDestroy() {
    window.removeEventListener("scroll", this.onScroll);
  },
};
</script>

<style>
.zoom-enter-active,
.zoom-leave-active {
  transition: transform 0.5s;
}

.zoom-enter,
.zoom-leave-to {
  transform: scale(0);
}
.shadow {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style>