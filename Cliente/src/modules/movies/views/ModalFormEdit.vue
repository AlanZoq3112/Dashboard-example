<template>
    <b-modal id="formEdit" title="Editar película" hide-footer="true">
        <form @submit.prevent="editMovie">
            <b-row>
                <b-col cols="12" class="my-2">
                    <b-form-group label="Título" label-for="title">
                        <b-form-input id="title" v-model="movieData.title" required></b-form-input>
                    </b-form-group>
                </b-col>
                <b-col cols="12" class="mb-2">
                    <b-form-group label="Descripción" label-for="description">
                        <b-form-input id="description" v-model="movieData.description" required></b-form-input>
                    </b-form-group>
                </b-col>
                <b-col cols="12" class="mb-2">
                    <b-form-group label="Director" label-for="director">
                        <b-form-input id="director" v-model="movieData.director" required></b-form-input>
                    </b-form-group>
                </b-col>
                <b-col cols="12" class="mb-2">
                    <b-form-group label="Fecha de estreno" label-fro="fecha">
                        <b-form-datepicker id="fecha" v-model="movieData.publishDate"></b-form-datepicker>
                    </b-form-group>
                </b-col>
                <b-col cols="12" class="mb-2">
                    <b-form-group label="Categoría" label-for="category">
                        <b-form-select v-model="selectedCategory.id" :options="categoriesOptions">
                            <template #first>
                                <b-form-select-option :value="null">Selecciona una opción</b-form-select-option>
                            </template>
                        </b-form-select>
                    </b-form-group>
                </b-col>
                <b-col cols="12">
                    <b-row class="d-flex flex-row-reverse">
                        <b-button type="submit" variant="success" class="mx-3">{{
                            isNew ? "Editar" : "Actualizar"
                        }}</b-button>
                        <b-button variant="danger" @click="closeModal">Cancelar</b-button>
                    </b-row>
                </b-col>
            </b-row>
        </form>

        <loading :active.sync="isLoading" :can-cancel="false" :is-full-page="fullPage"></loading>
    </b-modal>
</template>

<script>
import MovieService from "../services/MovieService";
import CategoryService from "../../category/services/CategoryService";

import Loading from "vue-loading-overlay";
import "vue-loading-overlay/dist/vue-loading.css";

export default {
    props: ["isNew", "movieData"],
    components: {
        Loading,
    },
    data() {
        return {
            isLoading: false,
            fullPage: true,
            categoriesOptions: [],
        };
    },
    computed: {
        movieForm() {
            if (this.movieData) {
                return { ...this.movieData };
            } else {
                return {
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
            }
        },
        selectedCategory() {
            if (this.movieData) {
                return { id: this.movieData.category.id };
            } else {
                return null;
            }
        },
    },
    methods: {
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
        async editMovie() {
            try {
               console.log(this.movieData);
                await MovieService.updateMovie(this.movieData);
            } catch (error) {
                console.error(error.message);
            }finally {                
                this.closeModal();
            }
        },        
        closeModal() {
            this.$bvModal.hide("formEdit");
            this.$emit("getMovies");
        },
    },
    mounted() {
        this.getCategories();
    },
};
</script>

<style></style>